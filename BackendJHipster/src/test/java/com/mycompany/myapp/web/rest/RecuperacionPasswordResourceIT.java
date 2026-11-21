package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.RecuperacionPasswordAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.RecuperacionPassword;
import com.mycompany.myapp.domain.Usuario;
import com.mycompany.myapp.repository.RecuperacionPasswordRepository;
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
 * Integration tests for the {@link RecuperacionPasswordResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RecuperacionPasswordResourceIT {

    private static final String DEFAULT_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_SOLICITUD = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_SOLICITUD = Instant.ofEpochMilli(1790641792953L);

    private static final Instant DEFAULT_FECHA_EXPIRACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_EXPIRACION = Instant.ofEpochMilli(1790641792953L);

    private static final Boolean DEFAULT_USADO = false;
    private static final Boolean UPDATED_USADO = true;

    private static final String ENTITY_API_URL = "/api/recuperacion-passwords";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RecuperacionPasswordRepository recuperacionPasswordRepository;

    @Mock
    private RecuperacionPasswordRepository recuperacionPasswordRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecuperacionPasswordMockMvc;

    private RecuperacionPassword recuperacionPassword;

    private RecuperacionPassword insertedRecuperacionPassword;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecuperacionPassword createEntity(EntityManager em) {
        RecuperacionPassword recuperacionPassword = new RecuperacionPassword()
            .token(DEFAULT_TOKEN)
            .fechaSolicitud(DEFAULT_FECHA_SOLICITUD)
            .fechaExpiracion(DEFAULT_FECHA_EXPIRACION)
            .usado(DEFAULT_USADO);
        // Add required entity
        Usuario usuario;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            usuario = UsuarioResourceIT.createEntity(em);
            em.persist(usuario);
            em.flush();
        } else {
            usuario = TestUtil.findAll(em, Usuario.class).getFirst();
        }
        recuperacionPassword.setUsuario(usuario);
        return recuperacionPassword;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecuperacionPassword createUpdatedEntity(EntityManager em) {
        RecuperacionPassword updatedRecuperacionPassword = new RecuperacionPassword()
            .token(UPDATED_TOKEN)
            .fechaSolicitud(UPDATED_FECHA_SOLICITUD)
            .fechaExpiracion(UPDATED_FECHA_EXPIRACION)
            .usado(UPDATED_USADO);
        // Add required entity
        Usuario usuario;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            usuario = UsuarioResourceIT.createUpdatedEntity(em);
            em.persist(usuario);
            em.flush();
        } else {
            usuario = TestUtil.findAll(em, Usuario.class).getFirst();
        }
        updatedRecuperacionPassword.setUsuario(usuario);
        return updatedRecuperacionPassword;
    }

    @BeforeEach
    void initTest() {
        recuperacionPassword = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedRecuperacionPassword != null) {
            recuperacionPasswordRepository.delete(insertedRecuperacionPassword);
            insertedRecuperacionPassword = null;
        }
    }

    @Test
    @Transactional
    void createRecuperacionPassword() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RecuperacionPassword
        var returnedRecuperacionPassword = om.readValue(
            restRecuperacionPasswordMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recuperacionPassword)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RecuperacionPassword.class
        );

        // Validate the RecuperacionPassword in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRecuperacionPasswordUpdatableFieldsEquals(
            returnedRecuperacionPassword,
            getPersistedRecuperacionPassword(returnedRecuperacionPassword)
        );

        insertedRecuperacionPassword = returnedRecuperacionPassword;
    }

    @Test
    @Transactional
    void createRecuperacionPasswordWithExistingId() throws Exception {
        // Create the RecuperacionPassword with an existing ID
        recuperacionPassword.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecuperacionPasswordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recuperacionPassword)))
            .andExpect(status().isBadRequest());

        // Validate the RecuperacionPassword in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTokenIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recuperacionPassword.setToken(null);

        // Create the RecuperacionPassword, which fails.

        restRecuperacionPasswordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recuperacionPassword)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaSolicitudIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recuperacionPassword.setFechaSolicitud(null);

        // Create the RecuperacionPassword, which fails.

        restRecuperacionPasswordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recuperacionPassword)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaExpiracionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recuperacionPassword.setFechaExpiracion(null);

        // Create the RecuperacionPassword, which fails.

        restRecuperacionPasswordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recuperacionPassword)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUsadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recuperacionPassword.setUsado(null);

        // Create the RecuperacionPassword, which fails.

        restRecuperacionPasswordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recuperacionPassword)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRecuperacionPasswords() throws Exception {
        // Initialize the database
        insertedRecuperacionPassword = recuperacionPasswordRepository.saveAndFlush(recuperacionPassword);

        // Get all the recuperacionPasswordList
        restRecuperacionPasswordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recuperacionPassword.getId().intValue())))
            .andExpect(jsonPath("$.[*].token").value(hasItem(DEFAULT_TOKEN)))
            .andExpect(jsonPath("$.[*].fechaSolicitud").value(hasItem(DEFAULT_FECHA_SOLICITUD.toString())))
            .andExpect(jsonPath("$.[*].fechaExpiracion").value(hasItem(DEFAULT_FECHA_EXPIRACION.toString())))
            .andExpect(jsonPath("$.[*].usado").value(hasItem(DEFAULT_USADO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRecuperacionPasswordsWithEagerRelationshipsIsEnabled() throws Exception {
        when(recuperacionPasswordRepositoryMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restRecuperacionPasswordMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(recuperacionPasswordRepositoryMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRecuperacionPasswordsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(recuperacionPasswordRepositoryMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restRecuperacionPasswordMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(recuperacionPasswordRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRecuperacionPassword() throws Exception {
        // Initialize the database
        insertedRecuperacionPassword = recuperacionPasswordRepository.saveAndFlush(recuperacionPassword);

        // Get the recuperacionPassword
        restRecuperacionPasswordMockMvc
            .perform(get(ENTITY_API_URL_ID, recuperacionPassword.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recuperacionPassword.getId().intValue()))
            .andExpect(jsonPath("$.token").value(DEFAULT_TOKEN))
            .andExpect(jsonPath("$.fechaSolicitud").value(DEFAULT_FECHA_SOLICITUD.toString()))
            .andExpect(jsonPath("$.fechaExpiracion").value(DEFAULT_FECHA_EXPIRACION.toString()))
            .andExpect(jsonPath("$.usado").value(DEFAULT_USADO));
    }

    @Test
    @Transactional
    void getNonExistingRecuperacionPassword() throws Exception {
        // Get the recuperacionPassword
        restRecuperacionPasswordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRecuperacionPassword() throws Exception {
        // Initialize the database
        insertedRecuperacionPassword = recuperacionPasswordRepository.saveAndFlush(recuperacionPassword);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recuperacionPassword
        RecuperacionPassword updatedRecuperacionPassword = recuperacionPasswordRepository
            .findById(recuperacionPassword.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedRecuperacionPassword are not directly saved in db
        em.detach(updatedRecuperacionPassword);
        updatedRecuperacionPassword
            .token(UPDATED_TOKEN)
            .fechaSolicitud(UPDATED_FECHA_SOLICITUD)
            .fechaExpiracion(UPDATED_FECHA_EXPIRACION)
            .usado(UPDATED_USADO);

        restRecuperacionPasswordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRecuperacionPassword.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedRecuperacionPassword))
            )
            .andExpect(status().isOk());

        // Validate the RecuperacionPassword in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRecuperacionPasswordToMatchAllProperties(updatedRecuperacionPassword);
    }

    @Test
    @Transactional
    void putNonExistingRecuperacionPassword() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recuperacionPassword.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecuperacionPasswordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recuperacionPassword.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recuperacionPassword))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecuperacionPassword in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecuperacionPassword() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recuperacionPassword.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecuperacionPasswordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recuperacionPassword))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecuperacionPassword in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecuperacionPassword() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recuperacionPassword.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecuperacionPasswordMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recuperacionPassword)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecuperacionPassword in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecuperacionPasswordWithPatch() throws Exception {
        // Initialize the database
        insertedRecuperacionPassword = recuperacionPasswordRepository.saveAndFlush(recuperacionPassword);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recuperacionPassword using partial update
        RecuperacionPassword partialUpdatedRecuperacionPassword = new RecuperacionPassword();
        partialUpdatedRecuperacionPassword.setId(recuperacionPassword.getId());

        partialUpdatedRecuperacionPassword.fechaSolicitud(UPDATED_FECHA_SOLICITUD);

        restRecuperacionPasswordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecuperacionPassword.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecuperacionPassword))
            )
            .andExpect(status().isOk());

        // Validate the RecuperacionPassword in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecuperacionPasswordUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRecuperacionPassword, recuperacionPassword),
            getPersistedRecuperacionPassword(recuperacionPassword)
        );
    }

    @Test
    @Transactional
    void fullUpdateRecuperacionPasswordWithPatch() throws Exception {
        // Initialize the database
        insertedRecuperacionPassword = recuperacionPasswordRepository.saveAndFlush(recuperacionPassword);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recuperacionPassword using partial update
        RecuperacionPassword partialUpdatedRecuperacionPassword = new RecuperacionPassword();
        partialUpdatedRecuperacionPassword.setId(recuperacionPassword.getId());

        partialUpdatedRecuperacionPassword
            .token(UPDATED_TOKEN)
            .fechaSolicitud(UPDATED_FECHA_SOLICITUD)
            .fechaExpiracion(UPDATED_FECHA_EXPIRACION)
            .usado(UPDATED_USADO);

        restRecuperacionPasswordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecuperacionPassword.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecuperacionPassword))
            )
            .andExpect(status().isOk());

        // Validate the RecuperacionPassword in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecuperacionPasswordUpdatableFieldsEquals(
            partialUpdatedRecuperacionPassword,
            getPersistedRecuperacionPassword(partialUpdatedRecuperacionPassword)
        );
    }

    @Test
    @Transactional
    void patchNonExistingRecuperacionPassword() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recuperacionPassword.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecuperacionPasswordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recuperacionPassword.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recuperacionPassword))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecuperacionPassword in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecuperacionPassword() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recuperacionPassword.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecuperacionPasswordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recuperacionPassword))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecuperacionPassword in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecuperacionPassword() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recuperacionPassword.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecuperacionPasswordMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(recuperacionPassword)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecuperacionPassword in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecuperacionPassword() throws Exception {
        // Initialize the database
        insertedRecuperacionPassword = recuperacionPasswordRepository.saveAndFlush(recuperacionPassword);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the recuperacionPassword
        restRecuperacionPasswordMockMvc
            .perform(delete(ENTITY_API_URL_ID, recuperacionPassword.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return recuperacionPasswordRepository.count();
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

    protected RecuperacionPassword getPersistedRecuperacionPassword(RecuperacionPassword recuperacionPassword) {
        return recuperacionPasswordRepository.findById(recuperacionPassword.getId()).orElseThrow();
    }

    protected void assertPersistedRecuperacionPasswordToMatchAllProperties(RecuperacionPassword expectedRecuperacionPassword) {
        assertRecuperacionPasswordAllPropertiesEquals(
            expectedRecuperacionPassword,
            getPersistedRecuperacionPassword(expectedRecuperacionPassword)
        );
    }

    protected void assertPersistedRecuperacionPasswordToMatchUpdatableProperties(RecuperacionPassword expectedRecuperacionPassword) {
        assertRecuperacionPasswordAllUpdatablePropertiesEquals(
            expectedRecuperacionPassword,
            getPersistedRecuperacionPassword(expectedRecuperacionPassword)
        );
    }
}
