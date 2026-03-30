package com.wallet.expense.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public class BudgetRequest {

	@NotBlank
	@Pattern(regexp = "\\d{4}-\\d{2}", message = "periodYm must be YYYY-MM")
	private String periodYm;

	private Long categoryId;

	@NotNull
	@DecimalMin(value = "0.01", inclusive = true)
	private BigDecimal limitAmount;

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
