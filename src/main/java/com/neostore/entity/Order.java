// src/main/java/com/neostore/entity/Order.java
package com.neostore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="orders")
@Getter
@Setter
public class Order {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne private User user;

  @OneToMany(cascade = CascadeType.ALL)
  private List<OrderItem> items;

  private BigDecimal total;
  private String paymentId; // Razorpay payment id
  private String status;    // CREATED, PAID, FAILED
  private LocalDateTime createdAt;

  // getters/setters
}
