// src/main/java/com/neostore/entity/VerificationToken.java
package com.neostore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class VerificationToken {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String token;

  @OneToOne
  private User user;

  private LocalDateTime expiry;

  // getters/setters
}
