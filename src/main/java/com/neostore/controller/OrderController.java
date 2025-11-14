// src/main/java/com/neostore/controller/OrderController.java
package com.neostore.controller;

import com.neostore.entity.Order;
import com.neostore.entity.CartItem;
import com.neostore.entity.User;
import com.neostore.service.CartService;
import com.neostore.service.OrderService;
import com.neostore.service.MailService;
import com.neostore.repo.OrderRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/order")
public class OrderController {

  private final OrderService orderService;
  private final CartService cartService;
  private final MailService mailService;
  private final OrderRepository orderRepository;

  public OrderController(OrderService orderService,
                         CartService cartService,
                         MailService mailService,
                         OrderRepository orderRepository) {
    this.orderService = orderService;
    this.cartService = cartService;
    this.mailService = mailService;
    this.orderRepository = orderRepository;
  }

  /**
   * List orders for the authenticated user
   */
  @GetMapping
  public String listOrders(@AuthenticationPrincipal User user, Model model) {
    if (user == null) {
      return "redirect:/auth/signin";
    }
    List<Order> orders = orderRepository.findByUser(user);
    model.addAttribute("orders", orders);
    return "orders"; // create src/main/resources/templates/orders.html to show order list
  }

  /**
   * View single order details
   */
  @GetMapping("/{id}")
  public String viewOrder(@AuthenticationPrincipal User user, @PathVariable Long id, Model model) {
    if (user == null) {
      return "redirect:/auth/signin";
    }
    Order order = orderRepository.findById(id).orElse(null);
    if (order == null || !order.getUser().getId().equals(user.getId())) {
      return "redirect:/order";
    }
    model.addAttribute("order", order);
    return "order-detail"; // create src/main/resources/templates/order-detail.html to show full order
  }

  /**
   * Create an order from the current user's cart (server-side flow).
   * This endpoint can be used for non-Razorpay flows or to create a server-side order
   * after payment verification. It will persist the order, clear the cart and send a confirmation email.
   */
  @PostMapping("/create-from-cart")
  public String createOrderFromCart(@AuthenticationPrincipal User user, Model model) {
    if (user == null) {
      return "redirect:/auth/signin";
    }

    List<CartItem> cartItems = cartService.list(user);
    if (cartItems.isEmpty()) {
      model.addAttribute("message", "Cart is empty");
      return "cart";
    }

    // Create order (no payment id here because this is the server-side creation endpoint)
    Order order = orderService.createFromCart(user, cartItems, null);

    // Prepare items summary for email
    String itemsSummary = cartItems.stream()
      .map(ci -> ci.getProduct().getName() + " x " + ci.getQuantity() + " = ₹" + ci.getLineTotal())
      .collect(Collectors.joining("\n"));

    // Send order confirmation email
    try {
      mailService.sendOrderConfirmation(user.getEmail(), String.valueOf(order.getId()), order.getTotal(), itemsSummary);
    } catch (Exception ex) {
      // log mail failure in real app; for now, continue
    }

    // Clear cart
    cartService.clear(user);

    model.addAttribute("order", order);
    return "order-success";
  }
}
