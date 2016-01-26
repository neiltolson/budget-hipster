package com.neiltolson.budget.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.neiltolson.budget.domain.TransactionLineItem;
import com.neiltolson.budget.repository.TransactionLineItemRepository;
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
 * REST controller for managing TransactionLineItem.
 */
@RestController
@RequestMapping("/api")
public class TransactionLineItemResource {

    private final Logger log = LoggerFactory.getLogger(TransactionLineItemResource.class);
        
    @Inject
    private TransactionLineItemRepository transactionLineItemRepository;
    
    /**
     * POST  /transactionLineItems -> Create a new transactionLineItem.
     */
    @RequestMapping(value = "/transactionLineItems",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TransactionLineItem> createTransactionLineItem(@RequestBody TransactionLineItem transactionLineItem) throws URISyntaxException {
        log.debug("REST request to save TransactionLineItem : {}", transactionLineItem);
        if (transactionLineItem.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("transactionLineItem", "idexists", "A new transactionLineItem cannot already have an ID")).body(null);
        }
        TransactionLineItem result = transactionLineItemRepository.save(transactionLineItem);
        return ResponseEntity.created(new URI("/api/transactionLineItems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("transactionLineItem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /transactionLineItems -> Updates an existing transactionLineItem.
     */
    @RequestMapping(value = "/transactionLineItems",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TransactionLineItem> updateTransactionLineItem(@RequestBody TransactionLineItem transactionLineItem) throws URISyntaxException {
        log.debug("REST request to update TransactionLineItem : {}", transactionLineItem);
        if (transactionLineItem.getId() == null) {
            return createTransactionLineItem(transactionLineItem);
        }
        TransactionLineItem result = transactionLineItemRepository.save(transactionLineItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("transactionLineItem", transactionLineItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transactionLineItems -> get all the transactionLineItems.
     */
    @RequestMapping(value = "/transactionLineItems",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TransactionLineItem> getAllTransactionLineItems() {
        log.debug("REST request to get all TransactionLineItems");
        return transactionLineItemRepository.findAll();
            }

    /**
     * GET  /transactionLineItems/:id -> get the "id" transactionLineItem.
     */
    @RequestMapping(value = "/transactionLineItems/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TransactionLineItem> getTransactionLineItem(@PathVariable Long id) {
        log.debug("REST request to get TransactionLineItem : {}", id);
        TransactionLineItem transactionLineItem = transactionLineItemRepository.findOne(id);
        return Optional.ofNullable(transactionLineItem)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /transactionLineItems/:id -> delete the "id" transactionLineItem.
     */
    @RequestMapping(value = "/transactionLineItems/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTransactionLineItem(@PathVariable Long id) {
        log.debug("REST request to delete TransactionLineItem : {}", id);
        transactionLineItemRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("transactionLineItem", id.toString())).build();
    }
}
