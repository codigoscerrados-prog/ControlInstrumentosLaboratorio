package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Rol;
import com.mycompany.myapp.repository.RolRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Rol}.
 */
@RestController
@RequestMapping("/api/rols")
@Transactional(rollbackFor = Exception.class)
public class RolResource {

    private static final Logger LOG = LoggerFactory.getLogger(RolResource.class);

    private static final String ENTITY_NAME = "rol";

    @Value("${jhipster.clientApp.name:ejemplo01}")
    private String applicationName;

    private final RolRepository rolRepository;

    public RolResource(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    /**
     * {@code POST  /rols} : Create a new rol.
     *
     * @param rol the rol to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rol, or with status {@code 400 (Bad Request)} if the rol already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Rol> createRol(@Valid @RequestBody Rol rol) throws URISyntaxException {
        LOG.debug("REST request to save Rol : {}", rol);
        if (rol.getId() != null) {
            throw new BadRequestAlertException("A new rol cannot already have an ID", ENTITY_NAME, "idexists");
        }
        rol = rolRepository.save(rol);
        return ResponseEntity.created(new URI("/api/rols/" + rol.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, rol.getId().toString()))
            .body(rol);
    }

    /**
     * {@code PUT  /rols/:id} : Updates an existing rol.
     *
     * @param id the id of the rol to save.
     * @param rol the rol to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rol,
     * or with status {@code 400 (Bad Request)} if the rol is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rol couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Rol> updateRol(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Rol rol)
        throws URISyntaxException {
        LOG.debug("REST request to update Rol : {}, {}", id, rol);
        if (rol.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rol.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rolRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        rol = rolRepository.save(rol);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rol.getId().toString()))
            .body(rol);
    }

    /**
     * {@code PATCH  /rols/:id} : Partial updates given fields of an existing rol, field will ignore if it is null
     *
     * @param id the id of the rol to save.
     * @param rol the rol to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rol,
     * or with status {@code 400 (Bad Request)} if the rol is not valid,
     * or with status {@code 404 (Not Found)} if the rol is not found,
     * or with status {@code 500 (Internal Server Error)} if the rol couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Rol> partialUpdateRol(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody Rol rol)
        throws URISyntaxException {
        LOG.debug("REST request to partially update Rol : {}, {}", id, rol);
        if (rol.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rol.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rolRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Rol> result = rolRepository
            .findById(rol.getId())
            .map(existingRol -> {
                updateIfPresent(existingRol::setNombre, rol.getNombre());
                updateIfPresent(existingRol::setDescripcion, rol.getDescripcion());
                updateIfPresent(existingRol::setFechaCreacion, rol.getFechaCreacion());
                updateIfPresent(existingRol::setUsuarioCreacion, rol.getUsuarioCreacion());
                updateIfPresent(existingRol::setFechaModificacion, rol.getFechaModificacion());
                updateIfPresent(existingRol::setUsuarioModificacion, rol.getUsuarioModificacion());

                return existingRol;
            })
            .map(rolRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rol.getId().toString())
        );
    }

    /**
     * {@code GET  /rols} : get all the Rols.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Rols in body.
     */
    @GetMapping("")
    public List<Rol> getAllRols() {
        LOG.debug("REST request to get all Rols");
        return rolRepository.findAll();
    }

    /**
     * {@code GET  /rols/:id} : get the "id" rol.
     *
     * @param id the id of the rol to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rol, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Rol> getRol(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Rol : {}", id);
        Optional<Rol> rol = rolRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(rol);
    }

    /**
     * {@code DELETE  /rols/:id} : delete the "id" rol.
     *
     * @param id the id of the rol to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRol(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Rol : {}", id);
        rolRepository.deleteById(id);
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
