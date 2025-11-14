// src/main/java/com/neostore/repo/OrderRepository.java
package com.neostore.repo;
import com.neostore.entity.Order;
import com.neostore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findByUser(User user);
}
