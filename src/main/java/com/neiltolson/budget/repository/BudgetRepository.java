package com.neiltolson.budget.repository;

import com.neiltolson.budget.domain.Budget;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Budget entity.
 */
public interface BudgetRepository extends JpaRepository<Budget,Long> {

}
