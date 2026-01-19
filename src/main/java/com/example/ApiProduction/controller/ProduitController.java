package com.example.ApiProduction.controller;


import com.example.ApiProduction.entity.Produit;
import com.example.ApiProduction.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/produits")
@CrossOrigin("*") // Pour les tests, à restreindre en production
public class ProduitController {

    @Autowired
    private ProduitRepository produitRepository;

    // CREATE - Ajouter un produit
    @PostMapping
    public ResponseEntity<Produit> createProduit(@RequestBody Produit produit) {
        try {
            Produit savedProduit = produitRepository.save(produit);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduit);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // READ ALL - Liste tous les produits
    @GetMapping
    public ResponseEntity<List<Produit>> getAllProduits() {
        List<Produit> produits = produitRepository.findAll();
        return ResponseEntity.ok(produits);
    }

    // READ ONE - Obtenir un produit par ID
    @GetMapping("/{id}")
    public ResponseEntity<Produit> getProduitById(@PathVariable Long id) {
        Optional<Produit> produit = produitRepository.findById(id);
        return produit.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // SEARCH - Rechercher par nom
    @GetMapping("/search")
    public ResponseEntity<List<Produit>> searchProduits(@RequestParam String nom) {
        List<Produit> produits = produitRepository.findByNomContainingIgnoreCase(nom);
        return ResponseEntity.ok(produits);
    }

    // STOCK FAIBLE - Produits avec peu de stock
    @GetMapping("/stock-faible")
    public ResponseEntity<List<Produit>> getStockFaible() {
        List<Produit> produits = produitRepository.findByQuantiteLessThan(10);
        return ResponseEntity.ok(produits);
    }

    // UPDATE - Mettre à jour un produit
    @PutMapping("/{id}")
    public ResponseEntity<Produit> updateProduit(@PathVariable Long id, @RequestBody Produit produitDetails) {
        Optional<Produit> produitOptional = produitRepository.findById(id);

        if (produitOptional.isPresent()) {
            Produit produit = produitOptional.get();
            produit.setNom(produitDetails.getNom());
            produit.setDescription(produitDetails.getDescription());
            produit.setPrix(produitDetails.getPrix());
            produit.setQuantite(produitDetails.getQuantite());

            Produit updatedProduit = produitRepository.save(produit);
            return ResponseEntity.ok(updatedProduit);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // DELETE - Supprimer un produit
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduit(@PathVariable Long id) {
        if (produitRepository.existsById(id)) {
            produitRepository.deleteById(id);
            return ResponseEntity.ok("Produit supprimé avec succès");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produit non trouvé");
    }

    // HEALTH CHECK - Vérifier que l'API fonctionne
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("✅ API Produits est en ligne! " + java.time.LocalDateTime.now());
    }

    // COUNT - Nombre total de produits
    @GetMapping("/count")
    public ResponseEntity<Long> countProduits() {
        long count = produitRepository.count();
        return ResponseEntity.ok(count);
    }
}