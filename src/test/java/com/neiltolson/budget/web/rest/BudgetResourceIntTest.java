package com.neiltolson.budget.web.rest;

import com.neiltolson.budget.Application;
import com.neiltolson.budget.domain.Budget;
import com.neiltolson.budget.repository.BudgetRepository;

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
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.neiltolson.budget.domain.enumeration.BudgetStatus;

/**
 * Test class for the BudgetResource REST controller.
 *
 * @see BudgetResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BudgetResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final ZonedDateTime DEFAULT_LAST_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LAST_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LAST_UPDATED_STR = dateTimeFormatter.format(DEFAULT_LAST_UPDATED);
    
    private static final BudgetStatus DEFAULT_STATUS = BudgetStatus.Working;
    private static final BudgetStatus UPDATED_STATUS = BudgetStatus.Approved;

    @Inject
    private BudgetRepository budgetRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBudgetMockMvc;

    private Budget budget;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BudgetResource budgetResource = new BudgetResource();
        ReflectionTestUtils.setField(budgetResource, "budgetRepository", budgetRepository);
        this.restBudgetMockMvc = MockMvcBuilders.standaloneSetup(budgetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        budget = new Budget();
        budget.setUserId(DEFAULT_USER_ID);
        budget.setName(DEFAULT_NAME);
        budget.setStartDate(DEFAULT_START_DATE);
        budget.setEndDate(DEFAULT_END_DATE);
        budget.setLastUpdated(DEFAULT_LAST_UPDATED);
        budget.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createBudget() throws Exception {
        int databaseSizeBeforeCreate = budgetRepository.findAll().size();

        // Create the Budget

        restBudgetMockMvc.perform(post("/api/budgets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(budget)))
                .andExpect(status().isCreated());

        // Validate the Budget in the database
        List<Budget> budgets = budgetRepository.findAll();
        assertThat(budgets).hasSize(databaseSizeBeforeCreate + 1);
        Budget testBudget = budgets.get(budgets.size() - 1);
        assertThat(testBudget.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testBudget.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBudget.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testBudget.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testBudget.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testBudget.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void getAllBudgets() throws Exception {
        // Initialize the database
        budgetRepository.saveAndFlush(budget);

        // Get all the budgets
        restBudgetMockMvc.perform(get("/api/budgets?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(budget.getId().intValue())))
                .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
                .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED_STR)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getBudget() throws Exception {
        // Initialize the database
        budgetRepository.saveAndFlush(budget);

        // Get the budget
        restBudgetMockMvc.perform(get("/api/budgets/{id}", budget.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(budget.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED_STR))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBudget() throws Exception {
        // Get the budget
        restBudgetMockMvc.perform(get("/api/budgets/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBudget() throws Exception {
        // Initialize the database
        budgetRepository.saveAndFlush(budget);

		int databaseSizeBeforeUpdate = budgetRepository.findAll().size();

        // Update the budget
        budget.setUserId(UPDATED_USER_ID);
        budget.setName(UPDATED_NAME);
        budget.setStartDate(UPDATED_START_DATE);
        budget.setEndDate(UPDATED_END_DATE);
        budget.setLastUpdated(UPDATED_LAST_UPDATED);
        budget.setStatus(UPDATED_STATUS);

        restBudgetMockMvc.perform(put("/api/budgets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(budget)))
                .andExpect(status().isOk());

        // Validate the Budget in the database
        List<Budget> budgets = budgetRepository.findAll();
        assertThat(budgets).hasSize(databaseSizeBeforeUpdate);
        Budget testBudget = budgets.get(budgets.size() - 1);
        assertThat(testBudget.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testBudget.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBudget.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testBudget.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testBudget.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testBudget.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteBudget() throws Exception {
        // Initialize the database
        budgetRepository.saveAndFlush(budget);

		int databaseSizeBeforeDelete = budgetRepository.findAll().size();

        // Get the budget
        restBudgetMockMvc.perform(delete("/api/budgets/{id}", budget.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Budget> budgets = budgetRepository.findAll();
        assertThat(budgets).hasSize(databaseSizeBeforeDelete - 1);
    }
}
