// src/main/java/com/neostore/repo/VerificationTokenRepository.java
package com.neostore.repo;
import com.neostore.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
  Optional<VerificationToken> findByToken(String token);
}
