package com.neiltolson.budget.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neiltolson.budget.domain.BudgetLineItem;

/**
 * Spring Data JPA repository for the BudgetLineItem entity.
 */
public interface BudgetLineItemRepository extends JpaRepository<BudgetLineItem, Long> {

	Long countByBudget_UserIdAndCategoryGroupAllIgnoreCase(Long userId, String categoryGroup);

	Long countByBudget_UserIdAndCategoryGroupAndCategoryNameAllIgnoreCase(Long userId, String categoryGroup,
			String categoryName);
}
