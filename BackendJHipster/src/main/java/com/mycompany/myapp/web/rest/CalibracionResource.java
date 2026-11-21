package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Calibracion;
import com.mycompany.myapp.repository.CalibracionRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Calibracion}.
 */
@RestController
@RequestMapping("/api/calibracions")
@Transactional(rollbackFor = Exception.class)
public class CalibracionResource {

    private static final Logger LOG = LoggerFactory.getLogger(CalibracionResource.class);

    private static final String ENTITY_NAME = "calibracion";

    @Value("${jhipster.clientApp.name:ejemplo01}")
    private String applicationName;

    private final CalibracionRepository calibracionRepository;

    public CalibracionResource(CalibracionRepository calibracionRepository) {
        this.calibracionRepository = calibracionRepository;
    }

    /**
     * {@code POST  /calibracions} : Create a new calibracion.
     *
     * @param calibracion the calibracion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new calibracion, or with status {@code 400 (Bad Request)} if the calibracion already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Calibracion> createCalibracion(@Valid @RequestBody Calibracion calibracion) throws URISyntaxException {
        LOG.debug("REST request to save Calibracion : {}", calibracion);
        if (calibracion.getId() != null) {
            throw new BadRequestAlertException("A new calibracion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        calibracion = calibracionRepository.save(calibracion);
        return ResponseEntity.created(new URI("/api/calibracions/" + calibracion.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, calibracion.getId().toString()))
            .body(calibracion);
    }

    /**
     * {@code PUT  /calibracions/:id} : Updates an existing calibracion.
     *
     * @param id the id of the calibracion to save.
     * @param calibracion the calibracion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated calibracion,
     * or with status {@code 400 (Bad Request)} if the calibracion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the calibracion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Calibracion> updateCalibracion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Calibracion calibracion
    ) throws URISyntaxException {
        LOG.debug("REST request to update Calibracion : {}, {}", id, calibracion);
        if (calibracion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, calibracion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!calibracionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        calibracion = calibracionRepository.save(calibracion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, calibracion.getId().toString()))
            .body(calibracion);
    }

    /**
     * {@code PATCH  /calibracions/:id} : Partial updates given fields of an existing calibracion, field will ignore if it is null
     *
     * @param id the id of the calibracion to save.
     * @param calibracion the calibracion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated calibracion,
     * or with status {@code 400 (Bad Request)} if the calibracion is not valid,
     * or with status {@code 404 (Not Found)} if the calibracion is not found,
     * or with status {@code 500 (Internal Server Error)} if the calibracion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Calibracion> partialUpdateCalibracion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Calibracion calibracion
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update Calibracion : {}, {}", id, calibracion);
        if (calibracion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, calibracion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!calibracionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Calibracion> result = calibracionRepository
            .findById(calibracion.getId())
            .map(existingCalibracion -> {
                updateIfPresent(existingCalibracion::setFechaEnvio, calibracion.getFechaEnvio());
                updateIfPresent(existingCalibracion::setFechaCalibracion, calibracion.getFechaCalibracion());
                updateIfPresent(existingCalibracion::setFechaRetorno, calibracion.getFechaRetorno());
                updateIfPresent(existingCalibracion::setFechaVencimiento, calibracion.getFechaVencimiento());
                updateIfPresent(existingCalibracion::setNumeroCertificado, calibracion.getNumeroCertificado());
                updateIfPresent(existingCalibracion::setResultado, calibracion.getResultado());
                updateIfPresent(existingCalibracion::setObservaciones, calibracion.getObservaciones());
                updateIfPresent(existingCalibracion::setFechaCreacion, calibracion.getFechaCreacion());
                updateIfPresent(existingCalibracion::setUsuarioCreacion, calibracion.getUsuarioCreacion());
                updateIfPresent(existingCalibracion::setFechaModificacion, calibracion.getFechaModificacion());
                updateIfPresent(existingCalibracion::setUsuarioModificacion, calibracion.getUsuarioModificacion());

                return existingCalibracion;
            })
            .map(calibracionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, calibracion.getId().toString())
        );
    }

    /**
     * {@code GET  /calibracions} : get all the Calibracions.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Calibracions in body.
     */
    @GetMapping("")
    public List<Calibracion> getAllCalibracions(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all Calibracions");
        if (eagerload) {
            return calibracionRepository.findAllWithEagerRelationships();
        } else {
            return calibracionRepository.findAll();
        }
    }

    /**
     * {@code GET  /calibracions/:id} : get the "id" calibracion.
     *
     * @param id the id of the calibracion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the calibracion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Calibracion> getCalibracion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Calibracion : {}", id);
        Optional<Calibracion> calibracion = calibracionRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(calibracion);
    }

    /**
     * {@code DELETE  /calibracions/:id} : delete the "id" calibracion.
     *
     * @param id the id of the calibracion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalibracion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Calibracion : {}", id);
        calibracionRepository.deleteById(id);
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
