package com.wallet.expense.service;

import com.wallet.expense.dto.ExpenseRequest;
import com.wallet.expense.dto.ExpenseResponse;
import com.wallet.expense.entity.Expense;
import com.wallet.expense.repository.CategoryRepository;
import com.wallet.expense.repository.ExpenseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpenseRecordService {

	private final ExpenseRepository expenseRepository;
	private final CategoryRepository categoryRepository;
	private final CategoryService categoryService;

	public ExpenseRecordService(
			ExpenseRepository expenseRepository,
			CategoryRepository categoryRepository,
			CategoryService categoryService) {
		this.expenseRepository = expenseRepository;
		this.categoryRepository = categoryRepository;
		this.categoryService = categoryService;
	}

	public List<ExpenseResponse> list(Long userId, Long categoryId) {
		List<Expense> rows = categoryId == null
				? expenseRepository.findByUserIdOrderByExpenseDateDescCreatedAtDesc(userId)
				: expenseRepository.findByUserIdAndCategoryIdOrderByExpenseDateDesc(userId, categoryId);
		Map<Long, String> names = categoryRepository.findByUserIdOrderByNameAsc(userId).stream()
				.collect(Collectors.toMap(c -> c.getId(), c -> c.getName()));
		return rows.stream().map(e -> toResponse(e, names.get(e.getCategoryId()))).toList();
	}

	@Transactional
	public ExpenseResponse create(Long userId, ExpenseRequest req) {
		categoryService.getOwned(userId, req.getCategoryId());
		Expense e = new Expense();
		e.setUserId(userId);
		e.setCategoryId(req.getCategoryId());
		e.setAmount(req.getAmount());
		e.setNote(req.getNote());
		e.setExpenseDate(req.getExpenseDate());
		expenseRepository.save(e);
		var cat = categoryRepository.findById(req.getCategoryId()).orElseThrow();
		return toResponse(e, cat.getName());
	}

	@Transactional
	public ExpenseResponse update(Long userId, Long id, ExpenseRequest req) {
		Expense e = expenseRepository.findById(id)
				.filter(x -> x.getUserId().equals(userId))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		categoryService.getOwned(userId, req.getCategoryId());
		e.setCategoryId(req.getCategoryId());
		e.setAmount(req.getAmount());
		e.setNote(req.getNote());
		e.setExpenseDate(req.getExpenseDate());
		var cat = categoryRepository.findById(req.getCategoryId()).orElseThrow();
		return toResponse(e, cat.getName());
	}

	@Transactional
	public void delete(Long userId, Long id) {
		Expense e = expenseRepository.findById(id)
				.filter(x -> x.getUserId().equals(userId))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		expenseRepository.delete(e);
	}

	private ExpenseResponse toResponse(Expense e, String categoryName) {
		ExpenseResponse r = new ExpenseResponse();
		r.setId(e.getId());
		r.setCategoryId(e.getCategoryId());
		r.setCategoryName(categoryName);
		r.setAmount(e.getAmount());
		r.setNote(e.getNote());
		r.setExpenseDate(e.getExpenseDate());
		r.setCreatedAt(e.getCreatedAt());
		return r;
	}
}
