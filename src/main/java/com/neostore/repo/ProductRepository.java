// src/main/java/com/neostore/repo/ProductRepository.java
package com.neostore.repo;
import com.neostore.entity.Product;
import com.neostore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
  List<Product> findByCategoryAndActiveTrue(Category category);
}
