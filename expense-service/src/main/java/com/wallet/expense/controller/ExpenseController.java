package com.wallet.expense.controller;

import com.wallet.expense.dto.ExpenseRequest;
import com.wallet.expense.dto.ExpenseResponse;
import com.wallet.expense.service.ExpenseRecordService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

	private final ExpenseRecordService expenseRecordService;

	public ExpenseController(ExpenseRecordService expenseRecordService) {
		this.expenseRecordService = expenseRecordService;
	}

	@GetMapping
	public List<ExpenseResponse> list(Authentication authentication, @RequestParam(required = false) Long categoryId) {
		return expenseRecordService.list((Long) authentication.getPrincipal(), categoryId);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ExpenseResponse create(Authentication authentication, @Valid @RequestBody ExpenseRequest req) {
		return expenseRecordService.create((Long) authentication.getPrincipal(), req);
	}

	@PutMapping("/{id}")
	public ExpenseResponse update(
			Authentication authentication,
			@PathVariable Long id,
			@Valid @RequestBody ExpenseRequest req) {
		return expenseRecordService.update((Long) authentication.getPrincipal(), id, req);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(Authentication authentication, @PathVariable Long id) {
		expenseRecordService.delete((Long) authentication.getPrincipal(), id);
	}
}
