package com.wallet.wallet.dto;

import com.wallet.wallet.entity.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public class TransactionResponse {

	private Long id;
	private TransactionType type;
	private BigDecimal amount;
	private BigDecimal balanceAfter;
	private String description;
	private Long counterpartyWalletId;
	private Instant createdAt;

	public TransactionResponse() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getBalanceAfter() {
		return balanceAfter;
	}

	public void setBalanceAfter(BigDecimal balanceAfter) {
		this.balanceAfter = balanceAfter;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getCounterpartyWalletId() {
		return counterpartyWalletId;
	}

	public void setCounterpartyWalletId(Long counterpartyWalletId) {
		this.counterpartyWalletId = counterpartyWalletId;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
}
