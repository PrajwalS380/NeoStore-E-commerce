// src/main/java/com/neostore/controller/HomeController.java
package com.neostore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
  @GetMapping("/")
  public String home(Model model) {
    model.addAttribute("bannerTitle", "Singles' Day");
    model.addAttribute("bannerSubtitle", "ENDS 11TH NOV");
    model.addAttribute("bannerCta", "EXTRA 25% OFF ON EVERYTHING*");
    model.addAttribute("bannerNote", "Discount auto-applied at checkout");
    return "home";
  }
}
