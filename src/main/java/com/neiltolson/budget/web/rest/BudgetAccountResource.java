package com.neiltolson.budget.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.neiltolson.budget.domain.BudgetAccount;
import com.neiltolson.budget.repository.BudgetAccountRepository;
import com.neiltolson.budget.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing BudgetAccount.
 */
@RestController
@RequestMapping("/api")
public class BudgetAccountResource {

    private final Logger log = LoggerFactory.getLogger(BudgetAccountResource.class);
        
    @Inject
    private BudgetAccountRepository budgetAccountRepository;
    
    /**
     * POST  /budgetAccounts -> Create a new budgetAccount.
     */
    @RequestMapping(value = "/budgetAccounts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BudgetAccount> createBudgetAccount(@RequestBody BudgetAccount budgetAccount) throws URISyntaxException {
        log.debug("REST request to save BudgetAccount : {}", budgetAccount);
        if (budgetAccount.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("budgetAccount", "idexists", "A new budgetAccount cannot already have an ID")).body(null);
        }
        BudgetAccount result = budgetAccountRepository.save(budgetAccount);
        return ResponseEntity.created(new URI("/api/budgetAccounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("budgetAccount", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /budgetAccounts -> Updates an existing budgetAccount.
     */
    @RequestMapping(value = "/budgetAccounts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BudgetAccount> updateBudgetAccount(@RequestBody BudgetAccount budgetAccount) throws URISyntaxException {
        log.debug("REST request to update BudgetAccount : {}", budgetAccount);
        if (budgetAccount.getId() == null) {
            return createBudgetAccount(budgetAccount);
        }
        BudgetAccount result = budgetAccountRepository.save(budgetAccount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("budgetAccount", budgetAccount.getId().toString()))
            .body(result);
    }

    /**
     * GET  /budgetAccounts -> get all the budgetAccounts.
     */
    @RequestMapping(value = "/budgetAccounts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BudgetAccount> getAllBudgetAccounts() {
        log.debug("REST request to get all BudgetAccounts");
        return budgetAccountRepository.findAll();
            }

    /**
     * GET  /budgetAccounts/:id -> get the "id" budgetAccount.
     */
    @RequestMapping(value = "/budgetAccounts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BudgetAccount> getBudgetAccount(@PathVariable Long id) {
        log.debug("REST request to get BudgetAccount : {}", id);
        BudgetAccount budgetAccount = budgetAccountRepository.findOne(id);
        return Optional.ofNullable(budgetAccount)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /budgetAccounts/:id -> delete the "id" budgetAccount.
     */
    @RequestMapping(value = "/budgetAccounts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBudgetAccount(@PathVariable Long id) {
        log.debug("REST request to delete BudgetAccount : {}", id);
        budgetAccountRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("budgetAccount", id.toString())).build();
    }
}
