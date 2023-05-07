package com.example.guitarshop.web.rest;

import static com.example.guitarshop.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.guitarshop.IntegrationTest;
import com.example.guitarshop.domain.GuitarOrder;
import com.example.guitarshop.repository.GuitarOrderRepository;
import com.example.guitarshop.service.GuitarOrderService;
import com.example.guitarshop.service.dto.GuitarOrderDTO;
import com.example.guitarshop.service.mapper.GuitarOrderMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GuitarOrderResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class GuitarOrderResourceIT {

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/guitar-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GuitarOrderRepository guitarOrderRepository;

    @Mock
    private GuitarOrderRepository guitarOrderRepositoryMock;

    @Autowired
    private GuitarOrderMapper guitarOrderMapper;

    @Mock
    private GuitarOrderService guitarOrderServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGuitarOrderMockMvc;

    private GuitarOrder guitarOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GuitarOrder createEntity(EntityManager em) {
        GuitarOrder guitarOrder = new GuitarOrder().totalPrice(DEFAULT_TOTAL_PRICE);
        return guitarOrder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GuitarOrder createUpdatedEntity(EntityManager em) {
        GuitarOrder guitarOrder = new GuitarOrder().totalPrice(UPDATED_TOTAL_PRICE);
        return guitarOrder;
    }

    @BeforeEach
    public void initTest() {
        guitarOrder = createEntity(em);
    }

    @Test
    @Transactional
    void createGuitarOrder() throws Exception {
        int databaseSizeBeforeCreate = guitarOrderRepository.findAll().size();
        // Create the GuitarOrder
        GuitarOrderDTO guitarOrderDTO = guitarOrderMapper.toDto(guitarOrder);
        restGuitarOrderMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guitarOrderDTO))
            )
            .andExpect(status().isCreated());

        // Validate the GuitarOrder in the database
        List<GuitarOrder> guitarOrderList = guitarOrderRepository.findAll();
        assertThat(guitarOrderList).hasSize(databaseSizeBeforeCreate + 1);
        GuitarOrder testGuitarOrder = guitarOrderList.get(guitarOrderList.size() - 1);
        assertThat(testGuitarOrder.getTotalPrice()).isEqualByComparingTo(DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void createGuitarOrderWithExistingId() throws Exception {
        // Create the GuitarOrder with an existing ID
        guitarOrder.setId(1L);
        GuitarOrderDTO guitarOrderDTO = guitarOrderMapper.toDto(guitarOrder);

        int databaseSizeBeforeCreate = guitarOrderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGuitarOrderMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guitarOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GuitarOrder in the database
        List<GuitarOrder> guitarOrderList = guitarOrderRepository.findAll();
        assertThat(guitarOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGuitarOrders() throws Exception {
        // Initialize the database
        guitarOrderRepository.saveAndFlush(guitarOrder);

        // Get all the guitarOrderList
        restGuitarOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guitarOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGuitarOrdersWithEagerRelationshipsIsEnabled() throws Exception {
        when(guitarOrderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGuitarOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(guitarOrderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGuitarOrdersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(guitarOrderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGuitarOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(guitarOrderRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getGuitarOrder() throws Exception {
        // Initialize the database
        guitarOrderRepository.saveAndFlush(guitarOrder);

        // Get the guitarOrder
        restGuitarOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, guitarOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(guitarOrder.getId().intValue()))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)));
    }

    @Test
    @Transactional
    void getNonExistingGuitarOrder() throws Exception {
        // Get the guitarOrder
        restGuitarOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGuitarOrder() throws Exception {
        // Initialize the database
        guitarOrderRepository.saveAndFlush(guitarOrder);

        int databaseSizeBeforeUpdate = guitarOrderRepository.findAll().size();

        // Update the guitarOrder
        GuitarOrder updatedGuitarOrder = guitarOrderRepository.findById(guitarOrder.getId()).get();
        // Disconnect from session so that the updates on updatedGuitarOrder are not directly saved in db
        em.detach(updatedGuitarOrder);
        updatedGuitarOrder.totalPrice(UPDATED_TOTAL_PRICE);
        GuitarOrderDTO guitarOrderDTO = guitarOrderMapper.toDto(updatedGuitarOrder);

        restGuitarOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, guitarOrderDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guitarOrderDTO))
            )
            .andExpect(status().isOk());

        // Validate the GuitarOrder in the database
        List<GuitarOrder> guitarOrderList = guitarOrderRepository.findAll();
        assertThat(guitarOrderList).hasSize(databaseSizeBeforeUpdate);
        GuitarOrder testGuitarOrder = guitarOrderList.get(guitarOrderList.size() - 1);
        assertThat(testGuitarOrder.getTotalPrice()).isEqualByComparingTo(UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingGuitarOrder() throws Exception {
        int databaseSizeBeforeUpdate = guitarOrderRepository.findAll().size();
        guitarOrder.setId(count.incrementAndGet());

        // Create the GuitarOrder
        GuitarOrderDTO guitarOrderDTO = guitarOrderMapper.toDto(guitarOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGuitarOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, guitarOrderDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guitarOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GuitarOrder in the database
        List<GuitarOrder> guitarOrderList = guitarOrderRepository.findAll();
        assertThat(guitarOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGuitarOrder() throws Exception {
        int databaseSizeBeforeUpdate = guitarOrderRepository.findAll().size();
        guitarOrder.setId(count.incrementAndGet());

        // Create the GuitarOrder
        GuitarOrderDTO guitarOrderDTO = guitarOrderMapper.toDto(guitarOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGuitarOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guitarOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GuitarOrder in the database
        List<GuitarOrder> guitarOrderList = guitarOrderRepository.findAll();
        assertThat(guitarOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGuitarOrder() throws Exception {
        int databaseSizeBeforeUpdate = guitarOrderRepository.findAll().size();
        guitarOrder.setId(count.incrementAndGet());

        // Create the GuitarOrder
        GuitarOrderDTO guitarOrderDTO = guitarOrderMapper.toDto(guitarOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGuitarOrderMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guitarOrderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GuitarOrder in the database
        List<GuitarOrder> guitarOrderList = guitarOrderRepository.findAll();
        assertThat(guitarOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGuitarOrderWithPatch() throws Exception {
        // Initialize the database
        guitarOrderRepository.saveAndFlush(guitarOrder);

        int databaseSizeBeforeUpdate = guitarOrderRepository.findAll().size();

        // Update the guitarOrder using partial update
        GuitarOrder partialUpdatedGuitarOrder = new GuitarOrder();
        partialUpdatedGuitarOrder.setId(guitarOrder.getId());

        restGuitarOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGuitarOrder.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGuitarOrder))
            )
            .andExpect(status().isOk());

        // Validate the GuitarOrder in the database
        List<GuitarOrder> guitarOrderList = guitarOrderRepository.findAll();
        assertThat(guitarOrderList).hasSize(databaseSizeBeforeUpdate);
        GuitarOrder testGuitarOrder = guitarOrderList.get(guitarOrderList.size() - 1);
        assertThat(testGuitarOrder.getTotalPrice()).isEqualByComparingTo(DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateGuitarOrderWithPatch() throws Exception {
        // Initialize the database
        guitarOrderRepository.saveAndFlush(guitarOrder);

        int databaseSizeBeforeUpdate = guitarOrderRepository.findAll().size();

        // Update the guitarOrder using partial update
        GuitarOrder partialUpdatedGuitarOrder = new GuitarOrder();
        partialUpdatedGuitarOrder.setId(guitarOrder.getId());

        partialUpdatedGuitarOrder.totalPrice(UPDATED_TOTAL_PRICE);

        restGuitarOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGuitarOrder.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGuitarOrder))
            )
            .andExpect(status().isOk());

        // Validate the GuitarOrder in the database
        List<GuitarOrder> guitarOrderList = guitarOrderRepository.findAll();
        assertThat(guitarOrderList).hasSize(databaseSizeBeforeUpdate);
        GuitarOrder testGuitarOrder = guitarOrderList.get(guitarOrderList.size() - 1);
        assertThat(testGuitarOrder.getTotalPrice()).isEqualByComparingTo(UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingGuitarOrder() throws Exception {
        int databaseSizeBeforeUpdate = guitarOrderRepository.findAll().size();
        guitarOrder.setId(count.incrementAndGet());

        // Create the GuitarOrder
        GuitarOrderDTO guitarOrderDTO = guitarOrderMapper.toDto(guitarOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGuitarOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, guitarOrderDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(guitarOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GuitarOrder in the database
        List<GuitarOrder> guitarOrderList = guitarOrderRepository.findAll();
        assertThat(guitarOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGuitarOrder() throws Exception {
        int databaseSizeBeforeUpdate = guitarOrderRepository.findAll().size();
        guitarOrder.setId(count.incrementAndGet());

        // Create the GuitarOrder
        GuitarOrderDTO guitarOrderDTO = guitarOrderMapper.toDto(guitarOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGuitarOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(guitarOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GuitarOrder in the database
        List<GuitarOrder> guitarOrderList = guitarOrderRepository.findAll();
        assertThat(guitarOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGuitarOrder() throws Exception {
        int databaseSizeBeforeUpdate = guitarOrderRepository.findAll().size();
        guitarOrder.setId(count.incrementAndGet());

        // Create the GuitarOrder
        GuitarOrderDTO guitarOrderDTO = guitarOrderMapper.toDto(guitarOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGuitarOrderMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(guitarOrderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GuitarOrder in the database
        List<GuitarOrder> guitarOrderList = guitarOrderRepository.findAll();
        assertThat(guitarOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGuitarOrder() throws Exception {
        // Initialize the database
        guitarOrderRepository.saveAndFlush(guitarOrder);

        int databaseSizeBeforeDelete = guitarOrderRepository.findAll().size();

        // Delete the guitarOrder
        restGuitarOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, guitarOrder.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GuitarOrder> guitarOrderList = guitarOrderRepository.findAll();
        assertThat(guitarOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
