package com.neiltolson.budget.web.rest;

import com.neiltolson.budget.Application;
import com.neiltolson.budget.domain.BudgetIncome;
import com.neiltolson.budget.repository.BudgetIncomeRepository;

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


/**
 * Test class for the BudgetIncomeResource REST controller.
 *
 * @see BudgetIncomeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BudgetIncomeResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    @Inject
    private BudgetIncomeRepository budgetIncomeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBudgetIncomeMockMvc;

    private BudgetIncome budgetIncome;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BudgetIncomeResource budgetIncomeResource = new BudgetIncomeResource();
        ReflectionTestUtils.setField(budgetIncomeResource, "budgetIncomeRepository", budgetIncomeRepository);
        this.restBudgetIncomeMockMvc = MockMvcBuilders.standaloneSetup(budgetIncomeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        budgetIncome = new BudgetIncome();
        budgetIncome.setDescription(DEFAULT_DESCRIPTION);
        budgetIncome.setAmount(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createBudgetIncome() throws Exception {
        int databaseSizeBeforeCreate = budgetIncomeRepository.findAll().size();

        // Create the BudgetIncome

        restBudgetIncomeMockMvc.perform(post("/api/budgetIncomes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(budgetIncome)))
                .andExpect(status().isCreated());

        // Validate the BudgetIncome in the database
        List<BudgetIncome> budgetIncomes = budgetIncomeRepository.findAll();
        assertThat(budgetIncomes).hasSize(databaseSizeBeforeCreate + 1);
        BudgetIncome testBudgetIncome = budgetIncomes.get(budgetIncomes.size() - 1);
        assertThat(testBudgetIncome.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBudgetIncome.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllBudgetIncomes() throws Exception {
        // Initialize the database
        budgetIncomeRepository.saveAndFlush(budgetIncome);

        // Get all the budgetIncomes
        restBudgetIncomeMockMvc.perform(get("/api/budgetIncomes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(budgetIncome.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void getBudgetIncome() throws Exception {
        // Initialize the database
        budgetIncomeRepository.saveAndFlush(budgetIncome);

        // Get the budgetIncome
        restBudgetIncomeMockMvc.perform(get("/api/budgetIncomes/{id}", budgetIncome.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(budgetIncome.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBudgetIncome() throws Exception {
        // Get the budgetIncome
        restBudgetIncomeMockMvc.perform(get("/api/budgetIncomes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBudgetIncome() throws Exception {
        // Initialize the database
        budgetIncomeRepository.saveAndFlush(budgetIncome);

		int databaseSizeBeforeUpdate = budgetIncomeRepository.findAll().size();

        // Update the budgetIncome
        budgetIncome.setDescription(UPDATED_DESCRIPTION);
        budgetIncome.setAmount(UPDATED_AMOUNT);

        restBudgetIncomeMockMvc.perform(put("/api/budgetIncomes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(budgetIncome)))
                .andExpect(status().isOk());

        // Validate the BudgetIncome in the database
        List<BudgetIncome> budgetIncomes = budgetIncomeRepository.findAll();
        assertThat(budgetIncomes).hasSize(databaseSizeBeforeUpdate);
        BudgetIncome testBudgetIncome = budgetIncomes.get(budgetIncomes.size() - 1);
        assertThat(testBudgetIncome.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBudgetIncome.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void deleteBudgetIncome() throws Exception {
        // Initialize the database
        budgetIncomeRepository.saveAndFlush(budgetIncome);

		int databaseSizeBeforeDelete = budgetIncomeRepository.findAll().size();

        // Get the budgetIncome
        restBudgetIncomeMockMvc.perform(delete("/api/budgetIncomes/{id}", budgetIncome.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BudgetIncome> budgetIncomes = budgetIncomeRepository.findAll();
        assertThat(budgetIncomes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
