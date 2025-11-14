// src/main/java/com/neostore/dto/PaymentRequest.java
package com.neostore.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
  private BigDecimal amount; // in rupees
  private String receipt;
  // getters/setters
}
