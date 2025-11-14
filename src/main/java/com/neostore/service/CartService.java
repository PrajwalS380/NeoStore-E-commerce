// src/main/java/com/neostore/service/CartService.java
package com.neostore.service;

import com.neostore.entity.CartItem;
import com.neostore.entity.Product;
import com.neostore.entity.User;
import com.neostore.repo.CartItemRepository;
import com.neostore.repo.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

  private final CartItemRepository cartItemRepository;
  private final ProductRepository productRepository;

  public CartService(CartItemRepository cartItemRepository, ProductRepository productRepository) {
    this.cartItemRepository = cartItemRepository;
    this.productRepository = productRepository;
  }

  @Transactional
  public void addToCart(User user, Product product, int quantity) {
    if (user == null || product == null || quantity <= 0) return;

    Optional<CartItem> existing = cartItemRepository.findByUserAndProduct(user, product);
    if (existing.isPresent()) {
      CartItem ci = existing.get();
      ci.setQuantity(ci.getQuantity() + quantity);
      ci.setLineTotal(product.getPrice().multiply(new BigDecimal(ci.getQuantity())));
      ci.setUpdatedAt(LocalDateTime.now());
      cartItemRepository.save(ci);
    } else {
      CartItem ci = new CartItem();
      ci.setUser(user);
      ci.setProduct(product);
      ci.setQuantity(quantity);
      ci.setLineTotal(product.getPrice().multiply(new BigDecimal(quantity)));
      ci.setCreatedAt(LocalDateTime.now());
      ci.setUpdatedAt(LocalDateTime.now());
      cartItemRepository.save(ci);
    }
  }

  @Transactional(readOnly = true)
  public List<CartItem> list(User user) {
    if (user == null) return Collections.emptyList();
    return cartItemRepository.findByUser(user);
  }

  @Transactional(readOnly = true)
  public BigDecimal total(User user) {
    if (user == null) return BigDecimal.ZERO;
    return cartItemRepository.findByUser(user)
        .stream()
        .map(ci -> ci.getLineTotal() == null ? BigDecimal.ZERO : ci.getLineTotal())
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  @Transactional
  public void removeByIdAndUser(Long cartItemId, User user) {
    if (user == null || cartItemId == null) return;
    cartItemRepository.findById(cartItemId).ifPresent(ci -> {
      if (ci.getUser() != null && ci.getUser().getId().equals(user.getId())) {
        cartItemRepository.delete(ci);
      }
    });
  }

  @Transactional
  public void updateQuantity(Long cartItemId, User user, int quantity) {
    if (user == null || cartItemId == null || quantity < 1) return;
    cartItemRepository.findById(cartItemId).ifPresent(ci -> {
      if (ci.getUser() != null && ci.getUser().getId().equals(user.getId())) {
        ci.setQuantity(quantity);
        ci.setLineTotal(ci.getProduct().getPrice().multiply(new BigDecimal(quantity)));
        ci.setUpdatedAt(LocalDateTime.now());
        cartItemRepository.save(ci);
      }
    });
  }

  /**
   * Clear all cart items for the given user.
   * Uses repository bulk delete if available, otherwise deletes found items.
   */
  @Transactional
  public void clear(User user) {
    if (user == null) return;
    try {
      // preferred: single JPQL/SQL delete (fast)
      cartItemRepository.deleteByUser(user);
    } catch (Exception e) {
      // fallback: fetch and delete (safe)
      List<CartItem> items = cartItemRepository.findByUser(user);
      if (!items.isEmpty()) {
        cartItemRepository.deleteAll(items);
      }
    }
  }
}
