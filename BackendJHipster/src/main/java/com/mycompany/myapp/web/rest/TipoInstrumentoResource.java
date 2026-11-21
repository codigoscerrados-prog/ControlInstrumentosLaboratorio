package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.TipoInstrumento;
import com.mycompany.myapp.repository.TipoInstrumentoRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.TipoInstrumento}.
 */
@RestController
@RequestMapping("/api/tipo-instrumentos")
@Transactional(rollbackFor = Exception.class)
public class TipoInstrumentoResource {

    private static final Logger LOG = LoggerFactory.getLogger(TipoInstrumentoResource.class);

    private static final String ENTITY_NAME = "tipoInstrumento";

    @Value("${jhipster.clientApp.name:ejemplo01}")
    private String applicationName;

    private final TipoInstrumentoRepository tipoInstrumentoRepository;

    public TipoInstrumentoResource(TipoInstrumentoRepository tipoInstrumentoRepository) {
        this.tipoInstrumentoRepository = tipoInstrumentoRepository;
    }

    /**
     * {@code POST  /tipo-instrumentos} : Create a new tipoInstrumento.
     *
     * @param tipoInstrumento the tipoInstrumento to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoInstrumento, or with status {@code 400 (Bad Request)} if the tipoInstrumento already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TipoInstrumento> createTipoInstrumento(@Valid @RequestBody TipoInstrumento tipoInstrumento)
        throws URISyntaxException {
        LOG.debug("REST request to save TipoInstrumento : {}", tipoInstrumento);
        if (tipoInstrumento.getId() != null) {
            throw new BadRequestAlertException("A new tipoInstrumento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tipoInstrumento = tipoInstrumentoRepository.save(tipoInstrumento);
        return ResponseEntity.created(new URI("/api/tipo-instrumentos/" + tipoInstrumento.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, tipoInstrumento.getId().toString()))
            .body(tipoInstrumento);
    }

    /**
     * {@code PUT  /tipo-instrumentos/:id} : Updates an existing tipoInstrumento.
     *
     * @param id the id of the tipoInstrumento to save.
     * @param tipoInstrumento the tipoInstrumento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoInstrumento,
     * or with status {@code 400 (Bad Request)} if the tipoInstrumento is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoInstrumento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TipoInstrumento> updateTipoInstrumento(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TipoInstrumento tipoInstrumento
    ) throws URISyntaxException {
        LOG.debug("REST request to update TipoInstrumento : {}, {}", id, tipoInstrumento);
        if (tipoInstrumento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoInstrumento.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoInstrumentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tipoInstrumento = tipoInstrumentoRepository.save(tipoInstrumento);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoInstrumento.getId().toString()))
            .body(tipoInstrumento);
    }

    /**
     * {@code PATCH  /tipo-instrumentos/:id} : Partial updates given fields of an existing tipoInstrumento, field will ignore if it is null
     *
     * @param id the id of the tipoInstrumento to save.
     * @param tipoInstrumento the tipoInstrumento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoInstrumento,
     * or with status {@code 400 (Bad Request)} if the tipoInstrumento is not valid,
     * or with status {@code 404 (Not Found)} if the tipoInstrumento is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoInstrumento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TipoInstrumento> partialUpdateTipoInstrumento(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TipoInstrumento tipoInstrumento
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update TipoInstrumento : {}, {}", id, tipoInstrumento);
        if (tipoInstrumento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoInstrumento.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoInstrumentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TipoInstrumento> result = tipoInstrumentoRepository
            .findById(tipoInstrumento.getId())
            .map(existingTipoInstrumento -> {
                updateIfPresent(existingTipoInstrumento::setNombre, tipoInstrumento.getNombre());
                updateIfPresent(existingTipoInstrumento::setDescripcion, tipoInstrumento.getDescripcion());
                updateIfPresent(existingTipoInstrumento::setFechaCreacion, tipoInstrumento.getFechaCreacion());
                updateIfPresent(existingTipoInstrumento::setUsuarioCreacion, tipoInstrumento.getUsuarioCreacion());
                updateIfPresent(existingTipoInstrumento::setFechaModificacion, tipoInstrumento.getFechaModificacion());
                updateIfPresent(existingTipoInstrumento::setUsuarioModificacion, tipoInstrumento.getUsuarioModificacion());

                return existingTipoInstrumento;
            })
            .map(tipoInstrumentoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoInstrumento.getId().toString())
        );
    }

    /**
     * {@code GET  /tipo-instrumentos} : get all the Tipo Instrumentos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Tipo Instrumentos in body.
     */
    @GetMapping("")
    public List<TipoInstrumento> getAllTipoInstrumentos() {
        LOG.debug("REST request to get all TipoInstrumentos");
        return tipoInstrumentoRepository.findAll();
    }

    /**
     * {@code GET  /tipo-instrumentos/:id} : get the "id" tipoInstrumento.
     *
     * @param id the id of the tipoInstrumento to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoInstrumento, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoInstrumento> getTipoInstrumento(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TipoInstrumento : {}", id);
        Optional<TipoInstrumento> tipoInstrumento = tipoInstrumentoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tipoInstrumento);
    }

    /**
     * {@code DELETE  /tipo-instrumentos/:id} : delete the "id" tipoInstrumento.
     *
     * @param id the id of the tipoInstrumento to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoInstrumento(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TipoInstrumento : {}", id);
        tipoInstrumentoRepository.deleteById(id);
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
