package com.example.guitarshop.web.rest;

import com.example.guitarshop.repository.GuitarRepository;
import com.example.guitarshop.service.GuitarService;
import com.example.guitarshop.service.dto.GuitarDTO;
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
 * REST controller for managing {@link com.example.guitarshop.domain.Guitar}.
 */
@RestController
@RequestMapping("/api")
public class GuitarResource {

    private final Logger log = LoggerFactory.getLogger(GuitarResource.class);

    private static final String ENTITY_NAME = "guitar";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GuitarService guitarService;

    private final GuitarRepository guitarRepository;

    public GuitarResource(GuitarService guitarService, GuitarRepository guitarRepository) {
        this.guitarService = guitarService;
        this.guitarRepository = guitarRepository;
    }

    /**
     * {@code POST  /guitars} : Create a new guitar.
     *
     * @param guitarDTO the guitarDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new guitarDTO, or with status {@code 400 (Bad Request)} if the guitar has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/guitars")
    public ResponseEntity<GuitarDTO> createGuitar(@RequestBody GuitarDTO guitarDTO) throws URISyntaxException {
        log.debug("REST request to save Guitar : {}", guitarDTO);
        if (guitarDTO.getId() != null) {
            throw new BadRequestAlertException("A new guitar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GuitarDTO result = guitarService.save(guitarDTO);
        return ResponseEntity
            .created(new URI("/api/guitars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /guitars/:id} : Updates an existing guitar.
     *
     * @param id the id of the guitarDTO to save.
     * @param guitarDTO the guitarDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated guitarDTO,
     * or with status {@code 400 (Bad Request)} if the guitarDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the guitarDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/guitars/{id}")
    public ResponseEntity<GuitarDTO> updateGuitar(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GuitarDTO guitarDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Guitar : {}, {}", id, guitarDTO);
        if (guitarDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, guitarDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!guitarRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GuitarDTO result = guitarService.update(guitarDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, guitarDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /guitars/:id} : Partial updates given fields of an existing guitar, field will ignore if it is null
     *
     * @param id the id of the guitarDTO to save.
     * @param guitarDTO the guitarDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated guitarDTO,
     * or with status {@code 400 (Bad Request)} if the guitarDTO is not valid,
     * or with status {@code 404 (Not Found)} if the guitarDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the guitarDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/guitars/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GuitarDTO> partialUpdateGuitar(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GuitarDTO guitarDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Guitar partially : {}, {}", id, guitarDTO);
        if (guitarDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, guitarDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!guitarRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GuitarDTO> result = guitarService.partialUpdate(guitarDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, guitarDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /guitars} : get all the guitars.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of guitars in body.
     */
    @GetMapping("/guitars")
    public ResponseEntity<List<GuitarDTO>> getAllGuitars(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Guitars");
        Page<GuitarDTO> page = guitarService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /guitars/:id} : get the "id" guitar.
     *
     * @param id the id of the guitarDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the guitarDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/guitars/{id}")
    public ResponseEntity<GuitarDTO> getGuitar(@PathVariable Long id) {
        log.debug("REST request to get Guitar : {}", id);
        Optional<GuitarDTO> guitarDTO = guitarService.findOne(id);
        return ResponseUtil.wrapOrNotFound(guitarDTO);
    }

    /**
     * {@code DELETE  /guitars/:id} : delete the "id" guitar.
     *
     * @param id the id of the guitarDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/guitars/{id}")
    public ResponseEntity<Void> deleteGuitar(@PathVariable Long id) {
        log.debug("REST request to delete Guitar : {}", id);
        guitarService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
