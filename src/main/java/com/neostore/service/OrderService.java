// src/main/java/com/neostore/service/OrderService.java
package com.neostore.service;

import com.neostore.entity.*;
import com.neostore.repo.OrderRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
  private final OrderRepository orderRepo;

  public OrderService(OrderRepository orderRepo) {
    this.orderRepo = orderRepo;
  }

  @Transactional
  public Order createFromCart(User user, List<CartItem> cartItems, String paymentId) {
    Order order = new Order();
    order.setUser(user);
    order.setCreatedAt(LocalDateTime.now());
    order.setStatus("PAID");
    order.setPaymentId(paymentId);

    BigDecimal total = BigDecimal.ZERO;
    List<OrderItem> items = cartItems.stream().map(ci -> {
      OrderItem oi = new OrderItem();
      oi.setProductId(ci.getProduct().getId());
      oi.setName(ci.getProduct().getName());
      oi.setImageUrl(ci.getProduct().getImageUrl());
      oi.setQuantity(ci.getQuantity());
      oi.setPrice(ci.getProduct().getPrice());
      oi.setLineTotal(ci.getLineTotal());
      return oi;
    }).toList();
    for (OrderItem oi : items) {
      total = total.add(oi.getLineTotal());
    }
    order.setItems(items);
    order.setTotal(total);
    return orderRepo.save(order);
  }
}
