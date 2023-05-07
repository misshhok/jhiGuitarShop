package com.example.guitarshop.web.rest;

import com.example.guitarshop.repository.GuitarOrderRepository;
import com.example.guitarshop.service.GuitarOrderService;
import com.example.guitarshop.service.dto.GuitarOrderDTO;
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
 * REST controller for managing {@link com.example.guitarshop.domain.GuitarOrder}.
 */
@RestController
@RequestMapping("/api")
public class GuitarOrderResource {

    private final Logger log = LoggerFactory.getLogger(GuitarOrderResource.class);

    private static final String ENTITY_NAME = "guitarOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GuitarOrderService guitarOrderService;

    private final GuitarOrderRepository guitarOrderRepository;

    public GuitarOrderResource(GuitarOrderService guitarOrderService, GuitarOrderRepository guitarOrderRepository) {
        this.guitarOrderService = guitarOrderService;
        this.guitarOrderRepository = guitarOrderRepository;
    }

    /**
     * {@code POST  /guitar-orders} : Create a new guitarOrder.
     *
     * @param guitarOrderDTO the guitarOrderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new guitarOrderDTO, or with status {@code 400 (Bad Request)} if the guitarOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/guitar-orders")
    public ResponseEntity<GuitarOrderDTO> createGuitarOrder(@RequestBody GuitarOrderDTO guitarOrderDTO) throws URISyntaxException {
        log.debug("REST request to save GuitarOrder : {}", guitarOrderDTO);
        if (guitarOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new guitarOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GuitarOrderDTO result = guitarOrderService.save(guitarOrderDTO);
        return ResponseEntity
            .created(new URI("/api/guitar-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /guitar-orders/:id} : Updates an existing guitarOrder.
     *
     * @param id the id of the guitarOrderDTO to save.
     * @param guitarOrderDTO the guitarOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated guitarOrderDTO,
     * or with status {@code 400 (Bad Request)} if the guitarOrderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the guitarOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/guitar-orders/{id}")
    public ResponseEntity<GuitarOrderDTO> updateGuitarOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GuitarOrderDTO guitarOrderDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GuitarOrder : {}, {}", id, guitarOrderDTO);
        if (guitarOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, guitarOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!guitarOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GuitarOrderDTO result = guitarOrderService.update(guitarOrderDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, guitarOrderDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /guitar-orders/:id} : Partial updates given fields of an existing guitarOrder, field will ignore if it is null
     *
     * @param id the id of the guitarOrderDTO to save.
     * @param guitarOrderDTO the guitarOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated guitarOrderDTO,
     * or with status {@code 400 (Bad Request)} if the guitarOrderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the guitarOrderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the guitarOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/guitar-orders/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GuitarOrderDTO> partialUpdateGuitarOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GuitarOrderDTO guitarOrderDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GuitarOrder partially : {}, {}", id, guitarOrderDTO);
        if (guitarOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, guitarOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!guitarOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GuitarOrderDTO> result = guitarOrderService.partialUpdate(guitarOrderDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, guitarOrderDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /guitar-orders} : get all the guitarOrders.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of guitarOrders in body.
     */
    @GetMapping("/guitar-orders")
    public ResponseEntity<List<GuitarOrderDTO>> getAllGuitarOrders(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of GuitarOrders");
        Page<GuitarOrderDTO> page;
        if (eagerload) {
            page = guitarOrderService.findAllWithEagerRelationships(pageable);
        } else {
            page = guitarOrderService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /guitar-orders/:id} : get the "id" guitarOrder.
     *
     * @param id the id of the guitarOrderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the guitarOrderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/guitar-orders/{id}")
    public ResponseEntity<GuitarOrderDTO> getGuitarOrder(@PathVariable Long id) {
        log.debug("REST request to get GuitarOrder : {}", id);
        Optional<GuitarOrderDTO> guitarOrderDTO = guitarOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(guitarOrderDTO);
    }

    /**
     * {@code DELETE  /guitar-orders/:id} : delete the "id" guitarOrder.
     *
     * @param id the id of the guitarOrderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/guitar-orders/{id}")
    public ResponseEntity<Void> deleteGuitarOrder(@PathVariable Long id) {
        log.debug("REST request to delete GuitarOrder : {}", id);
        guitarOrderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
