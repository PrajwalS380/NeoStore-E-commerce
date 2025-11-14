// src/main/java/com/neostore/dto/SignupRequest.java
package com.neostore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
  @Email @NotBlank private String email;
  @Size(min=6) @NotBlank private String password;
  @NotBlank private String name;
  // getters/setters
}
