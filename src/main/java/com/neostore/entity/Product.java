// src/main/java/com/neostore/entity/Product.java
package com.neostore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Product {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String brand = "NeoStore";
  private String imageUrl;
  private String description;

  @ManyToOne
  private Category category;

  private BigDecimal price;
  private boolean active = true;

  // getters/setters
}
