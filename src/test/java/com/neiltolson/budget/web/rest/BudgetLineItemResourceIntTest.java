package com.neiltolson.budget.web.rest;

import com.neiltolson.budget.Application;
import com.neiltolson.budget.domain.BudgetLineItem;
import com.neiltolson.budget.repository.BudgetLineItemRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.neiltolson.budget.domain.enumeration.BudgetLineItemType;

/**
 * Test class for the BudgetLineItemResource REST controller.
 *
 * @see BudgetLineItemResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BudgetLineItemResourceIntTest {


    private static final BigDecimal DEFAULT_BUDGET_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_BUDGET_AMOUNT = new BigDecimal(2);
    
    private static final BudgetLineItemType DEFAULT_BUDGET_LINE_ITEM_TYPE = BudgetLineItemType.Income;
    private static final BudgetLineItemType UPDATED_BUDGET_LINE_ITEM_TYPE = BudgetLineItemType.Carry_Over;
    private static final String DEFAULT_NOTES = "AAAAA";
    private static final String UPDATED_NOTES = "BBBBB";
    private static final String DEFAULT_CATEGORY_GROUP = "AAAAA";
    private static final String UPDATED_CATEGORY_GROUP = "BBBBB";
    private static final String DEFAULT_CATEGORY_NAME = "AAAAA";
    private static final String UPDATED_CATEGORY_NAME = "BBBBB";

    private static final Integer DEFAULT_SORT_ORDER = 1;
    private static final Integer UPDATED_SORT_ORDER = 2;

    @Inject
    private BudgetLineItemRepository budgetLineItemRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBudgetLineItemMockMvc;

    private BudgetLineItem budgetLineItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BudgetLineItemResource budgetLineItemResource = new BudgetLineItemResource();
        ReflectionTestUtils.setField(budgetLineItemResource, "budgetLineItemRepository", budgetLineItemRepository);
        this.restBudgetLineItemMockMvc = MockMvcBuilders.standaloneSetup(budgetLineItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        budgetLineItem = new BudgetLineItem();
        budgetLineItem.setBudgetAmount(DEFAULT_BUDGET_AMOUNT);
        budgetLineItem.setBudgetLineItemType(DEFAULT_BUDGET_LINE_ITEM_TYPE);
        budgetLineItem.setNotes(DEFAULT_NOTES);
        budgetLineItem.setCategoryGroup(DEFAULT_CATEGORY_GROUP);
        budgetLineItem.setCategoryName(DEFAULT_CATEGORY_NAME);
        budgetLineItem.setSortOrder(DEFAULT_SORT_ORDER);
    }

    @Test
    @Transactional
    public void createBudgetLineItem() throws Exception {
        int databaseSizeBeforeCreate = budgetLineItemRepository.findAll().size();

        // Create the BudgetLineItem

        restBudgetLineItemMockMvc.perform(post("/api/budgetLineItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(budgetLineItem)))
                .andExpect(status().isCreated());

        // Validate the BudgetLineItem in the database
        List<BudgetLineItem> budgetLineItems = budgetLineItemRepository.findAll();
        assertThat(budgetLineItems).hasSize(databaseSizeBeforeCreate + 1);
        BudgetLineItem testBudgetLineItem = budgetLineItems.get(budgetLineItems.size() - 1);
        assertThat(testBudgetLineItem.getBudgetAmount()).isEqualTo(DEFAULT_BUDGET_AMOUNT);
        assertThat(testBudgetLineItem.getBudgetLineItemType()).isEqualTo(DEFAULT_BUDGET_LINE_ITEM_TYPE);
        assertThat(testBudgetLineItem.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testBudgetLineItem.getCategoryGroup()).isEqualTo(DEFAULT_CATEGORY_GROUP);
        assertThat(testBudgetLineItem.getCategoryName()).isEqualTo(DEFAULT_CATEGORY_NAME);
        assertThat(testBudgetLineItem.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllBudgetLineItems() throws Exception {
        // Initialize the database
        budgetLineItemRepository.saveAndFlush(budgetLineItem);

        // Get all the budgetLineItems
        restBudgetLineItemMockMvc.perform(get("/api/budgetLineItems?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(budgetLineItem.getId().intValue())))
                .andExpect(jsonPath("$.[*].budgetAmount").value(hasItem(DEFAULT_BUDGET_AMOUNT.intValue())))
                .andExpect(jsonPath("$.[*].budgetLineItemType").value(hasItem(DEFAULT_BUDGET_LINE_ITEM_TYPE.toString())))
                .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
                .andExpect(jsonPath("$.[*].categoryGroup").value(hasItem(DEFAULT_CATEGORY_GROUP.toString())))
                .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME.toString())))
                .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)));
    }

    @Test
    @Transactional
    public void getBudgetLineItem() throws Exception {
        // Initialize the database
        budgetLineItemRepository.saveAndFlush(budgetLineItem);

        // Get the budgetLineItem
        restBudgetLineItemMockMvc.perform(get("/api/budgetLineItems/{id}", budgetLineItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(budgetLineItem.getId().intValue()))
            .andExpect(jsonPath("$.budgetAmount").value(DEFAULT_BUDGET_AMOUNT.intValue()))
            .andExpect(jsonPath("$.budgetLineItemType").value(DEFAULT_BUDGET_LINE_ITEM_TYPE.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.categoryGroup").value(DEFAULT_CATEGORY_GROUP.toString()))
            .andExpect(jsonPath("$.categoryName").value(DEFAULT_CATEGORY_NAME.toString()))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER));
    }

    @Test
    @Transactional
    public void getNonExistingBudgetLineItem() throws Exception {
        // Get the budgetLineItem
        restBudgetLineItemMockMvc.perform(get("/api/budgetLineItems/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBudgetLineItem() throws Exception {
        // Initialize the database
        budgetLineItemRepository.saveAndFlush(budgetLineItem);

		int databaseSizeBeforeUpdate = budgetLineItemRepository.findAll().size();

        // Update the budgetLineItem
        budgetLineItem.setBudgetAmount(UPDATED_BUDGET_AMOUNT);
        budgetLineItem.setBudgetLineItemType(UPDATED_BUDGET_LINE_ITEM_TYPE);
        budgetLineItem.setNotes(UPDATED_NOTES);
        budgetLineItem.setCategoryGroup(UPDATED_CATEGORY_GROUP);
        budgetLineItem.setCategoryName(UPDATED_CATEGORY_NAME);
        budgetLineItem.setSortOrder(UPDATED_SORT_ORDER);

        restBudgetLineItemMockMvc.perform(put("/api/budgetLineItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(budgetLineItem)))
                .andExpect(status().isOk());

        // Validate the BudgetLineItem in the database
        List<BudgetLineItem> budgetLineItems = budgetLineItemRepository.findAll();
        assertThat(budgetLineItems).hasSize(databaseSizeBeforeUpdate);
        BudgetLineItem testBudgetLineItem = budgetLineItems.get(budgetLineItems.size() - 1);
        assertThat(testBudgetLineItem.getBudgetAmount()).isEqualTo(UPDATED_BUDGET_AMOUNT);
        assertThat(testBudgetLineItem.getBudgetLineItemType()).isEqualTo(UPDATED_BUDGET_LINE_ITEM_TYPE);
        assertThat(testBudgetLineItem.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testBudgetLineItem.getCategoryGroup()).isEqualTo(UPDATED_CATEGORY_GROUP);
        assertThat(testBudgetLineItem.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
        assertThat(testBudgetLineItem.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void deleteBudgetLineItem() throws Exception {
        // Initialize the database
        budgetLineItemRepository.saveAndFlush(budgetLineItem);

		int databaseSizeBeforeDelete = budgetLineItemRepository.findAll().size();

        // Get the budgetLineItem
        restBudgetLineItemMockMvc.perform(delete("/api/budgetLineItems/{id}", budgetLineItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BudgetLineItem> budgetLineItems = budgetLineItemRepository.findAll();
        assertThat(budgetLineItems).hasSize(databaseSizeBeforeDelete - 1);
    }
}
