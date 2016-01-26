package com.neiltolson.budget.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.neiltolson.budget.domain.BudgetIncome;
import com.neiltolson.budget.repository.BudgetIncomeRepository;
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
 * REST controller for managing BudgetIncome.
 */
@RestController
@RequestMapping("/api")
public class BudgetIncomeResource {

    private final Logger log = LoggerFactory.getLogger(BudgetIncomeResource.class);
        
    @Inject
    private BudgetIncomeRepository budgetIncomeRepository;
    
    /**
     * POST  /budgetIncomes -> Create a new budgetIncome.
     */
    @RequestMapping(value = "/budgetIncomes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BudgetIncome> createBudgetIncome(@RequestBody BudgetIncome budgetIncome) throws URISyntaxException {
        log.debug("REST request to save BudgetIncome : {}", budgetIncome);
        if (budgetIncome.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("budgetIncome", "idexists", "A new budgetIncome cannot already have an ID")).body(null);
        }
        BudgetIncome result = budgetIncomeRepository.save(budgetIncome);
        return ResponseEntity.created(new URI("/api/budgetIncomes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("budgetIncome", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /budgetIncomes -> Updates an existing budgetIncome.
     */
    @RequestMapping(value = "/budgetIncomes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BudgetIncome> updateBudgetIncome(@RequestBody BudgetIncome budgetIncome) throws URISyntaxException {
        log.debug("REST request to update BudgetIncome : {}", budgetIncome);
        if (budgetIncome.getId() == null) {
            return createBudgetIncome(budgetIncome);
        }
        BudgetIncome result = budgetIncomeRepository.save(budgetIncome);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("budgetIncome", budgetIncome.getId().toString()))
            .body(result);
    }

    /**
     * GET  /budgetIncomes -> get all the budgetIncomes.
     */
    @RequestMapping(value = "/budgetIncomes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BudgetIncome> getAllBudgetIncomes() {
        log.debug("REST request to get all BudgetIncomes");
        return budgetIncomeRepository.findAll();
            }

    /**
     * GET  /budgetIncomes/:id -> get the "id" budgetIncome.
     */
    @RequestMapping(value = "/budgetIncomes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BudgetIncome> getBudgetIncome(@PathVariable Long id) {
        log.debug("REST request to get BudgetIncome : {}", id);
        BudgetIncome budgetIncome = budgetIncomeRepository.findOne(id);
        return Optional.ofNullable(budgetIncome)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /budgetIncomes/:id -> delete the "id" budgetIncome.
     */
    @RequestMapping(value = "/budgetIncomes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBudgetIncome(@PathVariable Long id) {
        log.debug("REST request to delete BudgetIncome : {}", id);
        budgetIncomeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("budgetIncome", id.toString())).build();
    }
}
