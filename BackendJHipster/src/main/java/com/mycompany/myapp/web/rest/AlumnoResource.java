package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Alumno;
import com.mycompany.myapp.repository.AlumnoRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Alumno}.
 */
@RestController
@RequestMapping("/api/alumnos")
@Transactional(rollbackFor = Exception.class)
public class AlumnoResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlumnoResource.class);

    private static final String ENTITY_NAME = "alumno";

    @Value("${jhipster.clientApp.name:ejemplo01}")
    private String applicationName;

    private final AlumnoRepository alumnoRepository;

    public AlumnoResource(AlumnoRepository alumnoRepository) {
        this.alumnoRepository = alumnoRepository;
    }

    /**
     * {@code POST  /alumnos} : Create a new alumno.
     *
     * @param alumno the alumno to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alumno, or with status {@code 400 (Bad Request)} if the alumno already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Alumno> createAlumno(@RequestBody Alumno alumno) throws URISyntaxException {
        LOG.debug("REST request to save Alumno : {}", alumno);
        if (alumno.getId() != null) {
            throw new BadRequestAlertException("A new alumno cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alumno = alumnoRepository.save(alumno);
        return ResponseEntity.created(new URI("/api/alumnos/" + alumno.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alumno.getId().toString()))
            .body(alumno);
    }

    /**
     * {@code PUT  /alumnos/:id} : Updates an existing alumno.
     *
     * @param id the id of the alumno to save.
     * @param alumno the alumno to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alumno,
     * or with status {@code 400 (Bad Request)} if the alumno is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alumno couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Alumno> updateAlumno(@PathVariable(value = "id", required = false) final Long id, @RequestBody Alumno alumno)
        throws URISyntaxException {
        LOG.debug("REST request to update Alumno : {}, {}", id, alumno);
        if (alumno.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alumno.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alumnoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alumno = alumnoRepository.save(alumno);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alumno.getId().toString()))
            .body(alumno);
    }

    /**
     * {@code PATCH  /alumnos/:id} : Partial updates given fields of an existing alumno, field will ignore if it is null
     *
     * @param id the id of the alumno to save.
     * @param alumno the alumno to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alumno,
     * or with status {@code 400 (Bad Request)} if the alumno is not valid,
     * or with status {@code 404 (Not Found)} if the alumno is not found,
     * or with status {@code 500 (Internal Server Error)} if the alumno couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Alumno> partialUpdateAlumno(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Alumno alumno
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update Alumno : {}, {}", id, alumno);
        if (alumno.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alumno.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alumnoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Alumno> result = alumnoRepository
            .findById(alumno.getId())
            .map(existingAlumno -> {
                updateIfPresent(existingAlumno::setCodigoAlumno, alumno.getCodigoAlumno());
                updateIfPresent(existingAlumno::setApellidoP, alumno.getApellidoP());
                updateIfPresent(existingAlumno::setApellidoM, alumno.getApellidoM());
                updateIfPresent(existingAlumno::setNombre, alumno.getNombre());
                updateIfPresent(existingAlumno::setUsuarioCreacion, alumno.getUsuarioCreacion());
                updateIfPresent(existingAlumno::setFechaCreacion, alumno.getFechaCreacion());
                updateIfPresent(existingAlumno::setUsuarioModificacion, alumno.getUsuarioModificacion());
                updateIfPresent(existingAlumno::setFechaModificacion, alumno.getFechaModificacion());

                return existingAlumno;
            })
            .map(alumnoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alumno.getId().toString())
        );
    }

    /**
     * {@code GET  /alumnos} : get all the Alumnos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Alumnos in body.
     */
    @GetMapping("")
    public List<Alumno> getAllAlumnos() {
        LOG.debug("REST request to get all Alumnos");
        return alumnoRepository.findAll();
    }

    /**
     * {@code GET  /alumnos/:id} : get the "id" alumno.
     *
     * @param id the id of the alumno to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alumno, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Alumno> getAlumno(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Alumno : {}", id);
        Optional<Alumno> alumno = alumnoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(alumno);
    }

    /**
     * {@code DELETE  /alumnos/:id} : delete the "id" alumno.
     *
     * @param id the id of the alumno to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlumno(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Alumno : {}", id);
        alumnoRepository.deleteById(id);
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
