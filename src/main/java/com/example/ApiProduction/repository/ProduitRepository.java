package com.example.ApiProduction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ApiProduction.entity.Produit;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {
    List<Produit> findByNomContainingIgnoreCase(String nom);

    // Recherche par stock faible
    List<Produit> findByQuantiteLessThan(Integer quantite);
}