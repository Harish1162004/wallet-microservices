package com.wallet.wallet.dto;

import java.math.BigDecimal;

public class WalletResponse {

	private Long id;
	private Long userId;
	private BigDecimal balance;
	private String currency;

	public WalletResponse() {
	}

	public WalletResponse(Long id, Long userId, BigDecimal balance, String currency) {
		this.id = id;
		this.userId = userId;
		this.balance = balance;
		this.currency = currency;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
