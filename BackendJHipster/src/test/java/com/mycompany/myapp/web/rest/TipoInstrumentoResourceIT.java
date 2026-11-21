package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.TipoInstrumentoAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.TipoInstrumento;
import com.mycompany.myapp.repository.TipoInstrumentoRepository;
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
 * Integration tests for the {@link TipoInstrumentoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoInstrumentoResourceIT {

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

    private static final String ENTITY_API_URL = "/api/tipo-instrumentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TipoInstrumentoRepository tipoInstrumentoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoInstrumentoMockMvc;

    private TipoInstrumento tipoInstrumento;

    private TipoInstrumento insertedTipoInstrumento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoInstrumento createEntity() {
        return new TipoInstrumento()
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
    public static TipoInstrumento createUpdatedEntity() {
        return new TipoInstrumento()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION);
    }

    @BeforeEach
    void initTest() {
        tipoInstrumento = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTipoInstrumento != null) {
            tipoInstrumentoRepository.delete(insertedTipoInstrumento);
            insertedTipoInstrumento = null;
        }
    }

    @Test
    @Transactional
    void createTipoInstrumento() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TipoInstrumento
        var returnedTipoInstrumento = om.readValue(
            restTipoInstrumentoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoInstrumento)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TipoInstrumento.class
        );

        // Validate the TipoInstrumento in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTipoInstrumentoUpdatableFieldsEquals(returnedTipoInstrumento, getPersistedTipoInstrumento(returnedTipoInstrumento));

        insertedTipoInstrumento = returnedTipoInstrumento;
    }

    @Test
    @Transactional
    void createTipoInstrumentoWithExistingId() throws Exception {
        // Create the TipoInstrumento with an existing ID
        tipoInstrumento.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoInstrumentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoInstrumento)))
            .andExpect(status().isBadRequest());

        // Validate the TipoInstrumento in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoInstrumento.setNombre(null);

        // Create the TipoInstrumento, which fails.

        restTipoInstrumentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoInstrumento)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTipoInstrumentos() throws Exception {
        // Initialize the database
        insertedTipoInstrumento = tipoInstrumentoRepository.saveAndFlush(tipoInstrumento);

        // Get all the tipoInstrumentoList
        restTipoInstrumentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoInstrumento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].usuarioCreacion").value(hasItem(DEFAULT_USUARIO_CREACION)))
            .andExpect(jsonPath("$.[*].fechaModificacion").value(hasItem(DEFAULT_FECHA_MODIFICACION.toString())))
            .andExpect(jsonPath("$.[*].usuarioModificacion").value(hasItem(DEFAULT_USUARIO_MODIFICACION)));
    }

    @Test
    @Transactional
    void getTipoInstrumento() throws Exception {
        // Initialize the database
        insertedTipoInstrumento = tipoInstrumentoRepository.saveAndFlush(tipoInstrumento);

        // Get the tipoInstrumento
        restTipoInstrumentoMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoInstrumento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoInstrumento.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.usuarioCreacion").value(DEFAULT_USUARIO_CREACION))
            .andExpect(jsonPath("$.fechaModificacion").value(DEFAULT_FECHA_MODIFICACION.toString()))
            .andExpect(jsonPath("$.usuarioModificacion").value(DEFAULT_USUARIO_MODIFICACION));
    }

    @Test
    @Transactional
    void getNonExistingTipoInstrumento() throws Exception {
        // Get the tipoInstrumento
        restTipoInstrumentoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTipoInstrumento() throws Exception {
        // Initialize the database
        insertedTipoInstrumento = tipoInstrumentoRepository.saveAndFlush(tipoInstrumento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoInstrumento
        TipoInstrumento updatedTipoInstrumento = tipoInstrumentoRepository.findById(tipoInstrumento.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTipoInstrumento are not directly saved in db
        em.detach(updatedTipoInstrumento);
        updatedTipoInstrumento
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION);

        restTipoInstrumentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTipoInstrumento.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTipoInstrumento))
            )
            .andExpect(status().isOk());

        // Validate the TipoInstrumento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTipoInstrumentoToMatchAllProperties(updatedTipoInstrumento);
    }

    @Test
    @Transactional
    void putNonExistingTipoInstrumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoInstrumento.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoInstrumentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoInstrumento.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoInstrumento))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoInstrumento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoInstrumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoInstrumento.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoInstrumentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoInstrumento))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoInstrumento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoInstrumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoInstrumento.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoInstrumentoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoInstrumento)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoInstrumento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoInstrumentoWithPatch() throws Exception {
        // Initialize the database
        insertedTipoInstrumento = tipoInstrumentoRepository.saveAndFlush(tipoInstrumento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoInstrumento using partial update
        TipoInstrumento partialUpdatedTipoInstrumento = new TipoInstrumento();
        partialUpdatedTipoInstrumento.setId(tipoInstrumento.getId());

        partialUpdatedTipoInstrumento.nombre(UPDATED_NOMBRE).fechaCreacion(UPDATED_FECHA_CREACION);

        restTipoInstrumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoInstrumento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTipoInstrumento))
            )
            .andExpect(status().isOk());

        // Validate the TipoInstrumento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTipoInstrumentoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTipoInstrumento, tipoInstrumento),
            getPersistedTipoInstrumento(tipoInstrumento)
        );
    }

    @Test
    @Transactional
    void fullUpdateTipoInstrumentoWithPatch() throws Exception {
        // Initialize the database
        insertedTipoInstrumento = tipoInstrumentoRepository.saveAndFlush(tipoInstrumento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoInstrumento using partial update
        TipoInstrumento partialUpdatedTipoInstrumento = new TipoInstrumento();
        partialUpdatedTipoInstrumento.setId(tipoInstrumento.getId());

        partialUpdatedTipoInstrumento
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION);

        restTipoInstrumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoInstrumento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTipoInstrumento))
            )
            .andExpect(status().isOk());

        // Validate the TipoInstrumento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTipoInstrumentoUpdatableFieldsEquals(
            partialUpdatedTipoInstrumento,
            getPersistedTipoInstrumento(partialUpdatedTipoInstrumento)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTipoInstrumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoInstrumento.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoInstrumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoInstrumento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tipoInstrumento))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoInstrumento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoInstrumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoInstrumento.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoInstrumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tipoInstrumento))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoInstrumento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoInstrumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoInstrumento.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoInstrumentoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tipoInstrumento)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoInstrumento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipoInstrumento() throws Exception {
        // Initialize the database
        insertedTipoInstrumento = tipoInstrumentoRepository.saveAndFlush(tipoInstrumento);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tipoInstrumento
        restTipoInstrumentoMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoInstrumento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tipoInstrumentoRepository.count();
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

    protected TipoInstrumento getPersistedTipoInstrumento(TipoInstrumento tipoInstrumento) {
        return tipoInstrumentoRepository.findById(tipoInstrumento.getId()).orElseThrow();
    }

    protected void assertPersistedTipoInstrumentoToMatchAllProperties(TipoInstrumento expectedTipoInstrumento) {
        assertTipoInstrumentoAllPropertiesEquals(expectedTipoInstrumento, getPersistedTipoInstrumento(expectedTipoInstrumento));
    }

    protected void assertPersistedTipoInstrumentoToMatchUpdatableProperties(TipoInstrumento expectedTipoInstrumento) {
        assertTipoInstrumentoAllUpdatablePropertiesEquals(expectedTipoInstrumento, getPersistedTipoInstrumento(expectedTipoInstrumento));
    }
}
