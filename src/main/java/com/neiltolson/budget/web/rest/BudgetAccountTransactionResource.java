package com.neiltolson.budget.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.neiltolson.budget.domain.BudgetAccountTransaction;
import com.neiltolson.budget.repository.BudgetAccountTransactionRepository;
import com.neiltolson.budget.web.rest.util.HeaderUtil;
import com.neiltolson.budget.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing BudgetAccountTransaction.
 */
@RestController
@RequestMapping("/api")
public class BudgetAccountTransactionResource {

    private final Logger log = LoggerFactory.getLogger(BudgetAccountTransactionResource.class);
        
    @Inject
    private BudgetAccountTransactionRepository budgetAccountTransactionRepository;
    
    /**
     * POST  /budgetAccountTransactions -> Create a new budgetAccountTransaction.
     */
    @RequestMapping(value = "/budgetAccountTransactions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BudgetAccountTransaction> createBudgetAccountTransaction(@RequestBody BudgetAccountTransaction budgetAccountTransaction) throws URISyntaxException {
        log.debug("REST request to save BudgetAccountTransaction : {}", budgetAccountTransaction);
        if (budgetAccountTransaction.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("budgetAccountTransaction", "idexists", "A new budgetAccountTransaction cannot already have an ID")).body(null);
        }
        BudgetAccountTransaction result = budgetAccountTransactionRepository.save(budgetAccountTransaction);
        return ResponseEntity.created(new URI("/api/budgetAccountTransactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("budgetAccountTransaction", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /budgetAccountTransactions -> Updates an existing budgetAccountTransaction.
     */
    @RequestMapping(value = "/budgetAccountTransactions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BudgetAccountTransaction> updateBudgetAccountTransaction(@RequestBody BudgetAccountTransaction budgetAccountTransaction) throws URISyntaxException {
        log.debug("REST request to update BudgetAccountTransaction : {}", budgetAccountTransaction);
        if (budgetAccountTransaction.getId() == null) {
            return createBudgetAccountTransaction(budgetAccountTransaction);
        }
        BudgetAccountTransaction result = budgetAccountTransactionRepository.save(budgetAccountTransaction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("budgetAccountTransaction", budgetAccountTransaction.getId().toString()))
            .body(result);
    }

    /**
     * GET  /budgetAccountTransactions -> get all the budgetAccountTransactions.
     */
    @RequestMapping(value = "/budgetAccountTransactions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<BudgetAccountTransaction>> getAllBudgetAccountTransactions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of BudgetAccountTransactions");
        Page<BudgetAccountTransaction> page = budgetAccountTransactionRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/budgetAccountTransactions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /budgetAccountTransactions/:id -> get the "id" budgetAccountTransaction.
     */
    @RequestMapping(value = "/budgetAccountTransactions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BudgetAccountTransaction> getBudgetAccountTransaction(@PathVariable Long id) {
        log.debug("REST request to get BudgetAccountTransaction : {}", id);
        BudgetAccountTransaction budgetAccountTransaction = budgetAccountTransactionRepository.findOne(id);
        return Optional.ofNullable(budgetAccountTransaction)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /budgetAccountTransactions/:id -> delete the "id" budgetAccountTransaction.
     */
    @RequestMapping(value = "/budgetAccountTransactions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBudgetAccountTransaction(@PathVariable Long id) {
        log.debug("REST request to delete BudgetAccountTransaction : {}", id);
        budgetAccountTransactionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("budgetAccountTransaction", id.toString())).build();
    }
}
