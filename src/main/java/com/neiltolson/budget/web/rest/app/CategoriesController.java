package com.neiltolson.budget.web.rest.app;

import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.neiltolson.budget.repository.UserRepository;
import com.neiltolson.budget.service.CategoryService;
import com.neiltolson.budget.web.rest.CategoryResource;
import com.neiltolson.budget.web.rest.dto.LabelValuePair;

@RestController
@RequestMapping("/api/app")
public class CategoriesController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Inject
	private CategoryService categoryService;
	
	@Inject
	private UserRepository userRepository;
	
    @RequestMapping(value = "/categories/initialize",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LabelValuePair> intializeCategories() {
    	User login = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	log.info("login id: " + login.getUsername());
    	categoryService.initializeUserCategories(userRepository.findOneByLogin(login.getUsername()).get().getId());
    	return new ResponseEntity<LabelValuePair>(new LabelValuePair("message", "ok"), HttpStatus.OK);
    }
	
}
