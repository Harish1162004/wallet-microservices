package com.wallet.expense.repository;

import com.wallet.expense.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

	List<Expense> findByUserIdOrderByExpenseDateDescCreatedAtDesc(Long userId);

	List<Expense> findByUserIdAndCategoryIdOrderByExpenseDateDesc(Long userId, Long categoryId);

	@Query(value = "SELECT COALESCE(SUM(amount), 0) FROM expenses WHERE user_id = :userId AND category_id = :categoryId "
			+ "AND DATE_FORMAT(expense_date, '%Y-%m') = :periodYm", nativeQuery = true)
	BigDecimal sumByUserCategoryPeriod(@Param("userId") Long userId, @Param("categoryId") Long categoryId, @Param("periodYm") String periodYm);

	@Query(value = "SELECT COALESCE(SUM(amount), 0) FROM expenses WHERE user_id = :userId "
			+ "AND DATE_FORMAT(expense_date, '%Y-%m') = :periodYm", nativeQuery = true)
	BigDecimal sumByUserPeriod(@Param("userId") Long userId, @Param("periodYm") String periodYm);
}
