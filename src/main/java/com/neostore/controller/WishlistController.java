// src/main/java/com/neostore/controller/WishlistController.java
package com.neostore.controller;

import com.neostore.entity.User;
import com.neostore.service.CartService;
import com.neostore.service.ProductService;
import com.neostore.service.WishlistService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/wishlist")
public class WishlistController {

  private final WishlistService wishlistService;
  private final ProductService productService;
  private final CartService cartService;

  public WishlistController(WishlistService wishlistService, ProductService productService, CartService cartService) {
    this.wishlistService = wishlistService;
    this.productService = productService;
    this.cartService = cartService;
  }

  @PostMapping("/add/{productId}")
  public String add(@AuthenticationPrincipal User user, @PathVariable Long productId) {
    wishlistService.add(user, productService.get(productId));
    return "redirect:/wishlist";
  }

  @GetMapping
  public String view(@AuthenticationPrincipal User user, Model model) {
    model.addAttribute("items", wishlistService.list(user));
    return "wishlist";
  }

  @PostMapping("/move-to-cart/{productId}")
  public String moveToCart(@AuthenticationPrincipal User user, @PathVariable Long productId) {
    var product = productService.get(productId);
    cartService.addToCart(user, product, 1);
    wishlistService.remove(user, productId);
    return "redirect:/cart";
  }
}
