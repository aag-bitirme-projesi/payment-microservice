package com.hacettepe.payment.microservice.controller;

import com.hacettepe.payment.microservice.dto.ChargeDto;
import com.hacettepe.payment.microservice.dto.TokenDto;
import com.hacettepe.payment.microservice.service.StripeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stripe")
@AllArgsConstructor
public class StripeController {
    private final StripeService stripeService; //todo add service interface and change this

    @PostMapping("/card/token")
    @ResponseBody
    public TokenDto createCardToken(@RequestBody TokenDto tokenDto) {
        return stripeService.createCardToken(tokenDto);
    }

    @PostMapping("/charge")
    @ResponseBody
    public ChargeDto createCardToken(@RequestBody ChargeDto chargeDto) {
        System.out.println("Helloworld");
        System.out.println(chargeDto);

        return stripeService.charge(chargeDto);
    }
}
