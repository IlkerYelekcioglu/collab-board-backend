package com.taskboard.collabboard.controller;

import com.taskboard.collabboard.dto.request.LoginRequest;
import com.taskboard.collabboard.dto.request.RegisterRequest;
import com.taskboard.collabboard.dto.response.AuthResponse;
import com.taskboard.collabboard.entity.User;
import com.taskboard.collabboard.repository.UserRepository;
import com.taskboard.collabboard.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,
      AuthenticationManager authenticationManager, JwtService jwtService,
      UserDetailsService userDetailsService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
  }

  @PostMapping("/register")
  public AuthResponse register(@RequestBody RegisterRequest request) {
    User user = new User();
    user.setUsername(request.username());
    user.setPassword(passwordEncoder.encode(request.password()));
    userRepository.save(user);

    UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());
    return new AuthResponse(jwtService.generateToken(userDetails));
  }

  @PostMapping("/login")
  public AuthResponse login(@RequestBody LoginRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.username(), request.password()));
    UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());
    return new AuthResponse(jwtService.generateToken(userDetails));
  }

  @GetMapping("/me")
  public String me(Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Yetkisiz erişim");
    }
    return "Merhaba " + authentication.getName();
  }
}
