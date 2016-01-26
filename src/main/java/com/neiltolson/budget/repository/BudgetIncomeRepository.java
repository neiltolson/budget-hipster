package com.neiltolson.budget.repository;

import com.neiltolson.budget.domain.BudgetIncome;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BudgetIncome entity.
 */
public interface BudgetIncomeRepository extends JpaRepository<BudgetIncome,Long> {

}
