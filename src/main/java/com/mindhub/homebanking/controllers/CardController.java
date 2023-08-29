package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

import static com.mindhub.homebanking.models.CardType.CREDIT;
import static com.mindhub.homebanking.models.CardType.DEBIT;
import static java.util.stream.Collectors.toList;

@RestController

public class CardController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    private LocalDate cardEmissionDate = LocalDate.now();
    private LocalDate cardExpiryDate =  cardEmissionDate.plusYears(5);

    Random random = new Random();

    int cvv = random.nextInt(900) + 100;


    String cardNumber = String.format("%04d", random.nextInt(9999)) + " " + String.format("%04d", random.nextInt(9999)) + " " + String.format("%04d", random.nextInt(9999)) + " " + String.format("%04d", random.nextInt(9999));



    @RequestMapping("/api/cards")
    public List<CardDTO> getCards() {
        return cardRepository.findAll().stream().map(card -> new CardDTO(card)).collect(toList());
    }

    @RequestMapping("/api/cards/{id}")
    public CardDTO getCard(@PathVariable Long id) {
        return cardRepository.findById(id).map(CardDTO::new).orElse(null);
    }

@RequestMapping(path="/api/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCard(
            Authentication authentication,
            @RequestParam String type,
            @RequestParam String color
    )

    {

            Client client = clientRepository.findByEmail(authentication.getName());
            Set <Card> cards = client.getCards();
            Stream <Card> debitCards = cards.stream().filter(card -> card.getCardType().equals(DEBIT));
            Stream <Card> creditCards = cards.stream().filter(card -> card.getCardType().equals(CREDIT));

        if (type.isBlank()) {
            return new ResponseEntity<>("Card type is missing", HttpStatus.FORBIDDEN);
        }

        if (color.isBlank()) {
            return new ResponseEntity<>("Card color is missing", HttpStatus.FORBIDDEN);
        }


        if (CardType.valueOf(type) == DEBIT && debitCards.count() >= 3) {
                return new ResponseEntity<>("Maximum of three debit cards permitted per account", HttpStatus.FORBIDDEN);
            } else if (CardType.valueOf(type) == CREDIT && creditCards.count() >= 3) {
                return new ResponseEntity<>("Maximum of three credit cards permitted per account", HttpStatus.FORBIDDEN);
            } else {
                String clientName = client.getFirstName() + " " + client.getLastName();
                Card newCard = new Card(clientName, CardType.valueOf(type), CardColor.valueOf(color), cardNumber, cvv, cardEmissionDate,cardExpiryDate);
                client.addCard(newCard);
                cardRepository.save(newCard);

                return new ResponseEntity<>("Card created", HttpStatus.CREATED);

            }


    }

}
