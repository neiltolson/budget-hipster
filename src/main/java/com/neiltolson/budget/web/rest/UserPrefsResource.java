package com.neiltolson.budget.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.neiltolson.budget.domain.UserPrefs;
import com.neiltolson.budget.repository.UserPrefsRepository;
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
 * REST controller for managing UserPrefs.
 */
@RestController
@RequestMapping("/api")
public class UserPrefsResource {

    private final Logger log = LoggerFactory.getLogger(UserPrefsResource.class);
        
    @Inject
    private UserPrefsRepository userPrefsRepository;
    
    /**
     * POST  /userPrefss -> Create a new userPrefs.
     */
    @RequestMapping(value = "/userPrefss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserPrefs> createUserPrefs(@RequestBody UserPrefs userPrefs) throws URISyntaxException {
        log.debug("REST request to save UserPrefs : {}", userPrefs);
        if (userPrefs.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userPrefs", "idexists", "A new userPrefs cannot already have an ID")).body(null);
        }
        UserPrefs result = userPrefsRepository.save(userPrefs);
        return ResponseEntity.created(new URI("/api/userPrefss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userPrefs", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /userPrefss -> Updates an existing userPrefs.
     */
    @RequestMapping(value = "/userPrefss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserPrefs> updateUserPrefs(@RequestBody UserPrefs userPrefs) throws URISyntaxException {
        log.debug("REST request to update UserPrefs : {}", userPrefs);
        if (userPrefs.getId() == null) {
            return createUserPrefs(userPrefs);
        }
        UserPrefs result = userPrefsRepository.save(userPrefs);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userPrefs", userPrefs.getId().toString()))
            .body(result);
    }

    /**
     * GET  /userPrefss -> get all the userPrefss.
     */
    @RequestMapping(value = "/userPrefss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<UserPrefs> getAllUserPrefss() {
        log.debug("REST request to get all UserPrefss");
        return userPrefsRepository.findAll();
            }

    /**
     * GET  /userPrefss/:id -> get the "id" userPrefs.
     */
    @RequestMapping(value = "/userPrefss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserPrefs> getUserPrefs(@PathVariable Long id) {
        log.debug("REST request to get UserPrefs : {}", id);
        UserPrefs userPrefs = userPrefsRepository.findOne(id);
        return Optional.ofNullable(userPrefs)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /userPrefss/:id -> delete the "id" userPrefs.
     */
    @RequestMapping(value = "/userPrefss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUserPrefs(@PathVariable Long id) {
        log.debug("REST request to delete UserPrefs : {}", id);
        userPrefsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userPrefs", id.toString())).build();
    }
}
