// src/main/java/com/neostore/entity/OrderItem.java
package com.neostore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class OrderItem {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long productId;
  private String name;
  private String imageUrl;
  private int quantity;
  private BigDecimal price;     // unit price
  private BigDecimal lineTotal; // price * quantity
  // getters/setters
}
