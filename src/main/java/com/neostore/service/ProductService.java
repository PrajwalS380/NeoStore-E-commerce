// src/main/java/com/neostore/service/ProductService.java
package com.neostore.service;

import com.neostore.entity.Category;
import com.neostore.entity.Product;
import com.neostore.repo.CategoryRepository;
import com.neostore.repo.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
  private final ProductRepository productRepo;
  private final CategoryRepository categoryRepo;

  public ProductService(ProductRepository productRepo, CategoryRepository categoryRepo) {
    this.productRepo = productRepo;
    this.categoryRepo = categoryRepo;
  }

  public List<Product> listByCategoryCode(String code) {
    Category c = categoryRepo.findByCode(code).orElseThrow();
    return productRepo.findByCategoryAndActiveTrue(c);
  }

  public Product get(Long id) { return productRepo.findById(id).orElseThrow(); }
}
