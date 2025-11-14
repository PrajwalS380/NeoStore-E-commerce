// src/main/java/com/neostore/entity/Category.java
package com.neostore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Category {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String code; // MEN, WOMEN, FOOTWEAR, KIDS, LIFESTYLE, GENZ

  private String name;

  // getters/setters
}
