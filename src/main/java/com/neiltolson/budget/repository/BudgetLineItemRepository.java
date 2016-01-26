package com.neiltolson.budget.repository;

import com.neiltolson.budget.domain.BudgetLineItem;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BudgetLineItem entity.
 */
public interface BudgetLineItemRepository extends JpaRepository<BudgetLineItem,Long> {

}
