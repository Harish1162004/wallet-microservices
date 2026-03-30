package com.wallet.expense.repository;

import com.wallet.expense.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findByUserIdOrderByNameAsc(Long userId);
}
