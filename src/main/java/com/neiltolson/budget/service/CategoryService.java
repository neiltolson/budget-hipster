package com.neiltolson.budget.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neiltolson.budget.domain.Category;
import com.neiltolson.budget.repository.CategoryRepository;
import com.neiltolson.budget.web.rest.dto.CategoryListDTO;

@Service
@Transactional
public class CategoryService {

	private CategoryRepository categoryRepository;

	@Inject
	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public void initializeUserCategories(Long userId) {
		List<Category> oldCategories = categoryRepository.findByUserId(userId);
		oldCategories.forEach(c -> categoryRepository.delete(c));
		
		List<Category> defaultCategories = categoryRepository.findByUserId(null);
		defaultCategories.forEach(c -> categoryRepository.save(copyCategory(c, userId)));
	}
	
	public CategoryListDTO getUserCategories(Long userId) {
		return null;
	}
	
	Category copyCategory(Category original, Long userId) {
		Category newCategory = new Category();
		BeanUtils.copyProperties(original, newCategory);
		newCategory.setId(null);
		newCategory.setUserId(userId);
		return newCategory;
	}
}
