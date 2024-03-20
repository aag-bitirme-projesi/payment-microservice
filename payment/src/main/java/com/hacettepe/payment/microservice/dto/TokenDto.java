package com.hacettepe.payment.microservice.dto;

import lombok.Data;

@Data
public class TokenDto {
    private String cardNumber;
    private String expMonth;
    private String expYear;
    private String cvc;
    private String token;
    //private String username;
    private boolean success;
}
