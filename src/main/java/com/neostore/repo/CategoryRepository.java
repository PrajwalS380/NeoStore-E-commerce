// src/main/java/com/neostore/repo/CategoryRepository.java
package com.neostore.repo;
import com.neostore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  Optional<Category> findByCode(String code);
}
