package com.neiltolson.budget.repository;

import com.neiltolson.budget.domain.BudgetAccount;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BudgetAccount entity.
 */
public interface BudgetAccountRepository extends JpaRepository<BudgetAccount,Long> {

}
