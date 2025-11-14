// src/main/java/com/neostore/dto/LoginRequest.java
package com.neostore.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
  @NotBlank private String email;
  @NotBlank private String password;
  // getters/setters
}
