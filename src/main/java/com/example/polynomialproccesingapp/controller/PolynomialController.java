package com.example.polynomialproccesingapp.controller;

import com.example.polynomialproccesingapp.lib.ValidPolynomial;
import com.example.polynomialproccesingapp.model.dto.RequestWithValueDto;
import com.example.polynomialproccesingapp.service.PolynomialCacheService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/polynomial")
@AllArgsConstructor
public class PolynomialController {
    private final PolynomialCacheService polynomialCacheService;

    @PostMapping("/simplify")
    public String simplifyPolynomial(@RequestBody @ValidPolynomial String polynomial) {
        return polynomialCacheService.getSimplifiedPolynomial(polynomial);
    }

    @PostMapping("/value")
    public int getValue(@Valid @RequestBody RequestWithValueDto request) {
        return polynomialCacheService.getValue(request.getPolynomial(), request.getValue());
    }
}
