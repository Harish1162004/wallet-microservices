package com.wallet.expense.dto;

import java.math.BigDecimal;

public class BudgetStatusResponse {

	private Long budgetId;
	private String periodYm;
	private Long categoryId;
	private BigDecimal limitAmount;
	private BigDecimal spentAmount;
	private BigDecimal remaining;
	private boolean overBudget;

	public Long getBudgetId() {
		return budgetId;
	}

	public void setBudgetId(Long budgetId) {
		this.budgetId = budgetId;
	}

	public String getPeriodYm() {
		return periodYm;
	}

	public void setPeriodYm(String periodYm) {
		this.periodYm = periodYm;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public BigDecimal getLimitAmount() {
		return limitAmount;
	}

	public void setLimitAmount(BigDecimal limitAmount) {
		this.limitAmount = limitAmount;
	}

	public BigDecimal getSpentAmount() {
		return spentAmount;
	}

	public void setSpentAmount(BigDecimal spentAmount) {
		this.spentAmount = spentAmount;
	}

	public BigDecimal getRemaining() {
		return remaining;
	}

	public void setRemaining(BigDecimal remaining) {
		this.remaining = remaining;
	}

	public boolean isOverBudget() {
		return overBudget;
	}

	public void setOverBudget(boolean overBudget) {
		this.overBudget = overBudget;
	}
}
