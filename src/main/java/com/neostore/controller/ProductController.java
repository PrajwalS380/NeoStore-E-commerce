// src/main/java/com/neostore/controller/ProductController.java
package com.neostore.controller;

import com.neostore.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/category")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping("/{code}")
  public String list(@PathVariable String code, Model model) {
    model.addAttribute("code", code);
    model.addAttribute("products", productService.listByCategoryCode(code.toUpperCase()));
    return "category";
  }
}
