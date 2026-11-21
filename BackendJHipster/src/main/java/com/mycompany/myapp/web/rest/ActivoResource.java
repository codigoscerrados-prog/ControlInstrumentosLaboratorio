package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Activo;
import com.mycompany.myapp.repository.ActivoRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Activo}.
 */
@RestController
@RequestMapping("/api/activos")
@Transactional(rollbackFor = Exception.class)
public class ActivoResource {

    private static final Logger LOG = LoggerFactory.getLogger(ActivoResource.class);

    private static final String ENTITY_NAME = "activo";

    @Value("${jhipster.clientApp.name:ejemplo01}")
    private String applicationName;

    private final ActivoRepository activoRepository;

    public ActivoResource(ActivoRepository activoRepository) {
        this.activoRepository = activoRepository;
    }

    /**
     * {@code POST  /activos} : Create a new activo.
     *
     * @param activo the activo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new activo, or with status {@code 400 (Bad Request)} if the activo already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Activo> createActivo(@Valid @RequestBody Activo activo) throws URISyntaxException {
        LOG.debug("REST request to save Activo : {}", activo);
        if (activo.getId() != null) {
            throw new BadRequestAlertException("A new activo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        activo = activoRepository.save(activo);
        return ResponseEntity.created(new URI("/api/activos/" + activo.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, activo.getId().toString()))
            .body(activo);
    }

    /**
     * {@code PUT  /activos/:id} : Updates an existing activo.
     *
     * @param id the id of the activo to save.
     * @param activo the activo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activo,
     * or with status {@code 400 (Bad Request)} if the activo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the activo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Activo> updateActivo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Activo activo
    ) throws URISyntaxException {
        LOG.debug("REST request to update Activo : {}, {}", id, activo);
        if (activo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, activo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!activoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        activo = activoRepository.save(activo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, activo.getId().toString()))
            .body(activo);
    }

    /**
     * {@code PATCH  /activos/:id} : Partial updates given fields of an existing activo, field will ignore if it is null
     *
     * @param id the id of the activo to save.
     * @param activo the activo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activo,
     * or with status {@code 400 (Bad Request)} if the activo is not valid,
     * or with status {@code 404 (Not Found)} if the activo is not found,
     * or with status {@code 500 (Internal Server Error)} if the activo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Activo> partialUpdateActivo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Activo activo
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update Activo : {}, {}", id, activo);
        if (activo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, activo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!activoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Activo> result = activoRepository
            .findById(activo.getId())
            .map(existingActivo -> {
                updateIfPresent(existingActivo::setCodigo, activo.getCodigo());
                updateIfPresent(existingActivo::setNombre, activo.getNombre());
                updateIfPresent(existingActivo::setMarca, activo.getMarca());
                updateIfPresent(existingActivo::setModelo, activo.getModelo());
                updateIfPresent(existingActivo::setNumeroSerie, activo.getNumeroSerie());
                updateIfPresent(existingActivo::setRango, activo.getRango());
                updateIfPresent(existingActivo::setResolucion, activo.getResolucion());
                updateIfPresent(existingActivo::setUbicacion, activo.getUbicacion());
                updateIfPresent(existingActivo::setEstado, activo.getEstado());
                updateIfPresent(existingActivo::setObservaciones, activo.getObservaciones());
                updateIfPresent(existingActivo::setFechaCreacion, activo.getFechaCreacion());
                updateIfPresent(existingActivo::setUsuarioCreacion, activo.getUsuarioCreacion());
                updateIfPresent(existingActivo::setFechaModificacion, activo.getFechaModificacion());
                updateIfPresent(existingActivo::setUsuarioModificacion, activo.getUsuarioModificacion());

                return existingActivo;
            })
            .map(activoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, activo.getId().toString())
        );
    }

    /**
     * {@code GET  /activos} : get all the Activos.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Activos in body.
     */
    @GetMapping("")
    public List<Activo> getAllActivos(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        LOG.debug("REST request to get all Activos");
        if (eagerload) {
            return activoRepository.findAllWithEagerRelationships();
        } else {
            return activoRepository.findAll();
        }
    }

    /**
     * {@code GET  /activos/:id} : get the "id" activo.
     *
     * @param id the id of the activo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the activo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Activo> getActivo(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Activo : {}", id);
        Optional<Activo> activo = activoRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(activo);
    }

    /**
     * {@code DELETE  /activos/:id} : delete the "id" activo.
     *
     * @param id the id of the activo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivo(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Activo : {}", id);
        activoRepository.deleteById(id);
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
