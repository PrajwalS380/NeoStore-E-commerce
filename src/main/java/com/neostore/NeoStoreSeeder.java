// src/main/java/com/neostore/NeoStoreSeeder.java
package com.neostore;

import com.neostore.entity.Category;
import com.neostore.entity.Product;
import com.neostore.repo.CategoryRepository;
import com.neostore.repo.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.IntStream;

@Configuration
public class NeoStoreSeeder {

  @Bean
  CommandLineRunner seed(CategoryRepository catRepo, ProductRepository prodRepo) {
    return args -> {
      Map<String, String> cats = Map.of(
        "MEN", "Men",
        "WOMEN", "Women",
        "FOOTWEAR", "Footwear",
        "KIDS", "Kids",
        "LIFESTYLE", "Lifestyle",
        "GENZ", "GenZ"
      );

      // ensure categories exist
      for (var e : cats.entrySet()) {
        catRepo.findByCode(e.getKey()).orElseGet(() -> {
          Category c = new Category();
          c.setCode(e.getKey());
          c.setName(e.getValue());
          return catRepo.save(c);
        });
      }

      // seed men & women only if missing (keeps your existing data safe)
      if (prodRepo.count() == 0) {
        // MEN
        Category men = catRepo.findByCode("MEN").orElseThrow();
        IntStream.rangeClosed(1, 8).forEach(i -> {
          Product p = new Product();
          p.setName("Neo Men Tee " + i);
          p.setCategory(men);
          p.setPrice(BigDecimal.valueOf(999 + i * 50));
          p.setImageUrl("/img/placeholders/men" + i + ".jpg");
          p.setDescription("Performance tee with breathable fabric and subtle branding.");
          prodRepo.save(p);
        });

        // WOMEN
        Category women = catRepo.findByCode("WOMEN").orElseThrow();
        IntStream.rangeClosed(1, 8).forEach(i -> {
          Product p = new Product();
          p.setName("Neo Women Sneaker " + i);
          p.setCategory(women);
          p.setPrice(BigDecimal.valueOf(2999 + i * 100));
          p.setImageUrl("/img/placeholders/women" + i + ".jpg");
          p.setDescription("Comfort knit upper with responsive sole for daily wear.");
          prodRepo.save(p);
        });
      }

      // Add FOOTWEAR if not already present (idempotent check by counting existing items in category)
      Category footwear = catRepo.findByCode("FOOTWEAR").orElseThrow();
      long footwearCount = prodRepo.findByCategoryAndActiveTrue(footwear).size();
      if (footwearCount == 0) {
        IntStream.rangeClosed(1, 8).forEach(i -> {
          Product p = new Product();
          p.setName("Neo Runner " + i);
          p.setCategory(footwear);
          p.setPrice(BigDecimal.valueOf(2499 + i * 150));
          p.setImageUrl("/img/placeholders/footwear" + i + ".jpg");
          p.setDescription("Lightweight runner with responsive cushioning and grip.");
          prodRepo.save(p);
        });
      }

      // Add KIDS
      Category kids = catRepo.findByCode("KIDS").orElseThrow();
      long kidsCount = prodRepo.findByCategoryAndActiveTrue(kids).size();
      if (kidsCount == 0) {
        IntStream.rangeClosed(1, 8).forEach(i -> {
          Product p = new Product();
          p.setName("Neo Kids Tee " + i);
          p.setCategory(kids);
          p.setPrice(BigDecimal.valueOf(499 + i * 30));
          p.setImageUrl("/img/placeholders/kids" + i + ".jpg");
          p.setDescription("Soft cotton tee built for play and comfort.");
          prodRepo.save(p);
        });
      }

      // Add LIFESTYLE
      Category lifestyle = catRepo.findByCode("LIFESTYLE").orElseThrow();
      long lifeCount = prodRepo.findByCategoryAndActiveTrue(lifestyle).size();
      if (lifeCount == 0) {
        IntStream.rangeClosed(1, 8).forEach(i -> {
          Product p = new Product();
          p.setName("Neo Lifestyle Pack " + i);
          p.setCategory(lifestyle);
          p.setPrice(BigDecimal.valueOf(799 + i * 75));
          p.setImageUrl("/img/placeholders/lifestyle" + i + ".jpg");
          p.setDescription("Everyday essentials and accessories designed to elevate your routine.");
          prodRepo.save(p);
        });
      }

      // Add GENZ
      Category genz = catRepo.findByCode("GENZ").orElseThrow();
      long genzCount = prodRepo.findByCategoryAndActiveTrue(genz).size();
      if (genzCount == 0) {
        IntStream.rangeClosed(1, 8).forEach(i -> {
          Product p = new Product();
          p.setName("Neo GenZ Street " + i);
          p.setCategory(genz);
          p.setPrice(BigDecimal.valueOf(1199 + i * 90));
          p.setImageUrl("/img/placeholders/genz" + i + ".jpg");
          p.setDescription("Streetwear-inspired pieces with bold colors and prints.");
          prodRepo.save(p);
        });
      }
    };
  }
}
