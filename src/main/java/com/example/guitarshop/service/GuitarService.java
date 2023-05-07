package com.example.guitarshop.service;

import com.example.guitarshop.service.dto.GuitarDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.example.guitarshop.domain.Guitar}.
 */
public interface GuitarService {
    /**
     * Save a guitar.
     *
     * @param guitarDTO the entity to save.
     * @return the persisted entity.
     */
    GuitarDTO save(GuitarDTO guitarDTO);

    /**
     * Updates a guitar.
     *
     * @param guitarDTO the entity to update.
     * @return the persisted entity.
     */
    GuitarDTO update(GuitarDTO guitarDTO);

    /**
     * Partially updates a guitar.
     *
     * @param guitarDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GuitarDTO> partialUpdate(GuitarDTO guitarDTO);

    /**
     * Get all the guitars.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GuitarDTO> findAll(Pageable pageable);

    /**
     * Get the "id" guitar.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GuitarDTO> findOne(Long id);

    /**
     * Delete the "id" guitar.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
