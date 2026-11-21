package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.RecuperacionPassword;
import com.mycompany.myapp.repository.RecuperacionPasswordRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.RecuperacionPassword}.
 */
@RestController
@RequestMapping("/api/recuperacion-passwords")
@Transactional(rollbackFor = Exception.class)
public class RecuperacionPasswordResource {

    private static final Logger LOG = LoggerFactory.getLogger(RecuperacionPasswordResource.class);

    private static final String ENTITY_NAME = "recuperacionPassword";

    @Value("${jhipster.clientApp.name:ejemplo01}")
    private String applicationName;

    private final RecuperacionPasswordRepository recuperacionPasswordRepository;

    public RecuperacionPasswordResource(RecuperacionPasswordRepository recuperacionPasswordRepository) {
        this.recuperacionPasswordRepository = recuperacionPasswordRepository;
    }

    /**
     * {@code POST  /recuperacion-passwords} : Create a new recuperacionPassword.
     *
     * @param recuperacionPassword the recuperacionPassword to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recuperacionPassword, or with status {@code 400 (Bad Request)} if the recuperacionPassword already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RecuperacionPassword> createRecuperacionPassword(@Valid @RequestBody RecuperacionPassword recuperacionPassword)
        throws URISyntaxException {
        LOG.debug("REST request to save RecuperacionPassword : {}", recuperacionPassword);
        if (recuperacionPassword.getId() != null) {
            throw new BadRequestAlertException("A new recuperacionPassword cannot already have an ID", ENTITY_NAME, "idexists");
        }
        recuperacionPassword = recuperacionPasswordRepository.save(recuperacionPassword);
        return ResponseEntity.created(new URI("/api/recuperacion-passwords/" + recuperacionPassword.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, recuperacionPassword.getId().toString()))
            .body(recuperacionPassword);
    }

    /**
     * {@code PUT  /recuperacion-passwords/:id} : Updates an existing recuperacionPassword.
     *
     * @param id the id of the recuperacionPassword to save.
     * @param recuperacionPassword the recuperacionPassword to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recuperacionPassword,
     * or with status {@code 400 (Bad Request)} if the recuperacionPassword is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recuperacionPassword couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RecuperacionPassword> updateRecuperacionPassword(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RecuperacionPassword recuperacionPassword
    ) throws URISyntaxException {
        LOG.debug("REST request to update RecuperacionPassword : {}, {}", id, recuperacionPassword);
        if (recuperacionPassword.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recuperacionPassword.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recuperacionPasswordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        recuperacionPassword = recuperacionPasswordRepository.save(recuperacionPassword);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recuperacionPassword.getId().toString()))
            .body(recuperacionPassword);
    }

    /**
     * {@code PATCH  /recuperacion-passwords/:id} : Partial updates given fields of an existing recuperacionPassword, field will ignore if it is null
     *
     * @param id the id of the recuperacionPassword to save.
     * @param recuperacionPassword the recuperacionPassword to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recuperacionPassword,
     * or with status {@code 400 (Bad Request)} if the recuperacionPassword is not valid,
     * or with status {@code 404 (Not Found)} if the recuperacionPassword is not found,
     * or with status {@code 500 (Internal Server Error)} if the recuperacionPassword couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RecuperacionPassword> partialUpdateRecuperacionPassword(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RecuperacionPassword recuperacionPassword
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update RecuperacionPassword : {}, {}", id, recuperacionPassword);
        if (recuperacionPassword.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recuperacionPassword.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recuperacionPasswordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RecuperacionPassword> result = recuperacionPasswordRepository
            .findById(recuperacionPassword.getId())
            .map(existingRecuperacionPassword -> {
                updateIfPresent(existingRecuperacionPassword::setToken, recuperacionPassword.getToken());
                updateIfPresent(existingRecuperacionPassword::setFechaSolicitud, recuperacionPassword.getFechaSolicitud());
                updateIfPresent(existingRecuperacionPassword::setFechaExpiracion, recuperacionPassword.getFechaExpiracion());
                updateIfPresent(existingRecuperacionPassword::setUsado, recuperacionPassword.getUsado());

                return existingRecuperacionPassword;
            })
            .map(recuperacionPasswordRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recuperacionPassword.getId().toString())
        );
    }

    /**
     * {@code GET  /recuperacion-passwords} : get all the Recuperacion Passwords.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Recuperacion Passwords in body.
     */
    @GetMapping("")
    public List<RecuperacionPassword> getAllRecuperacionPasswords(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all RecuperacionPasswords");
        if (eagerload) {
            return recuperacionPasswordRepository.findAllWithEagerRelationships();
        } else {
            return recuperacionPasswordRepository.findAll();
        }
    }

    /**
     * {@code GET  /recuperacion-passwords/:id} : get the "id" recuperacionPassword.
     *
     * @param id the id of the recuperacionPassword to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recuperacionPassword, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecuperacionPassword> getRecuperacionPassword(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RecuperacionPassword : {}", id);
        Optional<RecuperacionPassword> recuperacionPassword = recuperacionPasswordRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(recuperacionPassword);
    }

    /**
     * {@code DELETE  /recuperacion-passwords/:id} : delete the "id" recuperacionPassword.
     *
     * @param id the id of the recuperacionPassword to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecuperacionPassword(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RecuperacionPassword : {}", id);
        recuperacionPasswordRepository.deleteById(id);
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
