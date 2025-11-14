// src/main/java/com/neostore/service/PaymentService.java
package com.neostore.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class PaymentService {

  private final RazorpayClient client;

  @Value("${razorpay.currency}")
  private String currency;

  public PaymentService(RazorpayClient client) {
    this.client = client;
  }

  public Order createOrder(BigDecimal amountRupees, String receipt) throws Exception {
    // Razorpay expects paise
    int amountPaise = amountRupees.multiply(BigDecimal.valueOf(100)).intValue();
    JSONObject request = new JSONObject();
    request.put("amount", amountPaise);
    request.put("currency", currency);
    request.put("receipt", receipt);
    request.put("payment_capture", 1);
    return client.orders.create(request);
  }
}
