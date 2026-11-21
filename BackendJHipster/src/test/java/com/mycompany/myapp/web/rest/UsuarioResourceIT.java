package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.UsuarioAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Rol;
import com.mycompany.myapp.domain.Usuario;
import com.mycompany.myapp.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link UsuarioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UsuarioResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_CORREO = "AAAAAAAAAA";
    private static final String UPDATED_CORREO = "BBBBBBBBBB";

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.ofEpochMilli(1790641792953L);

    private static final String DEFAULT_USUARIO_CREACION = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_CREACION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_MODIFICACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_MODIFICACION = Instant.ofEpochMilli(1790641792953L);

    private static final String DEFAULT_USUARIO_MODIFICACION = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_MODIFICACION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/usuarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioRepository usuarioRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUsuarioMockMvc;

    private Usuario usuario;

    private Usuario insertedUsuario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usuario createEntity(EntityManager em) {
        Usuario usuario = new Usuario()
            .username(DEFAULT_USERNAME)
            .password(DEFAULT_PASSWORD)
            .correo(DEFAULT_CORREO)
            .estado(DEFAULT_ESTADO)
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .usuarioCreacion(DEFAULT_USUARIO_CREACION)
            .fechaModificacion(DEFAULT_FECHA_MODIFICACION)
            .usuarioModificacion(DEFAULT_USUARIO_MODIFICACION);
        // Add required entity
        Rol rol;
        if (TestUtil.findAll(em, Rol.class).isEmpty()) {
            rol = RolResourceIT.createEntity();
            em.persist(rol);
            em.flush();
        } else {
            rol = TestUtil.findAll(em, Rol.class).getFirst();
        }
        usuario.setRol(rol);
        return usuario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usuario createUpdatedEntity(EntityManager em) {
        Usuario updatedUsuario = new Usuario()
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .correo(UPDATED_CORREO)
            .estado(UPDATED_ESTADO)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION);
        // Add required entity
        Rol rol;
        if (TestUtil.findAll(em, Rol.class).isEmpty()) {
            rol = RolResourceIT.createUpdatedEntity();
            em.persist(rol);
            em.flush();
        } else {
            rol = TestUtil.findAll(em, Rol.class).getFirst();
        }
        updatedUsuario.setRol(rol);
        return updatedUsuario;
    }

    @BeforeEach
    void initTest() {
        usuario = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedUsuario != null) {
            usuarioRepository.delete(insertedUsuario);
            insertedUsuario = null;
        }
    }

    @Test
    @Transactional
    void createUsuario() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Usuario
        var returnedUsuario = om.readValue(
            restUsuarioMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuario)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Usuario.class
        );

        // Validate the Usuario in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUsuarioUpdatableFieldsEquals(returnedUsuario, getPersistedUsuario(returnedUsuario));

        insertedUsuario = returnedUsuario;
    }

    @Test
    @Transactional
    void createUsuarioWithExistingId() throws Exception {
        // Create the Usuario with an existing ID
        usuario.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuario)))
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUsernameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        usuario.setUsername(null);

        // Create the Usuario, which fails.

        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuario)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        usuario.setPassword(null);

        // Create the Usuario, which fails.

        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuario)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCorreoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        usuario.setCorreo(null);

        // Create the Usuario, which fails.

        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuario)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        usuario.setEstado(null);

        // Create the Usuario, which fails.

        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuario)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUsuarios() throws Exception {
        // Initialize the database
        insertedUsuario = usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].correo").value(hasItem(DEFAULT_CORREO)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].usuarioCreacion").value(hasItem(DEFAULT_USUARIO_CREACION)))
            .andExpect(jsonPath("$.[*].fechaModificacion").value(hasItem(DEFAULT_FECHA_MODIFICACION.toString())))
            .andExpect(jsonPath("$.[*].usuarioModificacion").value(hasItem(DEFAULT_USUARIO_MODIFICACION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUsuariosWithEagerRelationshipsIsEnabled() throws Exception {
        when(usuarioRepositoryMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restUsuarioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(usuarioRepositoryMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUsuariosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(usuarioRepositoryMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restUsuarioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(usuarioRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUsuario() throws Exception {
        // Initialize the database
        insertedUsuario = usuarioRepository.saveAndFlush(usuario);

        // Get the usuario
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL_ID, usuario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(usuario.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.correo").value(DEFAULT_CORREO))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.usuarioCreacion").value(DEFAULT_USUARIO_CREACION))
            .andExpect(jsonPath("$.fechaModificacion").value(DEFAULT_FECHA_MODIFICACION.toString()))
            .andExpect(jsonPath("$.usuarioModificacion").value(DEFAULT_USUARIO_MODIFICACION));
    }

    @Test
    @Transactional
    void getNonExistingUsuario() throws Exception {
        // Get the usuario
        restUsuarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUsuario() throws Exception {
        // Initialize the database
        insertedUsuario = usuarioRepository.saveAndFlush(usuario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the usuario
        Usuario updatedUsuario = usuarioRepository.findById(usuario.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUsuario are not directly saved in db
        em.detach(updatedUsuario);
        updatedUsuario
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .correo(UPDATED_CORREO)
            .estado(UPDATED_ESTADO)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION);

        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUsuario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUsuario))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUsuarioToMatchAllProperties(updatedUsuario);
    }

    @Test
    @Transactional
    void putNonExistingUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuario.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(put(ENTITY_API_URL_ID, usuario.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuario)))
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuario.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(usuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuario.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuario)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Usuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUsuarioWithPatch() throws Exception {
        // Initialize the database
        insertedUsuario = usuarioRepository.saveAndFlush(usuario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the usuario using partial update
        Usuario partialUpdatedUsuario = new Usuario();
        partialUpdatedUsuario.setId(usuario.getId());

        partialUpdatedUsuario
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .correo(UPDATED_CORREO)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION);

        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUsuario))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUsuarioUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedUsuario, usuario), getPersistedUsuario(usuario));
    }

    @Test
    @Transactional
    void fullUpdateUsuarioWithPatch() throws Exception {
        // Initialize the database
        insertedUsuario = usuarioRepository.saveAndFlush(usuario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the usuario using partial update
        Usuario partialUpdatedUsuario = new Usuario();
        partialUpdatedUsuario.setId(usuario.getId());

        partialUpdatedUsuario
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .correo(UPDATED_CORREO)
            .estado(UPDATED_ESTADO)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION);

        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUsuario))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUsuarioUpdatableFieldsEquals(partialUpdatedUsuario, getPersistedUsuario(partialUpdatedUsuario));
    }

    @Test
    @Transactional
    void patchNonExistingUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuario.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, usuario.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(usuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuario.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(usuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuario.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(usuario)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Usuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUsuario() throws Exception {
        // Initialize the database
        insertedUsuario = usuarioRepository.saveAndFlush(usuario);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the usuario
        restUsuarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, usuario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return usuarioRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Usuario getPersistedUsuario(Usuario usuario) {
        return usuarioRepository.findById(usuario.getId()).orElseThrow();
    }

    protected void assertPersistedUsuarioToMatchAllProperties(Usuario expectedUsuario) {
        assertUsuarioAllPropertiesEquals(expectedUsuario, getPersistedUsuario(expectedUsuario));
    }

    protected void assertPersistedUsuarioToMatchUpdatableProperties(Usuario expectedUsuario) {
        assertUsuarioAllUpdatablePropertiesEquals(expectedUsuario, getPersistedUsuario(expectedUsuario));
    }
}
