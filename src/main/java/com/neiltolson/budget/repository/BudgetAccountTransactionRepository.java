package com.neiltolson.budget.repository;

import com.neiltolson.budget.domain.BudgetAccountTransaction;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BudgetAccountTransaction entity.
 */
public interface BudgetAccountTransactionRepository extends JpaRepository<BudgetAccountTransaction,Long> {

}
