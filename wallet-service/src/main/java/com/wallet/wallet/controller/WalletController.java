package com.wallet.wallet.controller;

import com.wallet.wallet.dto.FundRequest;
import com.wallet.wallet.dto.TransactionResponse;
import com.wallet.wallet.dto.TransferRequest;
import com.wallet.wallet.dto.WalletResponse;
import com.wallet.wallet.service.WalletOperationsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

	private final WalletOperationsService walletService;

	public WalletController(WalletOperationsService walletService) {
		this.walletService = walletService;
	}

	@PostMapping("/bootstrap")
	@ResponseStatus(HttpStatus.CREATED)
	public WalletResponse bootstrap(Authentication authentication) {
		Long userId = (Long) authentication.getPrincipal();
		return walletService.bootstrap(userId);
	}

	@GetMapping("/me")
	public WalletResponse me(Authentication authentication) {
		Long userId = (Long) authentication.getPrincipal();
		return walletService.getMine(userId);
	}

	@PostMapping("/me/fund")
	public WalletResponse fund(Authentication authentication, @Valid @RequestBody FundRequest req) {
		Long userId = (Long) authentication.getPrincipal();
		return walletService.fund(userId, req);
	}

	@PostMapping("/me/transfer")
	public WalletResponse transfer(Authentication authentication, @Valid @RequestBody TransferRequest req) {
		Long userId = (Long) authentication.getPrincipal();
		return walletService.transfer(userId, req);
	}

	@GetMapping("/me/transactions")
	public List<TransactionResponse> transactions(Authentication authentication) {
		Long userId = (Long) authentication.getPrincipal();
		return walletService.transactions(userId);
	}
}
