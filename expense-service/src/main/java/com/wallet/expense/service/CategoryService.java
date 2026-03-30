package com.wallet.expense.service;

import com.wallet.expense.dto.CategoryRequest;
import com.wallet.expense.dto.CategoryResponse;
import com.wallet.expense.entity.Category;
import com.wallet.expense.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CategoryService {

	private final CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public List<CategoryResponse> list(Long userId) {
		return categoryRepository.findByUserIdOrderByNameAsc(userId).stream()
				.map(this::toResponse)
				.toList();
	}

	@Transactional
	public CategoryResponse create(Long userId, CategoryRequest req) {
		Category c = new Category();
		c.setUserId(userId);
		c.setName(req.getName().trim());
		c.setColorHex(req.getColorHex());
		categoryRepository.save(c);
		return toResponse(c);
	}

	@Transactional
	public CategoryResponse update(Long userId, Long id, CategoryRequest req) {
		Category c = categoryRepository.findById(id)
				.filter(x -> x.getUserId().equals(userId))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		c.setName(req.getName().trim());
		c.setColorHex(req.getColorHex());
		return toResponse(c);
	}

	@Transactional
	public void delete(Long userId, Long id) {
		Category c = categoryRepository.findById(id)
				.filter(x -> x.getUserId().equals(userId))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		categoryRepository.delete(c);
	}

	public Category getOwned(Long userId, Long categoryId) {
		return categoryRepository.findById(categoryId)
				.filter(c -> c.getUserId().equals(userId))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
	}

	private CategoryResponse toResponse(Category c) {
		return new CategoryResponse(c.getId(), c.getName(), c.getColorHex());
	}
}
