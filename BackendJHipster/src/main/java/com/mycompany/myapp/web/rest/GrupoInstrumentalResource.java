package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.GrupoInstrumental;
import com.mycompany.myapp.repository.GrupoInstrumentalRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.GrupoInstrumental}.
 */
@RestController
@RequestMapping("/api/grupo-instrumentals")
@Transactional(rollbackFor = Exception.class)
public class GrupoInstrumentalResource {

    private static final Logger LOG = LoggerFactory.getLogger(GrupoInstrumentalResource.class);

    private static final String ENTITY_NAME = "grupoInstrumental";

    @Value("${jhipster.clientApp.name:ejemplo01}")
    private String applicationName;

    private final GrupoInstrumentalRepository grupoInstrumentalRepository;

    public GrupoInstrumentalResource(GrupoInstrumentalRepository grupoInstrumentalRepository) {
        this.grupoInstrumentalRepository = grupoInstrumentalRepository;
    }

    /**
     * {@code POST  /grupo-instrumentals} : Create a new grupoInstrumental.
     *
     * @param grupoInstrumental the grupoInstrumental to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new grupoInstrumental, or with status {@code 400 (Bad Request)} if the grupoInstrumental already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<GrupoInstrumental> createGrupoInstrumental(@Valid @RequestBody GrupoInstrumental grupoInstrumental)
        throws URISyntaxException {
        LOG.debug("REST request to save GrupoInstrumental : {}", grupoInstrumental);
        if (grupoInstrumental.getId() != null) {
            throw new BadRequestAlertException("A new grupoInstrumental cannot already have an ID", ENTITY_NAME, "idexists");
        }
        grupoInstrumental = grupoInstrumentalRepository.save(grupoInstrumental);
        return ResponseEntity.created(new URI("/api/grupo-instrumentals/" + grupoInstrumental.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, grupoInstrumental.getId().toString()))
            .body(grupoInstrumental);
    }

    /**
     * {@code PUT  /grupo-instrumentals/:id} : Updates an existing grupoInstrumental.
     *
     * @param id the id of the grupoInstrumental to save.
     * @param grupoInstrumental the grupoInstrumental to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated grupoInstrumental,
     * or with status {@code 400 (Bad Request)} if the grupoInstrumental is not valid,
     * or with status {@code 500 (Internal Server Error)} if the grupoInstrumental couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GrupoInstrumental> updateGrupoInstrumental(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GrupoInstrumental grupoInstrumental
    ) throws URISyntaxException {
        LOG.debug("REST request to update GrupoInstrumental : {}, {}", id, grupoInstrumental);
        if (grupoInstrumental.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, grupoInstrumental.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!grupoInstrumentalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        grupoInstrumental = grupoInstrumentalRepository.save(grupoInstrumental);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, grupoInstrumental.getId().toString()))
            .body(grupoInstrumental);
    }

    /**
     * {@code PATCH  /grupo-instrumentals/:id} : Partial updates given fields of an existing grupoInstrumental, field will ignore if it is null
     *
     * @param id the id of the grupoInstrumental to save.
     * @param grupoInstrumental the grupoInstrumental to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated grupoInstrumental,
     * or with status {@code 400 (Bad Request)} if the grupoInstrumental is not valid,
     * or with status {@code 404 (Not Found)} if the grupoInstrumental is not found,
     * or with status {@code 500 (Internal Server Error)} if the grupoInstrumental couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GrupoInstrumental> partialUpdateGrupoInstrumental(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GrupoInstrumental grupoInstrumental
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update GrupoInstrumental : {}, {}", id, grupoInstrumental);
        if (grupoInstrumental.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, grupoInstrumental.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!grupoInstrumentalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GrupoInstrumental> result = grupoInstrumentalRepository
            .findById(grupoInstrumental.getId())
            .map(existingGrupoInstrumental -> {
                updateIfPresent(existingGrupoInstrumental::setCodigo, grupoInstrumental.getCodigo());
                updateIfPresent(existingGrupoInstrumental::setNombre, grupoInstrumental.getNombre());
                updateIfPresent(existingGrupoInstrumental::setDescripcion, grupoInstrumental.getDescripcion());
                updateIfPresent(existingGrupoInstrumental::setFechaCreacion, grupoInstrumental.getFechaCreacion());
                updateIfPresent(existingGrupoInstrumental::setUsuarioCreacion, grupoInstrumental.getUsuarioCreacion());
                updateIfPresent(existingGrupoInstrumental::setFechaModificacion, grupoInstrumental.getFechaModificacion());
                updateIfPresent(existingGrupoInstrumental::setUsuarioModificacion, grupoInstrumental.getUsuarioModificacion());

                return existingGrupoInstrumental;
            })
            .map(grupoInstrumentalRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, grupoInstrumental.getId().toString())
        );
    }

    /**
     * {@code GET  /grupo-instrumentals} : get all the Grupo Instrumentals.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Grupo Instrumentals in body.
     */
    @GetMapping("")
    public List<GrupoInstrumental> getAllGrupoInstrumentals() {
        LOG.debug("REST request to get all GrupoInstrumentals");
        return grupoInstrumentalRepository.findAll();
    }

    /**
     * {@code GET  /grupo-instrumentals/:id} : get the "id" grupoInstrumental.
     *
     * @param id the id of the grupoInstrumental to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the grupoInstrumental, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GrupoInstrumental> getGrupoInstrumental(@PathVariable("id") Long id) {
        LOG.debug("REST request to get GrupoInstrumental : {}", id);
        Optional<GrupoInstrumental> grupoInstrumental = grupoInstrumentalRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(grupoInstrumental);
    }

    /**
     * {@code DELETE  /grupo-instrumentals/:id} : delete the "id" grupoInstrumental.
     *
     * @param id the id of the grupoInstrumental to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrupoInstrumental(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete GrupoInstrumental : {}", id);
        grupoInstrumentalRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
