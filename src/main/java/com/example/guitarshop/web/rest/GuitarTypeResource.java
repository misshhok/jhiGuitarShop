package com.example.guitarshop.web.rest;

import com.example.guitarshop.repository.GuitarTypeRepository;
import com.example.guitarshop.service.GuitarTypeService;
import com.example.guitarshop.service.dto.GuitarTypeDTO;
import com.example.guitarshop.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.example.guitarshop.domain.GuitarType}.
 */
@RestController
@RequestMapping("/api")
public class GuitarTypeResource {

    private final Logger log = LoggerFactory.getLogger(GuitarTypeResource.class);

    private static final String ENTITY_NAME = "guitarType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GuitarTypeService guitarTypeService;

    private final GuitarTypeRepository guitarTypeRepository;

    public GuitarTypeResource(GuitarTypeService guitarTypeService, GuitarTypeRepository guitarTypeRepository) {
        this.guitarTypeService = guitarTypeService;
        this.guitarTypeRepository = guitarTypeRepository;
    }

    /**
     * {@code POST  /guitar-types} : Create a new guitarType.
     *
     * @param guitarTypeDTO the guitarTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new guitarTypeDTO, or with status {@code 400 (Bad Request)} if the guitarType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/guitar-types")
    public ResponseEntity<GuitarTypeDTO> createGuitarType(@RequestBody GuitarTypeDTO guitarTypeDTO) throws URISyntaxException {
        log.debug("REST request to save GuitarType : {}", guitarTypeDTO);
        if (guitarTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new guitarType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GuitarTypeDTO result = guitarTypeService.save(guitarTypeDTO);
        return ResponseEntity
            .created(new URI("/api/guitar-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /guitar-types/:id} : Updates an existing guitarType.
     *
     * @param id the id of the guitarTypeDTO to save.
     * @param guitarTypeDTO the guitarTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated guitarTypeDTO,
     * or with status {@code 400 (Bad Request)} if the guitarTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the guitarTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/guitar-types/{id}")
    public ResponseEntity<GuitarTypeDTO> updateGuitarType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GuitarTypeDTO guitarTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GuitarType : {}, {}", id, guitarTypeDTO);
        if (guitarTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, guitarTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!guitarTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GuitarTypeDTO result = guitarTypeService.update(guitarTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, guitarTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /guitar-types/:id} : Partial updates given fields of an existing guitarType, field will ignore if it is null
     *
     * @param id the id of the guitarTypeDTO to save.
     * @param guitarTypeDTO the guitarTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated guitarTypeDTO,
     * or with status {@code 400 (Bad Request)} if the guitarTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the guitarTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the guitarTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/guitar-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GuitarTypeDTO> partialUpdateGuitarType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GuitarTypeDTO guitarTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GuitarType partially : {}, {}", id, guitarTypeDTO);
        if (guitarTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, guitarTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!guitarTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GuitarTypeDTO> result = guitarTypeService.partialUpdate(guitarTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, guitarTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /guitar-types} : get all the guitarTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of guitarTypes in body.
     */
    @GetMapping("/guitar-types")
    public ResponseEntity<List<GuitarTypeDTO>> getAllGuitarTypes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of GuitarTypes");
        Page<GuitarTypeDTO> page = guitarTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /guitar-types/:id} : get the "id" guitarType.
     *
     * @param id the id of the guitarTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the guitarTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/guitar-types/{id}")
    public ResponseEntity<GuitarTypeDTO> getGuitarType(@PathVariable Long id) {
        log.debug("REST request to get GuitarType : {}", id);
        Optional<GuitarTypeDTO> guitarTypeDTO = guitarTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(guitarTypeDTO);
    }

    /**
     * {@code DELETE  /guitar-types/:id} : delete the "id" guitarType.
     *
     * @param id the id of the guitarTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/guitar-types/{id}")
    public ResponseEntity<Void> deleteGuitarType(@PathVariable Long id) {
        log.debug("REST request to delete GuitarType : {}", id);
        guitarTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
