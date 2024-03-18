package com.hacettepe.payment.microservice.service;

import com.hacettepe.payment.microservice.dto.ChargeDto;
import com.hacettepe.payment.microservice.dto.TokenDto;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Token;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class StripeService {
    @Value("${STRIPE_SECRET_KEY}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public TokenDto createCardToken(TokenDto tokenDto) {
        try {
            Map<String, Object> card = new HashMap<>();
            card.put("number", tokenDto.getCardNumber());
            card.put("exp_month", tokenDto.getExpMonth());
            card.put("exp_year", tokenDto.getExpYear());
            card.put("cvc", tokenDto.getCvc());

            Map<String, Object> params = new HashMap<>();
            params.put("card", card);

            Token token = Token.create(params);

            if (token != null && token.getId() != null) {
                tokenDto.setSuccess(true);
                tokenDto.setToken(token.getId());
            }

            return tokenDto;
        } catch (StripeException e) {
            log.error("StripeService createCardToken", e);
            throw new RuntimeException((e.getMessage()));
        }
    }

    public ChargeDto charge(ChargeDto chargeRequest) {
        try {
            chargeRequest.setSuccess(true);

            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", (int) (chargeRequest.getAmount() * 100));
            chargeParams.put("currency", "USD");
            chargeParams.put("description", "Payment for id" + chargeRequest.getAdditionalInfo().getOrDefault("ID_TAG", ""));
            //chargeParams.put("source", chargeRequest.getToken());
            chargeParams.put("source", "tok_visa");

            Map<String, Object> metaData = new HashMap<>();
            metaData.put("id", chargeRequest.getChargeId());
            metaData.putAll(chargeRequest.getAdditionalInfo());
            chargeParams.put("metadata", metaData);

            Charge charge = Charge.create(chargeParams);
            chargeRequest.setMessage(charge.getOutcome().getSellerMessage());

            if (charge.getPaid()) {
                chargeRequest.setChargeId(charge.getId());
                chargeRequest.setSuccess(true);
            }

            return chargeRequest;
        } catch (StripeException e) {
            log.error("StripeService charge", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
