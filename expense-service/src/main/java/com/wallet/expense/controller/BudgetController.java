package com.wallet.expense.controller;

import com.wallet.expense.dto.BudgetRequest;
import com.wallet.expense.dto.BudgetResponse;
import com.wallet.expense.dto.BudgetStatusResponse;
import com.wallet.expense.service.BudgetPlanService;
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
@RequestMapping("/api/budgets")
public class BudgetController {

	private final BudgetPlanService budgetPlanService;

	public BudgetController(BudgetPlanService budgetPlanService) {
		this.budgetPlanService = budgetPlanService;
	}

	@GetMapping
	public List<BudgetResponse> list(Authentication authentication) {
		return budgetPlanService.list((Long) authentication.getPrincipal());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BudgetResponse create(Authentication authentication, @Valid @RequestBody BudgetRequest req) {
		return budgetPlanService.create((Long) authentication.getPrincipal(), req);
	}

	@PutMapping("/{id}")
	public BudgetResponse update(
			Authentication authentication,
			@PathVariable Long id,
			@Valid @RequestBody BudgetRequest req) {
		return budgetPlanService.update((Long) authentication.getPrincipal(), id, req);
	}

	@GetMapping("/{id}/status")
	public BudgetStatusResponse status(Authentication authentication, @PathVariable Long id) {
		return budgetPlanService.status((Long) authentication.getPrincipal(), id);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(Authentication authentication, @PathVariable Long id) {
		budgetPlanService.delete((Long) authentication.getPrincipal(), id);
	}
}
