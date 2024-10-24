package com.example.polynomialproccesingapp.model.dto;

import com.example.polynomialproccesingapp.lib.ValidPolynomial;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestWithValueDto {
    private int value;
    @ValidPolynomial
    private String polynomial;
}
