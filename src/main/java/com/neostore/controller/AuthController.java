// src/main/java/com/neostore/controller/AuthController.java
package com.neostore.controller;

import com.neostore.dto.SignupRequest;
import com.neostore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

  private final UserService userService;
  private final AuthenticationManager authManager;

  public AuthController(UserService userService, AuthenticationManager authManager) {
    this.userService = userService;
    this.authManager = authManager;
  }

  @GetMapping("/signin")
  public String signin() { return "auth/signin"; }

  @GetMapping("/signup")
  public String signup(Model model) {
    model.addAttribute("signupRequest", new SignupRequest());
    return "auth/signup";
  }

  @PostMapping("/signup")
  public String doSignup(@ModelAttribute @Valid SignupRequest req, Model model) {
    userService.register(req.getEmail(), req.getPassword(), req.getName());
    model.addAttribute("message", "Check your email for verification link.");
    return "auth/verify";
  }

  @GetMapping("/verify")
  public String verify(@RequestParam String token, Model model) {
    boolean ok = userService.verify(token);
    model.addAttribute("message", ok ? "Verification successful. Please sign in." : "Verification failed or expired.");
    return "auth/signin";
  }

  @PostMapping("/signin")
  public String doSignin(@RequestParam String email, @RequestParam String password, Model model) {
    Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    if (auth.isAuthenticated()) {
      return "redirect:/";
    }
    model.addAttribute("error", "Invalid credentials");
    return "auth/signin";
  }
}
