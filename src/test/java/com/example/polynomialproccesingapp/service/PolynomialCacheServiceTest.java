package com.example.polynomialproccesingapp.service;

import com.example.polynomialproccesingapp.dao.PolynomialRepository;
import com.example.polynomialproccesingapp.model.PolynomialEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PolynomialCacheServiceTest {
    @Mock
    private PolynomialRepository mockRepository;
    @Mock
    private PolynomialService mockService;
    @InjectMocks
    private PolynomialCacheService cacheService;
    private static final String POLYNOMIAL = "2x + 3x";
    private static final String SIMPLIFIED = "5x";


    @Test
    void getSimplifiedPolynomial_withExistingPolynomial() {
        when(mockRepository.findById(POLYNOMIAL)).thenReturn(Optional.of(new PolynomialEntity(POLYNOMIAL, SIMPLIFIED, new HashMap<>())));

        String result = cacheService.getSimplifiedPolynomial(POLYNOMIAL);
        assertEquals(SIMPLIFIED, result);
    }

    @Test
    void getSimplifiedPolynomial_withNewPolynomial() {
        when(mockRepository.findById(POLYNOMIAL)).thenReturn(Optional.empty());
        when(mockService.simplifyPolynomial(POLYNOMIAL)).thenReturn(SIMPLIFIED);
        when(mockRepository.save(any(PolynomialEntity.class))).thenReturn(new PolynomialEntity(POLYNOMIAL, SIMPLIFIED, new HashMap<>()));

        String result = cacheService.getSimplifiedPolynomial(POLYNOMIAL);
        assertEquals(SIMPLIFIED, result);
    }

    @Test
    void getValue_withCachedResult() {
        HashMap<Integer, Integer> results = new HashMap<>();
        results.put(2, 10);
        PolynomialEntity entity = new PolynomialEntity("5x", "5x", results);

        when(mockRepository.findById("5x")).thenReturn(Optional.of(entity));

        int result = cacheService.getValue("5x", 2);
        assertEquals(10, result);
    }

    @Test
    void getValue_withNewResult() {
        PolynomialEntity entity = new PolynomialEntity(POLYNOMIAL, SIMPLIFIED, new HashMap<>());

        when(mockRepository.findById(POLYNOMIAL)).thenReturn(Optional.of(entity));
        when(mockService.evaluatePolynomial(SIMPLIFIED, 2)).thenReturn(10);

        int result = cacheService.getValue(POLYNOMIAL, 2);
        assertEquals(10, result);
        verify(mockRepository).save(entity);
    }
}
