package com.neiltolson.budget.web.rest;

import com.neiltolson.budget.Application;
import com.neiltolson.budget.domain.TransactionLineItem;
import com.neiltolson.budget.repository.TransactionLineItemRepository;

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
 * Test class for the TransactionLineItemResource REST controller.
 *
 * @see TransactionLineItemResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TransactionLineItemResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    @Inject
    private TransactionLineItemRepository transactionLineItemRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTransactionLineItemMockMvc;

    private TransactionLineItem transactionLineItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TransactionLineItemResource transactionLineItemResource = new TransactionLineItemResource();
        ReflectionTestUtils.setField(transactionLineItemResource, "transactionLineItemRepository", transactionLineItemRepository);
        this.restTransactionLineItemMockMvc = MockMvcBuilders.standaloneSetup(transactionLineItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        transactionLineItem = new TransactionLineItem();
        transactionLineItem.setDescription(DEFAULT_DESCRIPTION);
        transactionLineItem.setAmount(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createTransactionLineItem() throws Exception {
        int databaseSizeBeforeCreate = transactionLineItemRepository.findAll().size();

        // Create the TransactionLineItem

        restTransactionLineItemMockMvc.perform(post("/api/transactionLineItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(transactionLineItem)))
                .andExpect(status().isCreated());

        // Validate the TransactionLineItem in the database
        List<TransactionLineItem> transactionLineItems = transactionLineItemRepository.findAll();
        assertThat(transactionLineItems).hasSize(databaseSizeBeforeCreate + 1);
        TransactionLineItem testTransactionLineItem = transactionLineItems.get(transactionLineItems.size() - 1);
        assertThat(testTransactionLineItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTransactionLineItem.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllTransactionLineItems() throws Exception {
        // Initialize the database
        transactionLineItemRepository.saveAndFlush(transactionLineItem);

        // Get all the transactionLineItems
        restTransactionLineItemMockMvc.perform(get("/api/transactionLineItems?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(transactionLineItem.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void getTransactionLineItem() throws Exception {
        // Initialize the database
        transactionLineItemRepository.saveAndFlush(transactionLineItem);

        // Get the transactionLineItem
        restTransactionLineItemMockMvc.perform(get("/api/transactionLineItems/{id}", transactionLineItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(transactionLineItem.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTransactionLineItem() throws Exception {
        // Get the transactionLineItem
        restTransactionLineItemMockMvc.perform(get("/api/transactionLineItems/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransactionLineItem() throws Exception {
        // Initialize the database
        transactionLineItemRepository.saveAndFlush(transactionLineItem);

		int databaseSizeBeforeUpdate = transactionLineItemRepository.findAll().size();

        // Update the transactionLineItem
        transactionLineItem.setDescription(UPDATED_DESCRIPTION);
        transactionLineItem.setAmount(UPDATED_AMOUNT);

        restTransactionLineItemMockMvc.perform(put("/api/transactionLineItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(transactionLineItem)))
                .andExpect(status().isOk());

        // Validate the TransactionLineItem in the database
        List<TransactionLineItem> transactionLineItems = transactionLineItemRepository.findAll();
        assertThat(transactionLineItems).hasSize(databaseSizeBeforeUpdate);
        TransactionLineItem testTransactionLineItem = transactionLineItems.get(transactionLineItems.size() - 1);
        assertThat(testTransactionLineItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTransactionLineItem.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void deleteTransactionLineItem() throws Exception {
        // Initialize the database
        transactionLineItemRepository.saveAndFlush(transactionLineItem);

		int databaseSizeBeforeDelete = transactionLineItemRepository.findAll().size();

        // Get the transactionLineItem
        restTransactionLineItemMockMvc.perform(delete("/api/transactionLineItems/{id}", transactionLineItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TransactionLineItem> transactionLineItems = transactionLineItemRepository.findAll();
        assertThat(transactionLineItems).hasSize(databaseSizeBeforeDelete - 1);
    }
}
