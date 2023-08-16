package com.mindhub.homebanking.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity



public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private long id;

    private String cardholder;

    private CardType cardType;

    private CardColor cardColor;

    private String cardNumber;

    private short cvv;

    private LocalDate thruDate;

    private LocalDate fromDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    public Card() {}

    public Card(String cardholder, CardType cardType, CardColor cardColor, String cardNumber, short cvv, LocalDate thruDate, LocalDate fromDate) {
        this.cardholder = cardholder;
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.thruDate = thruDate;
        this.fromDate = fromDate;
    }

    public long getId() {return id;}

    public void setCardholder() {this.cardholder = cardholder;}

    public String getCardholder() {return cardholder;}

    public void setCardType() {this.cardType = cardType;}

    public CardType getCardType() {return cardType;}

    public void setCardColor() {this.cardColor = cardColor;}

    public CardColor getCardColor() {return cardColor;}

    public void setCardNumber() {this.cardNumber = cardNumber;}

    public String getCardNumber() {return cardNumber;}

    public void setCvv() {this.cvv = cvv;}

    public short getCvv() {return cvv;}

    public void setThruDate() {this.thruDate = thruDate;}

    public LocalDate getThruDate() {return thruDate;}

    public void setFromDate() {this.fromDate = fromDate;}

    public LocalDate getFromDate() {return fromDate;}

    @JsonIgnore
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String toString() {
        return id + " " + cardholder + " " + cardType + " " + cardColor + " " + cardNumber + " " + cvv + " " + thruDate + " " + fromDate;
    }




}
