package com.neiltolson.budget.web.rest;

import com.neiltolson.budget.Application;
import com.neiltolson.budget.domain.BudgetAccountTransaction;
import com.neiltolson.budget.repository.BudgetAccountTransactionRepository;

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
import java.time.ZoneId;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.neiltolson.budget.domain.enumeration.TransactionType;

/**
 * Test class for the BudgetAccountTransactionResource REST controller.
 *
 * @see BudgetAccountTransactionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BudgetAccountTransactionResourceIntTest {


    private static final LocalDate DEFAULT_TRANSACTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSACTION_DATE = LocalDate.now(ZoneId.systemDefault());
    
    private static final TransactionType DEFAULT_TRANSACTION_TYPE = TransactionType.Purchase;
    private static final TransactionType UPDATED_TRANSACTION_TYPE = TransactionType.Deposit;
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final String DEFAULT_NOTES = "AAAAA";
    private static final String UPDATED_NOTES = "BBBBB";
    private static final String DEFAULT_CHECK_NUMBER = "AAAAA";
    private static final String UPDATED_CHECK_NUMBER = "BBBBB";

    private static final Long DEFAULT_TRANSFER_TRANSACTION_ID = 1L;
    private static final Long UPDATED_TRANSFER_TRANSACTION_ID = 2L;

    private static final LocalDate DEFAULT_RECONCILED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RECONCILED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private BudgetAccountTransactionRepository budgetAccountTransactionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBudgetAccountTransactionMockMvc;

    private BudgetAccountTransaction budgetAccountTransaction;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BudgetAccountTransactionResource budgetAccountTransactionResource = new BudgetAccountTransactionResource();
        ReflectionTestUtils.setField(budgetAccountTransactionResource, "budgetAccountTransactionRepository", budgetAccountTransactionRepository);
        this.restBudgetAccountTransactionMockMvc = MockMvcBuilders.standaloneSetup(budgetAccountTransactionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        budgetAccountTransaction = new BudgetAccountTransaction();
        budgetAccountTransaction.setTransactionDate(DEFAULT_TRANSACTION_DATE);
        budgetAccountTransaction.setTransactionType(DEFAULT_TRANSACTION_TYPE);
        budgetAccountTransaction.setDescription(DEFAULT_DESCRIPTION);
        budgetAccountTransaction.setAmount(DEFAULT_AMOUNT);
        budgetAccountTransaction.setNotes(DEFAULT_NOTES);
        budgetAccountTransaction.setCheckNumber(DEFAULT_CHECK_NUMBER);
        budgetAccountTransaction.setTransferTransactionId(DEFAULT_TRANSFER_TRANSACTION_ID);
        budgetAccountTransaction.setReconciledDate(DEFAULT_RECONCILED_DATE);
    }

    @Test
    @Transactional
    public void createBudgetAccountTransaction() throws Exception {
        int databaseSizeBeforeCreate = budgetAccountTransactionRepository.findAll().size();

        // Create the BudgetAccountTransaction

        restBudgetAccountTransactionMockMvc.perform(post("/api/budgetAccountTransactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(budgetAccountTransaction)))
                .andExpect(status().isCreated());

        // Validate the BudgetAccountTransaction in the database
        List<BudgetAccountTransaction> budgetAccountTransactions = budgetAccountTransactionRepository.findAll();
        assertThat(budgetAccountTransactions).hasSize(databaseSizeBeforeCreate + 1);
        BudgetAccountTransaction testBudgetAccountTransaction = budgetAccountTransactions.get(budgetAccountTransactions.size() - 1);
        assertThat(testBudgetAccountTransaction.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testBudgetAccountTransaction.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
        assertThat(testBudgetAccountTransaction.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBudgetAccountTransaction.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testBudgetAccountTransaction.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testBudgetAccountTransaction.getCheckNumber()).isEqualTo(DEFAULT_CHECK_NUMBER);
        assertThat(testBudgetAccountTransaction.getTransferTransactionId()).isEqualTo(DEFAULT_TRANSFER_TRANSACTION_ID);
        assertThat(testBudgetAccountTransaction.getReconciledDate()).isEqualTo(DEFAULT_RECONCILED_DATE);
    }

    @Test
    @Transactional
    public void getAllBudgetAccountTransactions() throws Exception {
        // Initialize the database
        budgetAccountTransactionRepository.saveAndFlush(budgetAccountTransaction);

        // Get all the budgetAccountTransactions
        restBudgetAccountTransactionMockMvc.perform(get("/api/budgetAccountTransactions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(budgetAccountTransaction.getId().intValue())))
                .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
                .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
                .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
                .andExpect(jsonPath("$.[*].checkNumber").value(hasItem(DEFAULT_CHECK_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].transferTransactionId").value(hasItem(DEFAULT_TRANSFER_TRANSACTION_ID.intValue())))
                .andExpect(jsonPath("$.[*].reconciledDate").value(hasItem(DEFAULT_RECONCILED_DATE.toString())));
    }

    @Test
    @Transactional
    public void getBudgetAccountTransaction() throws Exception {
        // Initialize the database
        budgetAccountTransactionRepository.saveAndFlush(budgetAccountTransaction);

        // Get the budgetAccountTransaction
        restBudgetAccountTransactionMockMvc.perform(get("/api/budgetAccountTransactions/{id}", budgetAccountTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(budgetAccountTransaction.getId().intValue()))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.transactionType").value(DEFAULT_TRANSACTION_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.checkNumber").value(DEFAULT_CHECK_NUMBER.toString()))
            .andExpect(jsonPath("$.transferTransactionId").value(DEFAULT_TRANSFER_TRANSACTION_ID.intValue()))
            .andExpect(jsonPath("$.reconciledDate").value(DEFAULT_RECONCILED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBudgetAccountTransaction() throws Exception {
        // Get the budgetAccountTransaction
        restBudgetAccountTransactionMockMvc.perform(get("/api/budgetAccountTransactions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBudgetAccountTransaction() throws Exception {
        // Initialize the database
        budgetAccountTransactionRepository.saveAndFlush(budgetAccountTransaction);

		int databaseSizeBeforeUpdate = budgetAccountTransactionRepository.findAll().size();

        // Update the budgetAccountTransaction
        budgetAccountTransaction.setTransactionDate(UPDATED_TRANSACTION_DATE);
        budgetAccountTransaction.setTransactionType(UPDATED_TRANSACTION_TYPE);
        budgetAccountTransaction.setDescription(UPDATED_DESCRIPTION);
        budgetAccountTransaction.setAmount(UPDATED_AMOUNT);
        budgetAccountTransaction.setNotes(UPDATED_NOTES);
        budgetAccountTransaction.setCheckNumber(UPDATED_CHECK_NUMBER);
        budgetAccountTransaction.setTransferTransactionId(UPDATED_TRANSFER_TRANSACTION_ID);
        budgetAccountTransaction.setReconciledDate(UPDATED_RECONCILED_DATE);

        restBudgetAccountTransactionMockMvc.perform(put("/api/budgetAccountTransactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(budgetAccountTransaction)))
                .andExpect(status().isOk());

        // Validate the BudgetAccountTransaction in the database
        List<BudgetAccountTransaction> budgetAccountTransactions = budgetAccountTransactionRepository.findAll();
        assertThat(budgetAccountTransactions).hasSize(databaseSizeBeforeUpdate);
        BudgetAccountTransaction testBudgetAccountTransaction = budgetAccountTransactions.get(budgetAccountTransactions.size() - 1);
        assertThat(testBudgetAccountTransaction.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testBudgetAccountTransaction.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testBudgetAccountTransaction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBudgetAccountTransaction.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testBudgetAccountTransaction.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testBudgetAccountTransaction.getCheckNumber()).isEqualTo(UPDATED_CHECK_NUMBER);
        assertThat(testBudgetAccountTransaction.getTransferTransactionId()).isEqualTo(UPDATED_TRANSFER_TRANSACTION_ID);
        assertThat(testBudgetAccountTransaction.getReconciledDate()).isEqualTo(UPDATED_RECONCILED_DATE);
    }

    @Test
    @Transactional
    public void deleteBudgetAccountTransaction() throws Exception {
        // Initialize the database
        budgetAccountTransactionRepository.saveAndFlush(budgetAccountTransaction);

		int databaseSizeBeforeDelete = budgetAccountTransactionRepository.findAll().size();

        // Get the budgetAccountTransaction
        restBudgetAccountTransactionMockMvc.perform(delete("/api/budgetAccountTransactions/{id}", budgetAccountTransaction.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BudgetAccountTransaction> budgetAccountTransactions = budgetAccountTransactionRepository.findAll();
        assertThat(budgetAccountTransactions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
