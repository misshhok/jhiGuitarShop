package com.example.guitarshop.service;

import com.example.guitarshop.service.dto.GuitarTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.example.guitarshop.domain.GuitarType}.
 */
public interface GuitarTypeService {
    /**
     * Save a guitarType.
     *
     * @param guitarTypeDTO the entity to save.
     * @return the persisted entity.
     */
    GuitarTypeDTO save(GuitarTypeDTO guitarTypeDTO);

    /**
     * Updates a guitarType.
     *
     * @param guitarTypeDTO the entity to update.
     * @return the persisted entity.
     */
    GuitarTypeDTO update(GuitarTypeDTO guitarTypeDTO);

    /**
     * Partially updates a guitarType.
     *
     * @param guitarTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GuitarTypeDTO> partialUpdate(GuitarTypeDTO guitarTypeDTO);

    /**
     * Get all the guitarTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GuitarTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" guitarType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GuitarTypeDTO> findOne(Long id);

    /**
     * Delete the "id" guitarType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
