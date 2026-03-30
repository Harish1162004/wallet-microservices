package com.wallet.wallet.service;

import com.wallet.wallet.dto.FundRequest;
import com.wallet.wallet.dto.TransactionResponse;
import com.wallet.wallet.dto.TransferRequest;
import com.wallet.wallet.dto.WalletResponse;
import com.wallet.wallet.entity.TransactionType;
import com.wallet.wallet.entity.Wallet;
import com.wallet.wallet.entity.WalletTransaction;
import com.wallet.wallet.repository.WalletRepository;
import com.wallet.wallet.repository.WalletTransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletOperationsService {

	private final WalletRepository walletRepository;
	private final WalletTransactionRepository transactionRepository;

	public WalletOperationsService(
			WalletRepository walletRepository,
			WalletTransactionRepository transactionRepository) {
		this.walletRepository = walletRepository;
		this.transactionRepository = transactionRepository;
	}

	@Transactional
	public WalletResponse bootstrap(Long userId) {
		Wallet w = walletRepository.findByUserId(userId).orElseGet(() -> {
			Wallet n = new Wallet();
			n.setUserId(userId);
			return walletRepository.save(n);
		});
		return toWalletResponse(w);
	}

	public WalletResponse getMine(Long userId) {
		Wallet w = walletRepository.findByUserId(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found; call bootstrap first"));
		return toWalletResponse(w);
	}

	@Transactional
	public WalletResponse fund(Long userId, FundRequest req) {
		Wallet w = walletRepository.findForUpdateByUserId(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found; call bootstrap first"));
		BigDecimal amt = req.getAmount();
		w.setBalance(w.getBalance().add(amt));
		walletRepository.save(w);
		WalletTransaction tx = new WalletTransaction();
		tx.setWalletId(w.getId());
		tx.setType(TransactionType.FUND);
		tx.setAmount(amt);
		tx.setBalanceAfter(w.getBalance());
		tx.setDescription(req.getDescription() != null ? req.getDescription() : "Fund");
		transactionRepository.save(tx);
		return toWalletResponse(w);
	}

	@Transactional
	public WalletResponse transfer(Long fromUserId, TransferRequest req) {
		if (fromUserId.equals(req.getToUserId())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot transfer to self");
		}
		Wallet from = walletRepository.findForUpdateByUserId(fromUserId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found; call bootstrap first"));
		Wallet to = walletRepository.findForUpdateByUserId(req.getToUserId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipient wallet not found"));
		BigDecimal amt = req.getAmount();
		if (from.getBalance().compareTo(amt) < 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance");
		}
		from.setBalance(from.getBalance().subtract(amt));
		to.setBalance(to.getBalance().add(amt));
		walletRepository.save(from);
		walletRepository.save(to);

		String note = req.getNote() != null ? req.getNote() : "Transfer";

		WalletTransaction out = new WalletTransaction();
		out.setWalletId(from.getId());
		out.setType(TransactionType.TRANSFER_OUT);
		out.setAmount(amt);
		out.setBalanceAfter(from.getBalance());
		out.setCounterpartyWalletId(to.getId());
		out.setDescription("To user " + req.getToUserId() + ": " + note);
		transactionRepository.save(out);

		WalletTransaction in = new WalletTransaction();
		in.setWalletId(to.getId());
		in.setType(TransactionType.TRANSFER_IN);
		in.setAmount(amt);
		in.setBalanceAfter(to.getBalance());
		in.setCounterpartyWalletId(from.getId());
		in.setDescription("From user " + fromUserId + ": " + note);
		transactionRepository.save(in);

		return toWalletResponse(
				walletRepository.findById(from.getId()).orElseThrow());
	}

	public List<TransactionResponse> transactions(Long userId) {
		Wallet w = walletRepository.findByUserId(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found"));
		return transactionRepository.findByWalletIdOrderByCreatedAtDesc(w.getId()).stream()
				.map(this::toTxResponse)
				.toList();
	}

	private WalletResponse toWalletResponse(Wallet w) {
		return new WalletResponse(w.getId(), w.getUserId(), w.getBalance(), w.getCurrency());
	}

	private TransactionResponse toTxResponse(WalletTransaction t) {
		TransactionResponse r = new TransactionResponse();
		r.setId(t.getId());
		r.setType(t.getType());
		r.setAmount(t.getAmount());
		r.setBalanceAfter(t.getBalanceAfter());
		r.setDescription(t.getDescription());
		r.setCounterpartyWalletId(t.getCounterpartyWalletId());
		r.setCreatedAt(t.getCreatedAt());
		return r;
	}
}
