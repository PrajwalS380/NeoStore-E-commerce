// src/main/java/com/neostore/dto/OrderSummaryDto.java
package com.neostore.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSummaryDto {
  public static class Item {
    public Long productId;
    public String name;
    public String imageUrl;
    public int quantity;
    public BigDecimal price;
    public BigDecimal lineTotal;
  }
  private List<Item> items;
  private BigDecimal total;
  // getters/setters
}
