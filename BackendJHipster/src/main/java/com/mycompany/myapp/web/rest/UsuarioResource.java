package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Usuario;
import com.mycompany.myapp.repository.UsuarioRepository;
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
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.UsuarioRegistroService;
import com.mycompany.myapp.service.dto.RegistroUsuarioDTO;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Usuario}.
 */
@RestController
@RequestMapping("/api/usuarios")
@Transactional(rollbackFor = Exception.class)
public class UsuarioResource {

    private static final Logger LOG = LoggerFactory.getLogger(UsuarioResource.class);

    private static final String ENTITY_NAME = "usuario";

    @Value("${jhipster.clientApp.name:ejemplo01}")
    private String applicationName;

    private final UsuarioRepository usuarioRepository;

    private final UsuarioRegistroService usuarioRegistroService;

    public UsuarioResource(
    UsuarioRepository usuarioRepository,
    UsuarioRegistroService usuarioRegistroService
) {
    this.usuarioRepository =
        usuarioRepository;

    this.usuarioRegistroService =
        usuarioRegistroService;
}
    
    /**
     * {@code POST  /usuarios} : Create a new usuario.
     *
     * @param usuario the usuario to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new usuario, or with status {@code 400 (Bad Request)} if the usuario already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Usuario> createUsuario(@Valid @RequestBody Usuario usuario) throws URISyntaxException {
        LOG.debug("REST request to save Usuario : {}", usuario);
        if (usuario.getId() != null) {
            throw new BadRequestAlertException("A new usuario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        usuario = usuarioRepository.save(usuario);
        return ResponseEntity.created(new URI("/api/usuarios/" + usuario.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, usuario.getId().toString()))
            .body(usuario);
    }

    /**
     * {@code PUT  /usuarios/:id} : Updates an existing usuario.
     *
     * @param id the id of the usuario to save.
     * @param usuario the usuario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated usuario,
     * or with status {@code 400 (Bad Request)} if the usuario is not valid,
     * or with status {@code 500 (Internal Server Error)} if the usuario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Usuario> updateUsuario(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Usuario usuario
    ) throws URISyntaxException {
        LOG.debug("REST request to update Usuario : {}, {}", id, usuario);
        if (usuario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, usuario.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!usuarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        usuario = usuarioRepository.save(usuario);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, usuario.getId().toString()))
            .body(usuario);
    }

    /**
     * {@code PATCH  /usuarios/:id} : Partial updates given fields of an existing usuario, field will ignore if it is null
     *
     * @param id the id of the usuario to save.
     * @param usuario the usuario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated usuario,
     * or with status {@code 400 (Bad Request)} if the usuario is not valid,
     * or with status {@code 404 (Not Found)} if the usuario is not found,
     * or with status {@code 500 (Internal Server Error)} if the usuario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Usuario> partialUpdateUsuario(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Usuario usuario
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update Usuario : {}, {}", id, usuario);
        if (usuario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, usuario.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!usuarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Usuario> result = usuarioRepository
            .findById(usuario.getId())
            .map(existingUsuario -> {
                updateIfPresent(existingUsuario::setUsername, usuario.getUsername());
                updateIfPresent(existingUsuario::setPassword, usuario.getPassword());
                updateIfPresent(existingUsuario::setCorreo, usuario.getCorreo());
                updateIfPresent(existingUsuario::setEstado, usuario.getEstado());
                updateIfPresent(existingUsuario::setFechaCreacion, usuario.getFechaCreacion());
                updateIfPresent(existingUsuario::setUsuarioCreacion, usuario.getUsuarioCreacion());
                updateIfPresent(existingUsuario::setFechaModificacion, usuario.getFechaModificacion());
                updateIfPresent(existingUsuario::setUsuarioModificacion, usuario.getUsuarioModificacion());

                return existingUsuario;
            })
            .map(usuarioRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, usuario.getId().toString())
        );
    }

    @PostMapping("/registrar")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public ResponseEntity<Usuario> registrarUsuario(
    @Valid @RequestBody RegistroUsuarioDTO dto
) throws URISyntaxException {

    LOG.debug(
        "REST request to registrar Usuario : {}",
        dto.getUsername()
    );

    Usuario usuario =
        usuarioRegistroService
            .registrar(dto);

    return ResponseEntity
        .created(
            new URI(
                "/api/usuarios/" +
                usuario.getId()
            )
        )
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName,
                true,
                ENTITY_NAME,
                usuario.getId().toString()
            )
        )
        .body(usuario);
}

    /**
     * {@code GET  /usuarios} : get all the Usuarios.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Usuarios in body.
     */
    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<Usuario> getAllUsuarios(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        LOG.debug("REST request to get all Usuarios");
        if (eagerload) {
            return usuarioRepository.findAllWithEagerRelationships();
        } else {
            return usuarioRepository.findAll();
        }
    }

    /**
     * {@code GET  /usuarios/:id} : get the "id" usuario.
     *
     * @param id the id of the usuario to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the usuario, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Usuario> getUsuario(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Usuario : {}", id);
        Optional<Usuario> usuario = usuarioRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(usuario);
    }

    /**
     * {@code DELETE  /usuarios/:id} : delete the "id" usuario.
     *
     * @param id the id of the usuario to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUsuario(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Usuario : {}", id);
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }

    @GetMapping("/actual")
public ResponseEntity<Usuario> getUsuarioActual() {

    String login =
        SecurityUtils
            .getCurrentUserLogin()
            .orElse(null);

    if (login == null) {
        return ResponseEntity
            .notFound()
            .build();
    }

    Optional<Usuario> usuario =
        usuarioRepository
            .findOneByUsernameIgnoreCase(
                login
            );

    return ResponseUtil
        .wrapOrNotFound(
            usuario
        );
}
}
