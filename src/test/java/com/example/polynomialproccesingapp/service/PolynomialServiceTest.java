package com.example.polynomialproccesingapp.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PolynomialServiceTest {

    @Test
    void simplifyPolynomial_withSimplePolynomial() {
        PolynomialService service = new PolynomialService();
        String result = service.simplifyPolynomial("2x + 3x");
        assertEquals("5x", result);
    }

    @Test
    void simplifyPolynomial_withComplexPolynomial() {
        PolynomialService service = new PolynomialService();
        String result = service.simplifyPolynomial("2x^2 + 3x - x^2 + 4");
        assertEquals("x^2+3x+4", result);
    }

    @Test
    void simplifyPolynomial_withZeroCoefficient() {
        PolynomialService service = new PolynomialService();
        String result = service.simplifyPolynomial("2x - 2x");
        assertEquals("0", result);
    }

    @Test
    void evaluatePolynomial_withSimplePolynomial() {
        PolynomialService service = new PolynomialService();
        int result = service.evaluatePolynomial("5x", 2);
        assertEquals(10, result);
    }

    @Test
    void evaluatePolynomial_withComplexPolynomial() {
        PolynomialService service = new PolynomialService();
        int result = service.evaluatePolynomial("x^2 + 3x + 4", 2);
        assertEquals(14, result);
    }
}