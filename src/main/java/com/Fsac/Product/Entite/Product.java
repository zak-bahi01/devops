package com.Fsac.Product.Entite;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id ;
    private String Name ;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Categorie_Product")
    private Categorie categorie ;
    @Lob
    @Column(columnDefinition= "MEDIUMBLOB")
    private String Image ;
    private double Price ;
}

