package com.neiltolson.budget.web.rest;

import com.neiltolson.budget.Application;
import com.neiltolson.budget.domain.BudgetAccount;
import com.neiltolson.budget.repository.BudgetAccountRepository;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.neiltolson.budget.domain.enumeration.BudgetAccountType;
import com.neiltolson.budget.domain.enumeration.BudgetAccountStatus;

/**
 * Test class for the BudgetAccountResource REST controller.
 *
 * @see BudgetAccountResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BudgetAccountResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;
    
    private static final BudgetAccountType DEFAULT_ACCOUNT_TYPE = BudgetAccountType.Checking;
    private static final BudgetAccountType UPDATED_ACCOUNT_TYPE = BudgetAccountType.Savings;
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_SUB_ACCOUNT_NAME = "AAAAA";
    private static final String UPDATED_SUB_ACCOUNT_NAME = "BBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final ZonedDateTime DEFAULT_LAST_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LAST_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LAST_UPDATED_STR = dateTimeFormatter.format(DEFAULT_LAST_UPDATED);
    
    private static final BudgetAccountStatus DEFAULT_STATUS = BudgetAccountStatus.Active;
    private static final BudgetAccountStatus UPDATED_STATUS = BudgetAccountStatus.Inactive;
    private static final String DEFAULT_NOTES = "AAAAA";
    private static final String UPDATED_NOTES = "BBBBB";

    private static final BigDecimal DEFAULT_START_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_START_BALANCE = new BigDecimal(2);

    private static final Integer DEFAULT_SORT_ORDER = 1;
    private static final Integer UPDATED_SORT_ORDER = 2;

    @Inject
    private BudgetAccountRepository budgetAccountRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBudgetAccountMockMvc;

    private BudgetAccount budgetAccount;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BudgetAccountResource budgetAccountResource = new BudgetAccountResource();
        ReflectionTestUtils.setField(budgetAccountResource, "budgetAccountRepository", budgetAccountRepository);
        this.restBudgetAccountMockMvc = MockMvcBuilders.standaloneSetup(budgetAccountResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        budgetAccount = new BudgetAccount();
        budgetAccount.setUserId(DEFAULT_USER_ID);
        budgetAccount.setAccountType(DEFAULT_ACCOUNT_TYPE);
        budgetAccount.setName(DEFAULT_NAME);
        budgetAccount.setSubAccountName(DEFAULT_SUB_ACCOUNT_NAME);
        budgetAccount.setStartDate(DEFAULT_START_DATE);
        budgetAccount.setEndDate(DEFAULT_END_DATE);
        budgetAccount.setLastUpdated(DEFAULT_LAST_UPDATED);
        budgetAccount.setStatus(DEFAULT_STATUS);
        budgetAccount.setNotes(DEFAULT_NOTES);
        budgetAccount.setStartBalance(DEFAULT_START_BALANCE);
        budgetAccount.setSortOrder(DEFAULT_SORT_ORDER);
    }

    @Test
    @Transactional
    public void createBudgetAccount() throws Exception {
        int databaseSizeBeforeCreate = budgetAccountRepository.findAll().size();

        // Create the BudgetAccount

        restBudgetAccountMockMvc.perform(post("/api/budgetAccounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(budgetAccount)))
                .andExpect(status().isCreated());

        // Validate the BudgetAccount in the database
        List<BudgetAccount> budgetAccounts = budgetAccountRepository.findAll();
        assertThat(budgetAccounts).hasSize(databaseSizeBeforeCreate + 1);
        BudgetAccount testBudgetAccount = budgetAccounts.get(budgetAccounts.size() - 1);
        assertThat(testBudgetAccount.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testBudgetAccount.getAccountType()).isEqualTo(DEFAULT_ACCOUNT_TYPE);
        assertThat(testBudgetAccount.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBudgetAccount.getSubAccountName()).isEqualTo(DEFAULT_SUB_ACCOUNT_NAME);
        assertThat(testBudgetAccount.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testBudgetAccount.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testBudgetAccount.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testBudgetAccount.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testBudgetAccount.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testBudgetAccount.getStartBalance()).isEqualTo(DEFAULT_START_BALANCE);
        assertThat(testBudgetAccount.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
    }

    @Test
    @Transactional
    public void getAllBudgetAccounts() throws Exception {
        // Initialize the database
        budgetAccountRepository.saveAndFlush(budgetAccount);

        // Get all the budgetAccounts
        restBudgetAccountMockMvc.perform(get("/api/budgetAccounts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(budgetAccount.getId().intValue())))
                .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
                .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].subAccountName").value(hasItem(DEFAULT_SUB_ACCOUNT_NAME.toString())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
                .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED_STR)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
                .andExpect(jsonPath("$.[*].startBalance").value(hasItem(DEFAULT_START_BALANCE.intValue())))
                .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)));
    }

    @Test
    @Transactional
    public void getBudgetAccount() throws Exception {
        // Initialize the database
        budgetAccountRepository.saveAndFlush(budgetAccount);

        // Get the budgetAccount
        restBudgetAccountMockMvc.perform(get("/api/budgetAccounts/{id}", budgetAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(budgetAccount.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.accountType").value(DEFAULT_ACCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.subAccountName").value(DEFAULT_SUB_ACCOUNT_NAME.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED_STR))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.startBalance").value(DEFAULT_START_BALANCE.intValue()))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER));
    }

    @Test
    @Transactional
    public void getNonExistingBudgetAccount() throws Exception {
        // Get the budgetAccount
        restBudgetAccountMockMvc.perform(get("/api/budgetAccounts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBudgetAccount() throws Exception {
        // Initialize the database
        budgetAccountRepository.saveAndFlush(budgetAccount);

		int databaseSizeBeforeUpdate = budgetAccountRepository.findAll().size();

        // Update the budgetAccount
        budgetAccount.setUserId(UPDATED_USER_ID);
        budgetAccount.setAccountType(UPDATED_ACCOUNT_TYPE);
        budgetAccount.setName(UPDATED_NAME);
        budgetAccount.setSubAccountName(UPDATED_SUB_ACCOUNT_NAME);
        budgetAccount.setStartDate(UPDATED_START_DATE);
        budgetAccount.setEndDate(UPDATED_END_DATE);
        budgetAccount.setLastUpdated(UPDATED_LAST_UPDATED);
        budgetAccount.setStatus(UPDATED_STATUS);
        budgetAccount.setNotes(UPDATED_NOTES);
        budgetAccount.setStartBalance(UPDATED_START_BALANCE);
        budgetAccount.setSortOrder(UPDATED_SORT_ORDER);

        restBudgetAccountMockMvc.perform(put("/api/budgetAccounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(budgetAccount)))
                .andExpect(status().isOk());

        // Validate the BudgetAccount in the database
        List<BudgetAccount> budgetAccounts = budgetAccountRepository.findAll();
        assertThat(budgetAccounts).hasSize(databaseSizeBeforeUpdate);
        BudgetAccount testBudgetAccount = budgetAccounts.get(budgetAccounts.size() - 1);
        assertThat(testBudgetAccount.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testBudgetAccount.getAccountType()).isEqualTo(UPDATED_ACCOUNT_TYPE);
        assertThat(testBudgetAccount.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBudgetAccount.getSubAccountName()).isEqualTo(UPDATED_SUB_ACCOUNT_NAME);
        assertThat(testBudgetAccount.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testBudgetAccount.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testBudgetAccount.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testBudgetAccount.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBudgetAccount.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testBudgetAccount.getStartBalance()).isEqualTo(UPDATED_START_BALANCE);
        assertThat(testBudgetAccount.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    public void deleteBudgetAccount() throws Exception {
        // Initialize the database
        budgetAccountRepository.saveAndFlush(budgetAccount);

		int databaseSizeBeforeDelete = budgetAccountRepository.findAll().size();

        // Get the budgetAccount
        restBudgetAccountMockMvc.perform(delete("/api/budgetAccounts/{id}", budgetAccount.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BudgetAccount> budgetAccounts = budgetAccountRepository.findAll();
        assertThat(budgetAccounts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
