package com.neiltolson.budget.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.neiltolson.budget.domain.BudgetLineItem;
import com.neiltolson.budget.repository.BudgetLineItemRepository;
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
 * REST controller for managing BudgetLineItem.
 */
@RestController
@RequestMapping("/api")
public class BudgetLineItemResource {

    private final Logger log = LoggerFactory.getLogger(BudgetLineItemResource.class);
        
    @Inject
    private BudgetLineItemRepository budgetLineItemRepository;
    
    /**
     * POST  /budgetLineItems -> Create a new budgetLineItem.
     */
    @RequestMapping(value = "/budgetLineItems",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BudgetLineItem> createBudgetLineItem(@RequestBody BudgetLineItem budgetLineItem) throws URISyntaxException {
        log.debug("REST request to save BudgetLineItem : {}", budgetLineItem);
        if (budgetLineItem.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("budgetLineItem", "idexists", "A new budgetLineItem cannot already have an ID")).body(null);
        }
        BudgetLineItem result = budgetLineItemRepository.save(budgetLineItem);
        return ResponseEntity.created(new URI("/api/budgetLineItems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("budgetLineItem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /budgetLineItems -> Updates an existing budgetLineItem.
     */
    @RequestMapping(value = "/budgetLineItems",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BudgetLineItem> updateBudgetLineItem(@RequestBody BudgetLineItem budgetLineItem) throws URISyntaxException {
        log.debug("REST request to update BudgetLineItem : {}", budgetLineItem);
        if (budgetLineItem.getId() == null) {
            return createBudgetLineItem(budgetLineItem);
        }
        BudgetLineItem result = budgetLineItemRepository.save(budgetLineItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("budgetLineItem", budgetLineItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /budgetLineItems -> get all the budgetLineItems.
     */
    @RequestMapping(value = "/budgetLineItems",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BudgetLineItem> getAllBudgetLineItems() {
        log.debug("REST request to get all BudgetLineItems");
        return budgetLineItemRepository.findAll();
            }

    /**
     * GET  /budgetLineItems/:id -> get the "id" budgetLineItem.
     */
    @RequestMapping(value = "/budgetLineItems/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BudgetLineItem> getBudgetLineItem(@PathVariable Long id) {
        log.debug("REST request to get BudgetLineItem : {}", id);
        BudgetLineItem budgetLineItem = budgetLineItemRepository.findOne(id);
        return Optional.ofNullable(budgetLineItem)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /budgetLineItems/:id -> delete the "id" budgetLineItem.
     */
    @RequestMapping(value = "/budgetLineItems/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBudgetLineItem(@PathVariable Long id) {
        log.debug("REST request to delete BudgetLineItem : {}", id);
        budgetLineItemRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("budgetLineItem", id.toString())).build();
    }
}
