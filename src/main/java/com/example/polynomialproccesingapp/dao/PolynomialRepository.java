package com.example.polynomialproccesingapp.dao;

import com.example.polynomialproccesingapp.model.PolynomialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolynomialRepository extends JpaRepository<PolynomialEntity, String> {
}
