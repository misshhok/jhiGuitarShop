package com.example.guitarshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.guitarshop.IntegrationTest;
import com.example.guitarshop.domain.GuitarType;
import com.example.guitarshop.repository.GuitarTypeRepository;
import com.example.guitarshop.service.dto.GuitarTypeDTO;
import com.example.guitarshop.service.mapper.GuitarTypeMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GuitarTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GuitarTypeResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/guitar-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GuitarTypeRepository guitarTypeRepository;

    @Autowired
    private GuitarTypeMapper guitarTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGuitarTypeMockMvc;

    private GuitarType guitarType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GuitarType createEntity(EntityManager em) {
        GuitarType guitarType = new GuitarType().title(DEFAULT_TITLE);
        return guitarType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GuitarType createUpdatedEntity(EntityManager em) {
        GuitarType guitarType = new GuitarType().title(UPDATED_TITLE);
        return guitarType;
    }

    @BeforeEach
    public void initTest() {
        guitarType = createEntity(em);
    }

    @Test
    @Transactional
    void createGuitarType() throws Exception {
        int databaseSizeBeforeCreate = guitarTypeRepository.findAll().size();
        // Create the GuitarType
        GuitarTypeDTO guitarTypeDTO = guitarTypeMapper.toDto(guitarType);
        restGuitarTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guitarTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the GuitarType in the database
        List<GuitarType> guitarTypeList = guitarTypeRepository.findAll();
        assertThat(guitarTypeList).hasSize(databaseSizeBeforeCreate + 1);
        GuitarType testGuitarType = guitarTypeList.get(guitarTypeList.size() - 1);
        assertThat(testGuitarType.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void createGuitarTypeWithExistingId() throws Exception {
        // Create the GuitarType with an existing ID
        guitarType.setId(1L);
        GuitarTypeDTO guitarTypeDTO = guitarTypeMapper.toDto(guitarType);

        int databaseSizeBeforeCreate = guitarTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGuitarTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guitarTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GuitarType in the database
        List<GuitarType> guitarTypeList = guitarTypeRepository.findAll();
        assertThat(guitarTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGuitarTypes() throws Exception {
        // Initialize the database
        guitarTypeRepository.saveAndFlush(guitarType);

        // Get all the guitarTypeList
        restGuitarTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guitarType.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }

    @Test
    @Transactional
    void getGuitarType() throws Exception {
        // Initialize the database
        guitarTypeRepository.saveAndFlush(guitarType);

        // Get the guitarType
        restGuitarTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, guitarType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(guitarType.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE));
    }

    @Test
    @Transactional
    void getNonExistingGuitarType() throws Exception {
        // Get the guitarType
        restGuitarTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGuitarType() throws Exception {
        // Initialize the database
        guitarTypeRepository.saveAndFlush(guitarType);

        int databaseSizeBeforeUpdate = guitarTypeRepository.findAll().size();

        // Update the guitarType
        GuitarType updatedGuitarType = guitarTypeRepository.findById(guitarType.getId()).get();
        // Disconnect from session so that the updates on updatedGuitarType are not directly saved in db
        em.detach(updatedGuitarType);
        updatedGuitarType.title(UPDATED_TITLE);
        GuitarTypeDTO guitarTypeDTO = guitarTypeMapper.toDto(updatedGuitarType);

        restGuitarTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, guitarTypeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guitarTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the GuitarType in the database
        List<GuitarType> guitarTypeList = guitarTypeRepository.findAll();
        assertThat(guitarTypeList).hasSize(databaseSizeBeforeUpdate);
        GuitarType testGuitarType = guitarTypeList.get(guitarTypeList.size() - 1);
        assertThat(testGuitarType.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    void putNonExistingGuitarType() throws Exception {
        int databaseSizeBeforeUpdate = guitarTypeRepository.findAll().size();
        guitarType.setId(count.incrementAndGet());

        // Create the GuitarType
        GuitarTypeDTO guitarTypeDTO = guitarTypeMapper.toDto(guitarType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGuitarTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, guitarTypeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guitarTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GuitarType in the database
        List<GuitarType> guitarTypeList = guitarTypeRepository.findAll();
        assertThat(guitarTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGuitarType() throws Exception {
        int databaseSizeBeforeUpdate = guitarTypeRepository.findAll().size();
        guitarType.setId(count.incrementAndGet());

        // Create the GuitarType
        GuitarTypeDTO guitarTypeDTO = guitarTypeMapper.toDto(guitarType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGuitarTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guitarTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GuitarType in the database
        List<GuitarType> guitarTypeList = guitarTypeRepository.findAll();
        assertThat(guitarTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGuitarType() throws Exception {
        int databaseSizeBeforeUpdate = guitarTypeRepository.findAll().size();
        guitarType.setId(count.incrementAndGet());

        // Create the GuitarType
        GuitarTypeDTO guitarTypeDTO = guitarTypeMapper.toDto(guitarType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGuitarTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guitarTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GuitarType in the database
        List<GuitarType> guitarTypeList = guitarTypeRepository.findAll();
        assertThat(guitarTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGuitarTypeWithPatch() throws Exception {
        // Initialize the database
        guitarTypeRepository.saveAndFlush(guitarType);

        int databaseSizeBeforeUpdate = guitarTypeRepository.findAll().size();

        // Update the guitarType using partial update
        GuitarType partialUpdatedGuitarType = new GuitarType();
        partialUpdatedGuitarType.setId(guitarType.getId());

        partialUpdatedGuitarType.title(UPDATED_TITLE);

        restGuitarTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGuitarType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGuitarType))
            )
            .andExpect(status().isOk());

        // Validate the GuitarType in the database
        List<GuitarType> guitarTypeList = guitarTypeRepository.findAll();
        assertThat(guitarTypeList).hasSize(databaseSizeBeforeUpdate);
        GuitarType testGuitarType = guitarTypeList.get(guitarTypeList.size() - 1);
        assertThat(testGuitarType.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    void fullUpdateGuitarTypeWithPatch() throws Exception {
        // Initialize the database
        guitarTypeRepository.saveAndFlush(guitarType);

        int databaseSizeBeforeUpdate = guitarTypeRepository.findAll().size();

        // Update the guitarType using partial update
        GuitarType partialUpdatedGuitarType = new GuitarType();
        partialUpdatedGuitarType.setId(guitarType.getId());

        partialUpdatedGuitarType.title(UPDATED_TITLE);

        restGuitarTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGuitarType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGuitarType))
            )
            .andExpect(status().isOk());

        // Validate the GuitarType in the database
        List<GuitarType> guitarTypeList = guitarTypeRepository.findAll();
        assertThat(guitarTypeList).hasSize(databaseSizeBeforeUpdate);
        GuitarType testGuitarType = guitarTypeList.get(guitarTypeList.size() - 1);
        assertThat(testGuitarType.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    void patchNonExistingGuitarType() throws Exception {
        int databaseSizeBeforeUpdate = guitarTypeRepository.findAll().size();
        guitarType.setId(count.incrementAndGet());

        // Create the GuitarType
        GuitarTypeDTO guitarTypeDTO = guitarTypeMapper.toDto(guitarType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGuitarTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, guitarTypeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(guitarTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GuitarType in the database
        List<GuitarType> guitarTypeList = guitarTypeRepository.findAll();
        assertThat(guitarTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGuitarType() throws Exception {
        int databaseSizeBeforeUpdate = guitarTypeRepository.findAll().size();
        guitarType.setId(count.incrementAndGet());

        // Create the GuitarType
        GuitarTypeDTO guitarTypeDTO = guitarTypeMapper.toDto(guitarType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGuitarTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(guitarTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GuitarType in the database
        List<GuitarType> guitarTypeList = guitarTypeRepository.findAll();
        assertThat(guitarTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGuitarType() throws Exception {
        int databaseSizeBeforeUpdate = guitarTypeRepository.findAll().size();
        guitarType.setId(count.incrementAndGet());

        // Create the GuitarType
        GuitarTypeDTO guitarTypeDTO = guitarTypeMapper.toDto(guitarType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGuitarTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(guitarTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GuitarType in the database
        List<GuitarType> guitarTypeList = guitarTypeRepository.findAll();
        assertThat(guitarTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGuitarType() throws Exception {
        // Initialize the database
        guitarTypeRepository.saveAndFlush(guitarType);

        int databaseSizeBeforeDelete = guitarTypeRepository.findAll().size();

        // Delete the guitarType
        restGuitarTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, guitarType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GuitarType> guitarTypeList = guitarTypeRepository.findAll();
        assertThat(guitarTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
