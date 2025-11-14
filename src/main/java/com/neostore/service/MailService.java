// src/main/java/com/neostore/service/MailService.java
package com.neostore.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class MailService {

  private final JavaMailSender mailSender;

  @Value("${neostore.mail.from}")
  private String from;
  @Value("${neostore.mail.verifyBaseUrl}")
  private String verifyBaseUrl;

  public MailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void sendVerificationMail(String to, String token) {
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setFrom(from);
    msg.setTo(to);
    msg.setSubject("Verify your NeoStore account");
    msg.setText("Welcome to NeoStore!\n\nPlease verify your account:\n" + verifyBaseUrl + token + "\n\nThis link expires in 24 hours.");
    mailSender.send(msg);
  }

  public void sendOrderConfirmation(String to, String orderId, BigDecimal total, String itemsSummary) {
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setFrom(from);
    msg.setTo(to);
    msg.setSubject("NeoStore Order #" + orderId + " Confirmation");
    msg.setText("Thanks for your purchase!\n\nOrder ID: " + orderId +
      "\nTotal: ₹" + total +
      "\n\nItems:\n" + itemsSummary +
      "\n\nWe'll notify you when it's shipped.");
    mailSender.send(msg);
  }
}
