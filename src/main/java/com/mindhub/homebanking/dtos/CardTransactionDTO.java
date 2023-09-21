package com.mindhub.homebanking.dtos;

public class CardTransactionDTO {



    private int amount;

    private String description;

    private String cardNumber;


    private int cvv;




    public String getCardNumber() {
        return cardNumber;
    }

    public int getAmount() {
        return amount;
    }

    public int getCvv() {
        return cvv;
    }

    public String getDescription() {
        return description;
    }

}
