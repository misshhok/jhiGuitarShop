package com.example.guitarshop.service;

import com.example.guitarshop.service.dto.GuitarOrderDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.example.guitarshop.domain.GuitarOrder}.
 */
public interface GuitarOrderService {
    /**
     * Save a guitarOrder.
     *
     * @param guitarOrderDTO the entity to save.
     * @return the persisted entity.
     */
    GuitarOrderDTO save(GuitarOrderDTO guitarOrderDTO);

    /**
     * Updates a guitarOrder.
     *
     * @param guitarOrderDTO the entity to update.
     * @return the persisted entity.
     */
    GuitarOrderDTO update(GuitarOrderDTO guitarOrderDTO);

    /**
     * Partially updates a guitarOrder.
     *
     * @param guitarOrderDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GuitarOrderDTO> partialUpdate(GuitarOrderDTO guitarOrderDTO);

    /**
     * Get all the guitarOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GuitarOrderDTO> findAll(Pageable pageable);

    /**
     * Get all the guitarOrders with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GuitarOrderDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" guitarOrder.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GuitarOrderDTO> findOne(Long id);

    /**
     * Delete the "id" guitarOrder.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
