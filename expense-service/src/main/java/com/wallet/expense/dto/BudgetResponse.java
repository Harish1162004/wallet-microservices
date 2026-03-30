package com.wallet.expense.dto;

import java.math.BigDecimal;

public class BudgetResponse {

	private Long id;
	private String periodYm;
	private Long categoryId;
	private BigDecimal limitAmount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
}
