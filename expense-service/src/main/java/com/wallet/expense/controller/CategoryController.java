package com.wallet.expense.controller;

import com.wallet.expense.dto.CategoryRequest;
import com.wallet.expense.dto.CategoryResponse;
import com.wallet.expense.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	private final CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping
	public List<CategoryResponse> list(Authentication authentication) {
		return categoryService.list((Long) authentication.getPrincipal());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CategoryResponse create(Authentication authentication, @Valid @RequestBody CategoryRequest req) {
		return categoryService.create((Long) authentication.getPrincipal(), req);
	}

	@PutMapping("/{id}")
	public CategoryResponse update(
			Authentication authentication,
			@PathVariable Long id,
			@Valid @RequestBody CategoryRequest req) {
		return categoryService.update((Long) authentication.getPrincipal(), id, req);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(Authentication authentication, @PathVariable Long id) {
		categoryService.delete((Long) authentication.getPrincipal(), id);
	}
}
