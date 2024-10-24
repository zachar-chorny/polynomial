package com.example.polynomialproccesingapp.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolynomialEntity {
    @Id
    private String polynomial;
    private String simplified;
    @ElementCollection
    private Map<Integer, Integer> results;
}
