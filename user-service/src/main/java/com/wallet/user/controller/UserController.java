package com.wallet.user.controller;

import com.wallet.user.dto.UserResponse;
import com.wallet.user.entity.User;
import com.wallet.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserRepository userRepository;

	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping("/me")
	public UserResponse me(Authentication authentication) {
		Long id = (Long) authentication.getPrincipal();
		User u = userRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return new UserResponse(u.getId(), u.getEmail(), u.getFullName(), u.getCreatedAt());
	}

	@GetMapping("/{id}")
	public UserResponse byId(@PathVariable Long id, Authentication authentication) {
		Long me = (Long) authentication.getPrincipal();
		if (!me.equals(id)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		User u = userRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return new UserResponse(u.getId(), u.getEmail(), u.getFullName(), u.getCreatedAt());
	}
}
