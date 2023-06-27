package com.java.ProductRest.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PRODUCTS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    @Column(name = "productType")
    private String productType;
    private int quantity;
    private double price;
    @Column(name = "supplierName")

    private String supplierName;
    @Column(name = "supplierCode")

    private String supplierCode;
}