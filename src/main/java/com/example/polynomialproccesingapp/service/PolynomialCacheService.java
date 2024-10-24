package com.example.polynomialproccesingapp.service;

import com.example.polynomialproccesingapp.dao.PolynomialRepository;
import com.example.polynomialproccesingapp.model.PolynomialEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@AllArgsConstructor
public class PolynomialCacheService {
    private final PolynomialService polynomialService;
    private final PolynomialRepository polynomialRepository;

    public String getSimplifiedPolynomial(String polynomial) {
        return getOrCreate(polynomial).getSimplified();
    }

    public int getValue(String polynomial, int value) {
        PolynomialEntity entity = getOrCreate(polynomial);
        if (entity.getResults().containsKey(value)) {
            return entity.getResults().get(value);
        }
        int result = polynomialService.evaluatePolynomial(entity.getSimplified(), value);
        entity.getResults().put(value, result);
        polynomialRepository.save(entity);
        return result;
    }

    private PolynomialEntity getOrCreate(String polynomial) {
        return polynomialRepository.findById(polynomial)
                .orElseGet(() -> {
                    String simplified = polynomialService.simplifyPolynomial(polynomial);
                    return polynomialRepository.save(new PolynomialEntity(polynomial, simplified, new HashMap<>()));
                });
    }
}
