package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.LaboratorioCalibracion;
import com.mycompany.myapp.repository.LaboratorioCalibracionRepository;
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
import org.springframework.security.access.prepost.PreAuthorize;
/**
 * REST controller for managing {@link com.mycompany.myapp.domain.LaboratorioCalibracion}.
 */
@RestController
@RequestMapping("/api/laboratorio-calibracions")
@Transactional(rollbackFor = Exception.class)
public class LaboratorioCalibracionResource {

    private static final Logger LOG = LoggerFactory.getLogger(LaboratorioCalibracionResource.class);

    private static final String ENTITY_NAME = "laboratorioCalibracion";

    @Value("${jhipster.clientApp.name:ejemplo01}")
    private String applicationName;

    private final LaboratorioCalibracionRepository laboratorioCalibracionRepository;

    public LaboratorioCalibracionResource(LaboratorioCalibracionRepository laboratorioCalibracionRepository) {
        this.laboratorioCalibracionRepository = laboratorioCalibracionRepository;
    }

    /**
     * {@code POST  /laboratorio-calibracions} : Create a new laboratorioCalibracion.
     *
     * @param laboratorioCalibracion the laboratorioCalibracion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new laboratorioCalibracion, or with status {@code 400 (Bad Request)} if the laboratorioCalibracion already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PreAuthorize("@usuarioPermisoService.puedeEditar()")
    public ResponseEntity<LaboratorioCalibracion> createLaboratorioCalibracion(
        @Valid @RequestBody LaboratorioCalibracion laboratorioCalibracion
    ) throws URISyntaxException {
        LOG.debug("REST request to save LaboratorioCalibracion : {}", laboratorioCalibracion);
        if (laboratorioCalibracion.getId() != null) {
            throw new BadRequestAlertException("A new laboratorioCalibracion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        laboratorioCalibracion = laboratorioCalibracionRepository.save(laboratorioCalibracion);
        return ResponseEntity.created(new URI("/api/laboratorio-calibracions/" + laboratorioCalibracion.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, laboratorioCalibracion.getId().toString()))
            .body(laboratorioCalibracion);
    }

    /**
     * {@code PUT  /laboratorio-calibracions/:id} : Updates an existing laboratorioCalibracion.
     *
     * @param id the id of the laboratorioCalibracion to save.
     * @param laboratorioCalibracion the laboratorioCalibracion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated laboratorioCalibracion,
     * or with status {@code 400 (Bad Request)} if the laboratorioCalibracion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the laboratorioCalibracion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @PreAuthorize("@usuarioPermisoService.puedeEditar()")
    public ResponseEntity<LaboratorioCalibracion> updateLaboratorioCalibracion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LaboratorioCalibracion laboratorioCalibracion
    ) throws URISyntaxException {
        LOG.debug("REST request to update LaboratorioCalibracion : {}, {}", id, laboratorioCalibracion);
        if (laboratorioCalibracion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, laboratorioCalibracion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!laboratorioCalibracionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        laboratorioCalibracion = laboratorioCalibracionRepository.save(laboratorioCalibracion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, laboratorioCalibracion.getId().toString()))
            .body(laboratorioCalibracion);
    }

    /**
     * {@code PATCH  /laboratorio-calibracions/:id} : Partial updates given fields of an existing laboratorioCalibracion, field will ignore if it is null
     *
     * @param id the id of the laboratorioCalibracion to save.
     * @param laboratorioCalibracion the laboratorioCalibracion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated laboratorioCalibracion,
     * or with status {@code 400 (Bad Request)} if the laboratorioCalibracion is not valid,
     * or with status {@code 404 (Not Found)} if the laboratorioCalibracion is not found,
     * or with status {@code 500 (Internal Server Error)} if the laboratorioCalibracion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("@usuarioPermisoService.puedeEditar()")
    public ResponseEntity<LaboratorioCalibracion> partialUpdateLaboratorioCalibracion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LaboratorioCalibracion laboratorioCalibracion
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update LaboratorioCalibracion : {}, {}", id, laboratorioCalibracion);
        if (laboratorioCalibracion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, laboratorioCalibracion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!laboratorioCalibracionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LaboratorioCalibracion> result = laboratorioCalibracionRepository
            .findById(laboratorioCalibracion.getId())
            .map(existingLaboratorioCalibracion -> {
                updateIfPresent(existingLaboratorioCalibracion::setNombre, laboratorioCalibracion.getNombre());
                updateIfPresent(existingLaboratorioCalibracion::setRuc, laboratorioCalibracion.getRuc());
                updateIfPresent(existingLaboratorioCalibracion::setDireccion, laboratorioCalibracion.getDireccion());
                updateIfPresent(existingLaboratorioCalibracion::setTelefono, laboratorioCalibracion.getTelefono());
                updateIfPresent(existingLaboratorioCalibracion::setCorreo, laboratorioCalibracion.getCorreo());
                updateIfPresent(existingLaboratorioCalibracion::setEstado, laboratorioCalibracion.getEstado());
                updateIfPresent(existingLaboratorioCalibracion::setFechaCreacion, laboratorioCalibracion.getFechaCreacion());
                updateIfPresent(existingLaboratorioCalibracion::setUsuarioCreacion, laboratorioCalibracion.getUsuarioCreacion());
                updateIfPresent(existingLaboratorioCalibracion::setFechaModificacion, laboratorioCalibracion.getFechaModificacion());
                updateIfPresent(existingLaboratorioCalibracion::setUsuarioModificacion, laboratorioCalibracion.getUsuarioModificacion());

                return existingLaboratorioCalibracion;
            })
            .map(laboratorioCalibracionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, laboratorioCalibracion.getId().toString())
        );
    }

    /**
     * {@code GET  /laboratorio-calibracions} : get all the Laboratorio Calibracions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Laboratorio Calibracions in body.
     */
    @GetMapping("")
 
    public List<LaboratorioCalibracion> getAllLaboratorioCalibracions() {
        LOG.debug("REST request to get all LaboratorioCalibracions");
        return laboratorioCalibracionRepository.findAll();
    }

    /**
     * {@code GET  /laboratorio-calibracions/:id} : get the "id" laboratorioCalibracion.
     *
     * @param id the id of the laboratorioCalibracion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the laboratorioCalibracion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
   
    public ResponseEntity<LaboratorioCalibracion> getLaboratorioCalibracion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get LaboratorioCalibracion : {}", id);
        Optional<LaboratorioCalibracion> laboratorioCalibracion = laboratorioCalibracionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(laboratorioCalibracion);
    }

    /**
     * {@code DELETE  /laboratorio-calibracions/:id} : delete the "id" laboratorioCalibracion.
     *
     * @param id the id of the laboratorioCalibracion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@usuarioPermisoService.puedeEditar()")
    public ResponseEntity<Void> deleteLaboratorioCalibracion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete LaboratorioCalibracion : {}", id);
        laboratorioCalibracionRepository.deleteById(id);
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
