// src/main/java/com/neostore/config/SecurityConfig.java
package com.neostore.config;

import com.neostore.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/", "/auth/**", "/css/**", "/js/**", "/img/**").permitAll()
        .requestMatchers("/wishlist/**", "/cart/**", "/order/**", "/payment/**").authenticated()
        .anyRequest().permitAll()
      )
      .formLogin(form -> form
    		  .loginPage("/auth/signin")
    		  .usernameParameter("email")
    		  .passwordParameter("password")
    		  .defaultSuccessUrl("/", true)
    		  .permitAll()
    		)

      .logout(logout -> logout
        .logoutUrl("/auth/logout")
        .logoutSuccessUrl("/")
        .permitAll()
      )
      .rememberMe(Customizer.withDefaults());

    return http.build();
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * DaoAuthenticationProvider wired via method injection of UserService and password encoder.
   * This avoids constructor-level circular dependency.
   */
  @Bean
  public DaoAuthenticationProvider authProvider(UserService userService, BCryptPasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userService);
    provider.setPasswordEncoder(passwordEncoder);
    return provider;
  }

  /**
   * Expose AuthenticationManager from AuthenticationConfiguration so other beans/controllers can @Autowired it.
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }
}
