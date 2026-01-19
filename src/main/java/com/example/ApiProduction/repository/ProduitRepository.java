package com.example.ApiProduction.repository;

import com.example.ApiProduction.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
}