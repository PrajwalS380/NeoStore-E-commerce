// src/main/java/com/neostore/repo/WishlistItemRepository.java
package com.neostore.repo;
import com.neostore.entity.WishlistItem;
import com.neostore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
  List<WishlistItem> findByUser(User user);
  Optional<WishlistItem> findByUserAndProductId(User user, Long productId);
}
