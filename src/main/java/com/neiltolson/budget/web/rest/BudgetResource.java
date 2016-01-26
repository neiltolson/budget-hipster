package com.neiltolson.budget.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.neiltolson.budget.domain.Budget;
import com.neiltolson.budget.repository.BudgetRepository;
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
 * REST controller for managing Budget.
 */
@RestController
@RequestMapping("/api")
public class BudgetResource {

    private final Logger log = LoggerFactory.getLogger(BudgetResource.class);
        
    @Inject
    private BudgetRepository budgetRepository;
    
    /**
     * POST  /budgets -> Create a new budget.
     */
    @RequestMapping(value = "/budgets",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Budget> createBudget(@RequestBody Budget budget) throws URISyntaxException {
        log.debug("REST request to save Budget : {}", budget);
        if (budget.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("budget", "idexists", "A new budget cannot already have an ID")).body(null);
        }
        Budget result = budgetRepository.save(budget);
        return ResponseEntity.created(new URI("/api/budgets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("budget", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /budgets -> Updates an existing budget.
     */
    @RequestMapping(value = "/budgets",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Budget> updateBudget(@RequestBody Budget budget) throws URISyntaxException {
        log.debug("REST request to update Budget : {}", budget);
        if (budget.getId() == null) {
            return createBudget(budget);
        }
        Budget result = budgetRepository.save(budget);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("budget", budget.getId().toString()))
            .body(result);
    }

    /**
     * GET  /budgets -> get all the budgets.
     */
    @RequestMapping(value = "/budgets",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Budget> getAllBudgets() {
        log.debug("REST request to get all Budgets");
        return budgetRepository.findAll();
            }

    /**
     * GET  /budgets/:id -> get the "id" budget.
     */
    @RequestMapping(value = "/budgets/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Budget> getBudget(@PathVariable Long id) {
        log.debug("REST request to get Budget : {}", id);
        Budget budget = budgetRepository.findOne(id);
        return Optional.ofNullable(budget)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /budgets/:id -> delete the "id" budget.
     */
    @RequestMapping(value = "/budgets/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
        log.debug("REST request to delete Budget : {}", id);
        budgetRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("budget", id.toString())).build();
    }
}
