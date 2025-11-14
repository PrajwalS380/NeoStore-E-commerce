// src/main/java/com/neostore/service/WishlistService.java
package com.neostore.service;

import com.neostore.entity.Product;
import com.neostore.entity.User;
import com.neostore.entity.WishlistItem;
import com.neostore.repo.WishlistItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WishlistService {
  private final WishlistItemRepository wishlistRepo;

  public WishlistService(WishlistItemRepository wishlistRepo) {
    this.wishlistRepo = wishlistRepo;
  }

  public void add(User user, Product product) {
    wishlistRepo.findByUserAndProductId(user, product.getId())
      .orElseGet(() -> {
        WishlistItem wi = new WishlistItem();
        wi.setUser(user);
        wi.setProduct(product);
        return wishlistRepo.save(wi);
      });
  }

  public void remove(User user, Long productId) {
    wishlistRepo.findByUserAndProductId(user, productId).ifPresent(wishlistRepo::delete);
  }

  public List<WishlistItem> list(User user) { return wishlistRepo.findByUser(user); }
}
