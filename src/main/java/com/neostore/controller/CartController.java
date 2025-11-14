// src/main/java/com/neostore/controller/CartController.java
package com.neostore.controller;

import com.neostore.entity.User;
import com.neostore.entity.Product;
import com.neostore.service.CartService;
import com.neostore.service.ProductService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {
  private final CartService cartService;
  private final ProductService productService;

  public CartController(CartService cartService, ProductService productService) {
    this.cartService = cartService;
    this.productService = productService;
  }

  @PostMapping("/add/{productId}")
  public String add(@AuthenticationPrincipal User user,
                    @PathVariable Long productId,
                    @RequestParam(defaultValue = "1") int qty) {
    if (user == null) return "redirect:/auth/signin";
    Product p = productService.get(productId);
    cartService.addToCart(user, p, qty);
    return "redirect:/cart";
  }

  @GetMapping
  public String view(@AuthenticationPrincipal User user, Model model) {
    if (user == null) return "redirect:/auth/signin";
    var items = cartService.list(user);
    model.addAttribute("items", items);
    model.addAttribute("total", cartService.total(user));
    return "cart";
  }

  @PostMapping("/remove/{cartItemId}")
  public String remove(@AuthenticationPrincipal User user, @PathVariable Long cartItemId) {
    if (user == null) return "redirect:/auth/signin";
    cartService.removeByIdAndUser(cartItemId, user);
    return "redirect:/cart";
  }

  @PostMapping("/update/{cartItemId}")
  public String updateQuantity(@AuthenticationPrincipal User user,
                               @PathVariable Long cartItemId,
                               @RequestParam int quantity) {
    if (user == null) return "redirect:/auth/signin";
    if (quantity < 1) quantity = 1;
    cartService.updateQuantity(cartItemId, user, quantity);
    return "redirect:/cart";
  }
}
