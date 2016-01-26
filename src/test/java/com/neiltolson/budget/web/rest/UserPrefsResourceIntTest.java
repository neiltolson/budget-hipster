package com.neiltolson.budget.web.rest;

import com.neiltolson.budget.Application;
import com.neiltolson.budget.domain.UserPrefs;
import com.neiltolson.budget.repository.UserPrefsRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the UserPrefsResource REST controller.
 *
 * @see UserPrefsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UserPrefsResourceIntTest {


    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;
    private static final String DEFAULT_KEY = "AAAAA";
    private static final String UPDATED_KEY = "BBBBB";
    private static final String DEFAULT_VALUE = "AAAAA";
    private static final String UPDATED_VALUE = "BBBBB";

    @Inject
    private UserPrefsRepository userPrefsRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUserPrefsMockMvc;

    private UserPrefs userPrefs;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserPrefsResource userPrefsResource = new UserPrefsResource();
        ReflectionTestUtils.setField(userPrefsResource, "userPrefsRepository", userPrefsRepository);
        this.restUserPrefsMockMvc = MockMvcBuilders.standaloneSetup(userPrefsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        userPrefs = new UserPrefs();
        userPrefs.setUserId(DEFAULT_USER_ID);
        userPrefs.setKey(DEFAULT_KEY);
        userPrefs.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createUserPrefs() throws Exception {
        int databaseSizeBeforeCreate = userPrefsRepository.findAll().size();

        // Create the UserPrefs

        restUserPrefsMockMvc.perform(post("/api/userPrefss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userPrefs)))
                .andExpect(status().isCreated());

        // Validate the UserPrefs in the database
        List<UserPrefs> userPrefss = userPrefsRepository.findAll();
        assertThat(userPrefss).hasSize(databaseSizeBeforeCreate + 1);
        UserPrefs testUserPrefs = userPrefss.get(userPrefss.size() - 1);
        assertThat(testUserPrefs.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserPrefs.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testUserPrefs.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void getAllUserPrefss() throws Exception {
        // Initialize the database
        userPrefsRepository.saveAndFlush(userPrefs);

        // Get all the userPrefss
        restUserPrefsMockMvc.perform(get("/api/userPrefss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userPrefs.getId().intValue())))
                .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
                .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getUserPrefs() throws Exception {
        // Initialize the database
        userPrefsRepository.saveAndFlush(userPrefs);

        // Get the userPrefs
        restUserPrefsMockMvc.perform(get("/api/userPrefss/{id}", userPrefs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userPrefs.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserPrefs() throws Exception {
        // Get the userPrefs
        restUserPrefsMockMvc.perform(get("/api/userPrefss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserPrefs() throws Exception {
        // Initialize the database
        userPrefsRepository.saveAndFlush(userPrefs);

		int databaseSizeBeforeUpdate = userPrefsRepository.findAll().size();

        // Update the userPrefs
        userPrefs.setUserId(UPDATED_USER_ID);
        userPrefs.setKey(UPDATED_KEY);
        userPrefs.setValue(UPDATED_VALUE);

        restUserPrefsMockMvc.perform(put("/api/userPrefss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userPrefs)))
                .andExpect(status().isOk());

        // Validate the UserPrefs in the database
        List<UserPrefs> userPrefss = userPrefsRepository.findAll();
        assertThat(userPrefss).hasSize(databaseSizeBeforeUpdate);
        UserPrefs testUserPrefs = userPrefss.get(userPrefss.size() - 1);
        assertThat(testUserPrefs.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserPrefs.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testUserPrefs.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteUserPrefs() throws Exception {
        // Initialize the database
        userPrefsRepository.saveAndFlush(userPrefs);

		int databaseSizeBeforeDelete = userPrefsRepository.findAll().size();

        // Get the userPrefs
        restUserPrefsMockMvc.perform(delete("/api/userPrefss/{id}", userPrefs.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserPrefs> userPrefss = userPrefsRepository.findAll();
        assertThat(userPrefss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
