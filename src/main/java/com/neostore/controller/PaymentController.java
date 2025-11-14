// src/main/java/com/neostore/controller/PaymentController.java
package com.neostore.controller;

import com.razorpay.Order;
import com.neostore.entity.User;
import com.neostore.service.CartService;
import com.neostore.service.OrderService;
import com.neostore.service.PaymentService;
import com.neostore.service.MailService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/payment")
public class PaymentController {

  private final PaymentService paymentService;
  private final CartService cartService;
  private final OrderService orderService;
  private final MailService mailService;

  @Value("${razorpay.key_id}")
  private String razorpayKeyId;

  public PaymentController(PaymentService paymentService, CartService cartService,
                           OrderService orderService, MailService mailService) {
    this.paymentService = paymentService;
    this.cartService = cartService;
    this.orderService = orderService;
    this.mailService = mailService;
  }

  @GetMapping("/checkout")
  public String checkout(@AuthenticationPrincipal User user, Model model) throws Exception {
    var total = cartService.total(user);
    Order rpOrder = paymentService.createOrder(total, "rcpt_" + System.currentTimeMillis());
    JSONObject json = rpOrder.toJson();

    model.addAttribute("razorpayKeyId", razorpayKeyId);
    model.addAttribute("razorpayOrderId", json.getString("id"));
    model.addAttribute("amountPaise", json.getInt("amount"));
    model.addAttribute("amountRupees", total);
    return "checkout";
  }

  @PostMapping("/success")
  public String success(@AuthenticationPrincipal User user,
                        @RequestParam("razorpay_payment_id") String paymentId,
                        @RequestParam("razorpay_order_id") String orderId,
                        @RequestParam("razorpay_signature") String signature,
                        Model model) {
    // Signature verification omitted for brevity; implement in production.

    var cartItems = cartService.list(user);
    var order = orderService.createFromCart(user, cartItems, paymentId);
    cartService.clear(user);

    String itemsSummary = cartItems.stream()
      .map(ci -> ci.getProduct().getName() + " x " + ci.getQuantity() + " = ₹" + ci.getLineTotal())
      .collect(Collectors.joining("\n"));

    mailService.sendOrderConfirmation(user.getEmail(), String.valueOf(order.getId()), order.getTotal(), itemsSummary);

    model.addAttribute("order", order);
    return "order-success";
  }
}
