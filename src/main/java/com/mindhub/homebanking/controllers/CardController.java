package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mindhub.homebanking.models.CardType.CREDIT;
import static com.mindhub.homebanking.models.CardType.DEBIT;
import static java.util.stream.Collectors.toList;

@RestController

public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private ClientService clientService;

    private LocalDate cardEmissionDate = LocalDate.now();
    private LocalDate cardExpiryDate =  cardEmissionDate.plusYears(5);

    Random random = new Random();

    int cvv = random.nextInt(900) + 100;


    String cardNumber = String.format("%04d", random.nextInt(10000)) + " " + String.format("%04d", random.nextInt(10000)) + " " + String.format("%04d", random.nextInt(10000)) + " " + String.format("%04d", random.nextInt(10000));



    @RequestMapping("/api/cards")
    public List<CardDTO> getCards() {
        return cardService.getCardsDTO();
    }



    @RequestMapping(path="/api/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCard(
            Authentication authentication,
            @RequestParam String type,
            @RequestParam String color
    )

    {

            Client client = clientService.findClientByEmail(authentication.getName());


        if (type.isBlank()) {
            return new ResponseEntity<>("Card type is missing", HttpStatus.FORBIDDEN);
        }

        if (color.isBlank()) {
            return new ResponseEntity<>("Card color is missing", HttpStatus.FORBIDDEN);
        }

     if (!client.getCards().stream().filter(card -> card.getCardType().equals(CardType.valueOf(type)) && card.getCardColor().equals(CardColor.valueOf(color))).collect(Collectors.toSet()).isEmpty())
        {
            return new ResponseEntity<>("Apologies, you already have this card", HttpStatus.FORBIDDEN);
        } else {
                String clientName = client.getFirstName() + " " + client.getLastName();
                Card newCard = new Card(clientName, CardType.valueOf(type), CardColor.valueOf(color), cardNumber, cvv, cardEmissionDate,cardExpiryDate);
                client.addCard(newCard);
                cardService.saveCard(newCard);
                return new ResponseEntity<>("Card created", HttpStatus.CREATED);

            }


    }

}
