package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

import java.time.LocalDate;

public class CardDTO {

    private long id;

    private String cardholder;

    private CardType cardType;

    private CardColor cardColor;

    private String cardNumber;

    private short cvv;

    private LocalDate thruDate;

    private LocalDate fromDate;

    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardholder = card.getCardholder();
        this.cardType = card.getCardType();
        this.cardColor = card.getCardColor();
        this.cardNumber = card.getCardNumber();
        this.cvv = card.getCvv();
        this.thruDate = card.getThruDate();
        this.fromDate = card.getFromDate();

    }

    public long getId() {return id;}

    public String getCardholder() {return cardholder;}

    public CardType getCardType() {return cardType;}

    public CardColor getCardColor() {return cardColor;}

    public String getCardNumber() {return cardNumber;}

    public short getCvv() {return cvv;}

    public LocalDate getThruDate() {return thruDate;}

    public LocalDate getFromDate() {return fromDate;}



}
