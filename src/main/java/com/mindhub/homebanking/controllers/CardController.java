package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@RestController

public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private ClientService clientService;

    private LocalDate cardEmissionDate = LocalDate.now();
    private LocalDate cardExpiryDate =  cardEmissionDate.plusYears(5);





    @GetMapping("/api/cards")
    public List<CardDTO> getCards() {
        return cardService.getCardsDTO();
    }



    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<Object> createCard(
            Authentication authentication,
            @RequestParam String type,
            @RequestParam String color
    )

    {

        String cardNumber = CardUtils.getCardNumber();

        int cvv = CardUtils.getCvv();

        Client client = clientService.findClientByEmail(authentication.getName());

        Boolean isActive = true;

        if (type.isBlank()) {
            return new ResponseEntity<>("Card type is missing", HttpStatus.FORBIDDEN);
        }

        if (color.isBlank()) {
            return new ResponseEntity<>("Card color is missing", HttpStatus.FORBIDDEN);
        }

     if (!client.getCards().stream().filter(card -> card.getCardType().equals(CardType.valueOf(type)) && card.getIsActive() && card.getCardColor().equals(CardColor.valueOf(color))).collect(Collectors.toSet()).isEmpty())
        {
            return new ResponseEntity<>("Apologies, you already have this card", HttpStatus.FORBIDDEN);
        } else {
                String clientName = client.getFirstName() + " " + client.getLastName();
                Card newCard = new Card(clientName, CardType.valueOf(type), CardColor.valueOf(color), cardNumber, cvv, cardEmissionDate, cardExpiryDate, isActive);
                client.addCard(newCard);
                cardService.saveCard(newCard);
                return new ResponseEntity<>("Card created", HttpStatus.CREATED);

            }
    }

    @PatchMapping("/api/clients/current/cards/deactivate")
    public ResponseEntity<Object> disableCard(@RequestParam long id, Authentication authentication){
        Card card = cardService.findById(id);
        Client client = clientService.findClientByEmail(authentication.getName());
        Boolean cardIsClients = client.getCards().contains(card);
        if(card == null){
            return  new ResponseEntity<>("This card does not exist", HttpStatus.FORBIDDEN);
        }
        if (!cardIsClients){
            return  new ResponseEntity<>("This card does not belong to this client", HttpStatus.FORBIDDEN);
        }
        if(!card.getIsActive()){
            return new ResponseEntity<>("This card is already deactivated", HttpStatus.FORBIDDEN);
        }
        card.isActive(false);
        cardService.saveCard(card);
        return  new ResponseEntity<>("The card has been deactivated successfully", HttpStatus.OK);
    }

}
