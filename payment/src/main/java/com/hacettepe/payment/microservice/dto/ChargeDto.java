package com.hacettepe.payment.microservice.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ChargeDto {
    private String token;
    private String username;
    private Double amount;
    private Boolean success;
    private String message;
    private String chargeId;
    private Map<String, Object> additionalInfo = new HashMap<>();
}
