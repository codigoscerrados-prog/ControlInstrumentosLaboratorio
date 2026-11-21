package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.GrupoInstrumentalAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.GrupoInstrumental;
import com.mycompany.myapp.repository.GrupoInstrumentalRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link GrupoInstrumentalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GrupoInstrumentalResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.ofEpochMilli(1790641792953L);

    private static final String DEFAULT_USUARIO_CREACION = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_CREACION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_MODIFICACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_MODIFICACION = Instant.ofEpochMilli(1790641792953L);

    private static final String DEFAULT_USUARIO_MODIFICACION = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_MODIFICACION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/grupo-instrumentals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GrupoInstrumentalRepository grupoInstrumentalRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGrupoInstrumentalMockMvc;

    private GrupoInstrumental grupoInstrumental;

    private GrupoInstrumental insertedGrupoInstrumental;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GrupoInstrumental createEntity() {
        return new GrupoInstrumental()
            .codigo(DEFAULT_CODIGO)
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .usuarioCreacion(DEFAULT_USUARIO_CREACION)
            .fechaModificacion(DEFAULT_FECHA_MODIFICACION)
            .usuarioModificacion(DEFAULT_USUARIO_MODIFICACION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GrupoInstrumental createUpdatedEntity() {
        return new GrupoInstrumental()
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION);
    }

    @BeforeEach
    void initTest() {
        grupoInstrumental = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedGrupoInstrumental != null) {
            grupoInstrumentalRepository.delete(insertedGrupoInstrumental);
            insertedGrupoInstrumental = null;
        }
    }

    @Test
    @Transactional
    void createGrupoInstrumental() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the GrupoInstrumental
        var returnedGrupoInstrumental = om.readValue(
            restGrupoInstrumentalMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(grupoInstrumental)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            GrupoInstrumental.class
        );

        // Validate the GrupoInstrumental in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertGrupoInstrumentalUpdatableFieldsEquals(returnedGrupoInstrumental, getPersistedGrupoInstrumental(returnedGrupoInstrumental));

        insertedGrupoInstrumental = returnedGrupoInstrumental;
    }

    @Test
    @Transactional
    void createGrupoInstrumentalWithExistingId() throws Exception {
        // Create the GrupoInstrumental with an existing ID
        grupoInstrumental.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGrupoInstrumentalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(grupoInstrumental)))
            .andExpect(status().isBadRequest());

        // Validate the GrupoInstrumental in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        grupoInstrumental.setCodigo(null);

        // Create the GrupoInstrumental, which fails.

        restGrupoInstrumentalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(grupoInstrumental)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        grupoInstrumental.setNombre(null);

        // Create the GrupoInstrumental, which fails.

        restGrupoInstrumentalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(grupoInstrumental)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGrupoInstrumentals() throws Exception {
        // Initialize the database
        insertedGrupoInstrumental = grupoInstrumentalRepository.saveAndFlush(grupoInstrumental);

        // Get all the grupoInstrumentalList
        restGrupoInstrumentalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(grupoInstrumental.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].usuarioCreacion").value(hasItem(DEFAULT_USUARIO_CREACION)))
            .andExpect(jsonPath("$.[*].fechaModificacion").value(hasItem(DEFAULT_FECHA_MODIFICACION.toString())))
            .andExpect(jsonPath("$.[*].usuarioModificacion").value(hasItem(DEFAULT_USUARIO_MODIFICACION)));
    }

    @Test
    @Transactional
    void getGrupoInstrumental() throws Exception {
        // Initialize the database
        insertedGrupoInstrumental = grupoInstrumentalRepository.saveAndFlush(grupoInstrumental);

        // Get the grupoInstrumental
        restGrupoInstrumentalMockMvc
            .perform(get(ENTITY_API_URL_ID, grupoInstrumental.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(grupoInstrumental.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.usuarioCreacion").value(DEFAULT_USUARIO_CREACION))
            .andExpect(jsonPath("$.fechaModificacion").value(DEFAULT_FECHA_MODIFICACION.toString()))
            .andExpect(jsonPath("$.usuarioModificacion").value(DEFAULT_USUARIO_MODIFICACION));
    }

    @Test
    @Transactional
    void getNonExistingGrupoInstrumental() throws Exception {
        // Get the grupoInstrumental
        restGrupoInstrumentalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGrupoInstrumental() throws Exception {
        // Initialize the database
        insertedGrupoInstrumental = grupoInstrumentalRepository.saveAndFlush(grupoInstrumental);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the grupoInstrumental
        GrupoInstrumental updatedGrupoInstrumental = grupoInstrumentalRepository.findById(grupoInstrumental.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGrupoInstrumental are not directly saved in db
        em.detach(updatedGrupoInstrumental);
        updatedGrupoInstrumental
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION);

        restGrupoInstrumentalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGrupoInstrumental.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedGrupoInstrumental))
            )
            .andExpect(status().isOk());

        // Validate the GrupoInstrumental in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGrupoInstrumentalToMatchAllProperties(updatedGrupoInstrumental);
    }

    @Test
    @Transactional
    void putNonExistingGrupoInstrumental() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        grupoInstrumental.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGrupoInstrumentalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, grupoInstrumental.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(grupoInstrumental))
            )
            .andExpect(status().isBadRequest());

        // Validate the GrupoInstrumental in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGrupoInstrumental() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        grupoInstrumental.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGrupoInstrumentalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(grupoInstrumental))
            )
            .andExpect(status().isBadRequest());

        // Validate the GrupoInstrumental in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGrupoInstrumental() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        grupoInstrumental.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGrupoInstrumentalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(grupoInstrumental)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GrupoInstrumental in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGrupoInstrumentalWithPatch() throws Exception {
        // Initialize the database
        insertedGrupoInstrumental = grupoInstrumentalRepository.saveAndFlush(grupoInstrumental);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the grupoInstrumental using partial update
        GrupoInstrumental partialUpdatedGrupoInstrumental = new GrupoInstrumental();
        partialUpdatedGrupoInstrumental.setId(grupoInstrumental.getId());

        partialUpdatedGrupoInstrumental
            .codigo(UPDATED_CODIGO)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);

        restGrupoInstrumentalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGrupoInstrumental.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGrupoInstrumental))
            )
            .andExpect(status().isOk());

        // Validate the GrupoInstrumental in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGrupoInstrumentalUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedGrupoInstrumental, grupoInstrumental),
            getPersistedGrupoInstrumental(grupoInstrumental)
        );
    }

    @Test
    @Transactional
    void fullUpdateGrupoInstrumentalWithPatch() throws Exception {
        // Initialize the database
        insertedGrupoInstrumental = grupoInstrumentalRepository.saveAndFlush(grupoInstrumental);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the grupoInstrumental using partial update
        GrupoInstrumental partialUpdatedGrupoInstrumental = new GrupoInstrumental();
        partialUpdatedGrupoInstrumental.setId(grupoInstrumental.getId());

        partialUpdatedGrupoInstrumental
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION);

        restGrupoInstrumentalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGrupoInstrumental.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGrupoInstrumental))
            )
            .andExpect(status().isOk());

        // Validate the GrupoInstrumental in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGrupoInstrumentalUpdatableFieldsEquals(
            partialUpdatedGrupoInstrumental,
            getPersistedGrupoInstrumental(partialUpdatedGrupoInstrumental)
        );
    }

    @Test
    @Transactional
    void patchNonExistingGrupoInstrumental() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        grupoInstrumental.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGrupoInstrumentalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, grupoInstrumental.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(grupoInstrumental))
            )
            .andExpect(status().isBadRequest());

        // Validate the GrupoInstrumental in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGrupoInstrumental() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        grupoInstrumental.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGrupoInstrumentalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(grupoInstrumental))
            )
            .andExpect(status().isBadRequest());

        // Validate the GrupoInstrumental in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGrupoInstrumental() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        grupoInstrumental.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGrupoInstrumentalMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(grupoInstrumental)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GrupoInstrumental in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGrupoInstrumental() throws Exception {
        // Initialize the database
        insertedGrupoInstrumental = grupoInstrumentalRepository.saveAndFlush(grupoInstrumental);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the grupoInstrumental
        restGrupoInstrumentalMockMvc
            .perform(delete(ENTITY_API_URL_ID, grupoInstrumental.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return grupoInstrumentalRepository.count();
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

    protected GrupoInstrumental getPersistedGrupoInstrumental(GrupoInstrumental grupoInstrumental) {
        return grupoInstrumentalRepository.findById(grupoInstrumental.getId()).orElseThrow();
    }

    protected void assertPersistedGrupoInstrumentalToMatchAllProperties(GrupoInstrumental expectedGrupoInstrumental) {
        assertGrupoInstrumentalAllPropertiesEquals(expectedGrupoInstrumental, getPersistedGrupoInstrumental(expectedGrupoInstrumental));
    }

    protected void assertPersistedGrupoInstrumentalToMatchUpdatableProperties(GrupoInstrumental expectedGrupoInstrumental) {
        assertGrupoInstrumentalAllUpdatablePropertiesEquals(
            expectedGrupoInstrumental,
            getPersistedGrupoInstrumental(expectedGrupoInstrumental)
        );
    }
}
