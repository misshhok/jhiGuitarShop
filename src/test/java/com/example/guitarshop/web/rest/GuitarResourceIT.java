package com.example.guitarshop.web.rest;

import static com.example.guitarshop.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.guitarshop.IntegrationTest;
import com.example.guitarshop.domain.Guitar;
import com.example.guitarshop.repository.GuitarRepository;
import com.example.guitarshop.service.dto.GuitarDTO;
import com.example.guitarshop.service.mapper.GuitarMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link GuitarResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GuitarResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/guitars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GuitarRepository guitarRepository;

    @Autowired
    private GuitarMapper guitarMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGuitarMockMvc;

    private Guitar guitar;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Guitar createEntity(EntityManager em) {
        Guitar guitar = new Guitar().title(DEFAULT_TITLE).price(DEFAULT_PRICE);
        return guitar;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Guitar createUpdatedEntity(EntityManager em) {
        Guitar guitar = new Guitar().title(UPDATED_TITLE).price(UPDATED_PRICE);
        return guitar;
    }

    @BeforeEach
    public void initTest() {
        guitar = createEntity(em);
    }

    @Test
    @Transactional
    void createGuitar() throws Exception {
        int databaseSizeBeforeCreate = guitarRepository.findAll().size();
        // Create the Guitar
        GuitarDTO guitarDTO = guitarMapper.toDto(guitar);
        restGuitarMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guitarDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Guitar in the database
        List<Guitar> guitarList = guitarRepository.findAll();
        assertThat(guitarList).hasSize(databaseSizeBeforeCreate + 1);
        Guitar testGuitar = guitarList.get(guitarList.size() - 1);
        assertThat(testGuitar.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testGuitar.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void createGuitarWithExistingId() throws Exception {
        // Create the Guitar with an existing ID
        guitar.setId(1L);
        GuitarDTO guitarDTO = guitarMapper.toDto(guitar);

        int databaseSizeBeforeCreate = guitarRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGuitarMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guitarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Guitar in the database
        List<Guitar> guitarList = guitarRepository.findAll();
        assertThat(guitarList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGuitars() throws Exception {
        // Initialize the database
        guitarRepository.saveAndFlush(guitar);

        // Get all the guitarList
        restGuitarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guitar.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))));
    }

    @Test
    @Transactional
    void getGuitar() throws Exception {
        // Initialize the database
        guitarRepository.saveAndFlush(guitar);

        // Get the guitar
        restGuitarMockMvc
            .perform(get(ENTITY_API_URL_ID, guitar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(guitar.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)));
    }

    @Test
    @Transactional
    void getNonExistingGuitar() throws Exception {
        // Get the guitar
        restGuitarMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGuitar() throws Exception {
        // Initialize the database
        guitarRepository.saveAndFlush(guitar);

        int databaseSizeBeforeUpdate = guitarRepository.findAll().size();

        // Update the guitar
        Guitar updatedGuitar = guitarRepository.findById(guitar.getId()).get();
        // Disconnect from session so that the updates on updatedGuitar are not directly saved in db
        em.detach(updatedGuitar);
        updatedGuitar.title(UPDATED_TITLE).price(UPDATED_PRICE);
        GuitarDTO guitarDTO = guitarMapper.toDto(updatedGuitar);

        restGuitarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, guitarDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guitarDTO))
            )
            .andExpect(status().isOk());

        // Validate the Guitar in the database
        List<Guitar> guitarList = guitarRepository.findAll();
        assertThat(guitarList).hasSize(databaseSizeBeforeUpdate);
        Guitar testGuitar = guitarList.get(guitarList.size() - 1);
        assertThat(testGuitar.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testGuitar.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingGuitar() throws Exception {
        int databaseSizeBeforeUpdate = guitarRepository.findAll().size();
        guitar.setId(count.incrementAndGet());

        // Create the Guitar
        GuitarDTO guitarDTO = guitarMapper.toDto(guitar);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGuitarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, guitarDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guitarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Guitar in the database
        List<Guitar> guitarList = guitarRepository.findAll();
        assertThat(guitarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGuitar() throws Exception {
        int databaseSizeBeforeUpdate = guitarRepository.findAll().size();
        guitar.setId(count.incrementAndGet());

        // Create the Guitar
        GuitarDTO guitarDTO = guitarMapper.toDto(guitar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGuitarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guitarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Guitar in the database
        List<Guitar> guitarList = guitarRepository.findAll();
        assertThat(guitarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGuitar() throws Exception {
        int databaseSizeBeforeUpdate = guitarRepository.findAll().size();
        guitar.setId(count.incrementAndGet());

        // Create the Guitar
        GuitarDTO guitarDTO = guitarMapper.toDto(guitar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGuitarMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guitarDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Guitar in the database
        List<Guitar> guitarList = guitarRepository.findAll();
        assertThat(guitarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGuitarWithPatch() throws Exception {
        // Initialize the database
        guitarRepository.saveAndFlush(guitar);

        int databaseSizeBeforeUpdate = guitarRepository.findAll().size();

        // Update the guitar using partial update
        Guitar partialUpdatedGuitar = new Guitar();
        partialUpdatedGuitar.setId(guitar.getId());

        partialUpdatedGuitar.title(UPDATED_TITLE).price(UPDATED_PRICE);

        restGuitarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGuitar.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGuitar))
            )
            .andExpect(status().isOk());

        // Validate the Guitar in the database
        List<Guitar> guitarList = guitarRepository.findAll();
        assertThat(guitarList).hasSize(databaseSizeBeforeUpdate);
        Guitar testGuitar = guitarList.get(guitarList.size() - 1);
        assertThat(testGuitar.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testGuitar.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateGuitarWithPatch() throws Exception {
        // Initialize the database
        guitarRepository.saveAndFlush(guitar);

        int databaseSizeBeforeUpdate = guitarRepository.findAll().size();

        // Update the guitar using partial update
        Guitar partialUpdatedGuitar = new Guitar();
        partialUpdatedGuitar.setId(guitar.getId());

        partialUpdatedGuitar.title(UPDATED_TITLE).price(UPDATED_PRICE);

        restGuitarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGuitar.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGuitar))
            )
            .andExpect(status().isOk());

        // Validate the Guitar in the database
        List<Guitar> guitarList = guitarRepository.findAll();
        assertThat(guitarList).hasSize(databaseSizeBeforeUpdate);
        Guitar testGuitar = guitarList.get(guitarList.size() - 1);
        assertThat(testGuitar.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testGuitar.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingGuitar() throws Exception {
        int databaseSizeBeforeUpdate = guitarRepository.findAll().size();
        guitar.setId(count.incrementAndGet());

        // Create the Guitar
        GuitarDTO guitarDTO = guitarMapper.toDto(guitar);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGuitarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, guitarDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(guitarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Guitar in the database
        List<Guitar> guitarList = guitarRepository.findAll();
        assertThat(guitarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGuitar() throws Exception {
        int databaseSizeBeforeUpdate = guitarRepository.findAll().size();
        guitar.setId(count.incrementAndGet());

        // Create the Guitar
        GuitarDTO guitarDTO = guitarMapper.toDto(guitar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGuitarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(guitarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Guitar in the database
        List<Guitar> guitarList = guitarRepository.findAll();
        assertThat(guitarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGuitar() throws Exception {
        int databaseSizeBeforeUpdate = guitarRepository.findAll().size();
        guitar.setId(count.incrementAndGet());

        // Create the Guitar
        GuitarDTO guitarDTO = guitarMapper.toDto(guitar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGuitarMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(guitarDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Guitar in the database
        List<Guitar> guitarList = guitarRepository.findAll();
        assertThat(guitarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGuitar() throws Exception {
        // Initialize the database
        guitarRepository.saveAndFlush(guitar);

        int databaseSizeBeforeDelete = guitarRepository.findAll().size();

        // Delete the guitar
        restGuitarMockMvc
            .perform(delete(ENTITY_API_URL_ID, guitar.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Guitar> guitarList = guitarRepository.findAll();
        assertThat(guitarList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
