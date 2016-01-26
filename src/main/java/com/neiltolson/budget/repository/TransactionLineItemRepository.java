package com.neiltolson.budget.repository;

import com.neiltolson.budget.domain.TransactionLineItem;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TransactionLineItem entity.
 */
public interface TransactionLineItemRepository extends JpaRepository<TransactionLineItem,Long> {

}
