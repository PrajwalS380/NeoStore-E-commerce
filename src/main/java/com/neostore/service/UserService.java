// src/main/java/com/neostore/service/UserService.java
package com.neostore.service;

import com.neostore.entity.User;
import com.neostore.entity.VerificationToken;
import com.neostore.repo.UserRepository;
import com.neostore.repo.VerificationTokenRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

  private final UserRepository userRepo;
  private final VerificationTokenRepository tokenRepo;
  private final PasswordEncoder encoder;
  private final MailService mailService;

  public UserService(UserRepository userRepo, VerificationTokenRepository tokenRepo,
                     PasswordEncoder encoder, MailService mailService) {
    this.userRepo = userRepo;
    this.tokenRepo = tokenRepo;
    this.encoder = encoder;
    this.mailService = mailService;
  }

  public User register(String email, String password, String name) {
    User user = new User();
    user.setEmail(email);
    user.setPassword(encoder.encode(password));
    user.setName(name);
    user.setEnabled(false); // verify first
    userRepo.save(user);

    String token = UUID.randomUUID().toString();
    VerificationToken vt = new VerificationToken();
    vt.setToken(token);
    vt.setUser(user);
    vt.setExpiry(LocalDateTime.now().plusHours(24));
    tokenRepo.save(vt);

    mailService.sendVerificationMail(email, token);
    return user;
  }

  public boolean verify(String token) {
    VerificationToken vt = tokenRepo.findByToken(token).orElse(null);
    if (vt == null || vt.getExpiry().isBefore(LocalDateTime.now())) return false;
    User u = vt.getUser();
    u.setEnabled(true);
    userRepo.save(u);
    tokenRepo.delete(vt);
    return true;
  }

  @Override
  public User loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
}
