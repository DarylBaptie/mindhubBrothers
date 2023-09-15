package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.services.CardService;

import java.util.Random;

public final class CardUtils {

    public static int getCvv() {
        int cvv = (int)((Math.random() * (999 - 100)) + 100);
        return cvv;
    }

    public static String getCardNumber() {
        String
        cardNumber = (int) ((Math.random() * (9999 - 1000)) + 1000)
                + "-" + (int) ((Math.random() * (9999 - 1000)) + 1000)
                + "-" + (int) ((Math.random() * (9999 - 1000)) + 1000)
                + "-" + (int) ((Math.random() * (9999 - 1000)) + 1000);
        return cardNumber;
    }

}
