package com.wallet.expense.service;

import com.wallet.expense.dto.BudgetRequest;
import com.wallet.expense.dto.BudgetResponse;
import com.wallet.expense.dto.BudgetStatusResponse;
import com.wallet.expense.entity.Budget;
import com.wallet.expense.repository.BudgetRepository;
import com.wallet.expense.repository.ExpenseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BudgetPlanService {

	private final BudgetRepository budgetRepository;
	private final ExpenseRepository expenseRepository;
	private final CategoryService categoryService;

	public BudgetPlanService(
			BudgetRepository budgetRepository,
			ExpenseRepository expenseRepository,
			CategoryService categoryService) {
		this.budgetRepository = budgetRepository;
		this.expenseRepository = expenseRepository;
		this.categoryService = categoryService;
	}

	public List<BudgetResponse> list(Long userId) {
		return budgetRepository.findByUserIdOrderByPeriodYmDesc(userId).stream()
				.map(this::toResponse)
				.toList();
	}

	@Transactional
	public BudgetResponse create(Long userId, BudgetRequest req) {
		if (req.getCategoryId() != null) {
			categoryService.getOwned(userId, req.getCategoryId());
		}
		Budget b = new Budget();
		b.setUserId(userId);
		b.setPeriodYm(req.getPeriodYm());
		b.setCategoryId(req.getCategoryId());
		b.setLimitAmount(req.getLimitAmount());
		budgetRepository.save(b);
		return toResponse(b);
	}

	@Transactional
	public BudgetResponse update(Long userId, Long id, BudgetRequest req) {
		Budget b = budgetRepository.findByIdAndUserId(id, userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		if (req.getCategoryId() != null) {
			categoryService.getOwned(userId, req.getCategoryId());
		}
		b.setPeriodYm(req.getPeriodYm());
		b.setCategoryId(req.getCategoryId());
		b.setLimitAmount(req.getLimitAmount());
		return toResponse(b);
	}

	@Transactional
	public void delete(Long userId, Long id) {
		Budget b = budgetRepository.findByIdAndUserId(id, userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		budgetRepository.delete(b);
	}

	public BudgetStatusResponse status(Long userId, Long budgetId) {
		Budget b = budgetRepository.findByIdAndUserId(budgetId, userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		BigDecimal spent;
		if (b.getCategoryId() != null) {
			spent = expenseRepository.sumByUserCategoryPeriod(userId, b.getCategoryId(), b.getPeriodYm());
		} else {
			spent = expenseRepository.sumByUserPeriod(userId, b.getPeriodYm());
		}
		if (spent == null) {
			spent = BigDecimal.ZERO;
		}
		BigDecimal remaining = b.getLimitAmount().subtract(spent);
		BudgetStatusResponse r = new BudgetStatusResponse();
		r.setBudgetId(b.getId());
		r.setPeriodYm(b.getPeriodYm());
		r.setCategoryId(b.getCategoryId());
		r.setLimitAmount(b.getLimitAmount());
		r.setSpentAmount(spent);
		r.setRemaining(remaining);
		r.setOverBudget(spent.compareTo(b.getLimitAmount()) > 0);
		return r;
	}

	private BudgetResponse toResponse(Budget b) {
		BudgetResponse r = new BudgetResponse();
		r.setId(b.getId());
		r.setPeriodYm(b.getPeriodYm());
		r.setCategoryId(b.getCategoryId());
		r.setLimitAmount(b.getLimitAmount());
		return r;
	}
}
