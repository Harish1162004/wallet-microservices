package com.wallet.user.service;

import com.wallet.user.dto.AuthResponse;
import com.wallet.user.dto.LoginRequest;
import com.wallet.user.dto.RegisterRequest;
import com.wallet.user.entity.User;
import com.wallet.user.repository.UserRepository;
import com.wallet.user.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	public AuthResponse register(RegisterRequest req) {
		if (userRepository.existsByEmail(req.getEmail().toLowerCase().trim())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
		}
		User u = new User();
		u.setEmail(req.getEmail().toLowerCase().trim());
		u.setPasswordHash(passwordEncoder.encode(req.getPassword()));
		u.setFullName(req.getFullName().trim());
		userRepository.save(u);
		String token = jwtService.generateToken(u.getId(), u.getEmail());
		return new AuthResponse(token, u.getId(), u.getEmail(), u.getFullName());
	}

	public AuthResponse login(LoginRequest req) {
		User u = userRepository.findByEmail(req.getEmail().toLowerCase().trim())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
		if (!passwordEncoder.matches(req.getPassword(), u.getPasswordHash())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
		}
		String token = jwtService.generateToken(u.getId(), u.getEmail());
		return new AuthResponse(token, u.getId(), u.getEmail(), u.getFullName());
	}
}
