package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.CursosAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Cursos;
import com.mycompany.myapp.repository.CursosRepository;
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
 * Integration tests for the {@link CursosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CursosResourceIT {

    private static final String DEFAULT_CURSO = "AAAAAAAAAA";
    private static final String UPDATED_CURSO = "BBBBBBBBBB";

    private static final String DEFAULT_USUARIO_CREACION = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_CREACION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.ofEpochMilli(1790641792953L);

    private static final String DEFAULT_usuario_MODIFICACION = "AAAAAAAAAA";
    private static final String UPDATED_usuario_MODIFICACION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_MODIFICACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_MODIFICACION = Instant.ofEpochMilli(1790641792953L);

    private static final String ENTITY_API_URL = "/api/cursos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CursosRepository cursosRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCursosMockMvc;

    private Cursos cursos;

    private Cursos insertedCursos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cursos createEntity() {
        return new Cursos()
            .curso(DEFAULT_CURSO)
            .usuarioCreacion(DEFAULT_USUARIO_CREACION)
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .usuarioModificacion(DEFAULT_usuario_MODIFICACION)
            .fechaModificacion(DEFAULT_FECHA_MODIFICACION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cursos createUpdatedEntity() {
        return new Cursos()
            .curso(UPDATED_CURSO)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_usuario_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);
    }

    @BeforeEach
    void initTest() {
        cursos = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCursos != null) {
            cursosRepository.delete(insertedCursos);
            insertedCursos = null;
        }
    }

    @Test
    @Transactional
    void createCursos() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Cursos
        var returnedCursos = om.readValue(
            restCursosMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cursos)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Cursos.class
        );

        // Validate the Cursos in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCursosUpdatableFieldsEquals(returnedCursos, getPersistedCursos(returnedCursos));

        insertedCursos = returnedCursos;
    }

    @Test
    @Transactional
    void createCursosWithExistingId() throws Exception {
        // Create the Cursos with an existing ID
        cursos.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCursosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cursos)))
            .andExpect(status().isBadRequest());

        // Validate the Cursos in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCursoses() throws Exception {
        // Initialize the database
        insertedCursos = cursosRepository.saveAndFlush(cursos);

        // Get all the cursosList
        restCursosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cursos.getId().intValue())))
            .andExpect(jsonPath("$.[*].curso").value(hasItem(DEFAULT_CURSO)))
            .andExpect(jsonPath("$.[*].usuarioCreacion").value(hasItem(DEFAULT_USUARIO_CREACION)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].usuarioModificacion").value(hasItem(DEFAULT_usuario_MODIFICACION)))
            .andExpect(jsonPath("$.[*].fechaModificacion").value(hasItem(DEFAULT_FECHA_MODIFICACION.toString())));
    }

    @Test
    @Transactional
    void getCursos() throws Exception {
        // Initialize the database
        insertedCursos = cursosRepository.saveAndFlush(cursos);

        // Get the cursos
        restCursosMockMvc
            .perform(get(ENTITY_API_URL_ID, cursos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cursos.getId().intValue()))
            .andExpect(jsonPath("$.curso").value(DEFAULT_CURSO))
            .andExpect(jsonPath("$.usuarioCreacion").value(DEFAULT_USUARIO_CREACION))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.usuarioModificacion").value(DEFAULT_usuario_MODIFICACION))
            .andExpect(jsonPath("$.fechaModificacion").value(DEFAULT_FECHA_MODIFICACION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCursos() throws Exception {
        // Get the cursos
        restCursosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCursos() throws Exception {
        // Initialize the database
        insertedCursos = cursosRepository.saveAndFlush(cursos);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cursos
        Cursos updatedCursos = cursosRepository.findById(cursos.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCursos are not directly saved in db
        em.detach(updatedCursos);
        updatedCursos
            .curso(UPDATED_CURSO)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_usuario_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);

        restCursosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCursos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCursos))
            )
            .andExpect(status().isOk());

        // Validate the Cursos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCursosToMatchAllProperties(updatedCursos);
    }

    @Test
    @Transactional
    void putNonExistingCursos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cursos.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCursosMockMvc
            .perform(put(ENTITY_API_URL_ID, cursos.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cursos)))
            .andExpect(status().isBadRequest());

        // Validate the Cursos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCursos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cursos.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCursosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cursos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cursos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCursos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cursos.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCursosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cursos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cursos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCursosWithPatch() throws Exception {
        // Initialize the database
        insertedCursos = cursosRepository.saveAndFlush(cursos);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cursos using partial update
        Cursos partialUpdatedCursos = new Cursos();
        partialUpdatedCursos.setId(cursos.getId());

        partialUpdatedCursos.usuarioCreacion(UPDATED_USUARIO_CREACION).fechaModificacion(UPDATED_FECHA_MODIFICACION);

        restCursosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCursos.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCursos))
            )
            .andExpect(status().isOk());

        // Validate the Cursos in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCursosUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCursos, cursos), getPersistedCursos(cursos));
    }

    @Test
    @Transactional
    void fullUpdateCursosWithPatch() throws Exception {
        // Initialize the database
        insertedCursos = cursosRepository.saveAndFlush(cursos);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cursos using partial update
        Cursos partialUpdatedCursos = new Cursos();
        partialUpdatedCursos.setId(cursos.getId());

        partialUpdatedCursos
            .curso(UPDATED_CURSO)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_usuario_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);

        restCursosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCursos.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCursos))
            )
            .andExpect(status().isOk());

        // Validate the Cursos in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCursosUpdatableFieldsEquals(partialUpdatedCursos, getPersistedCursos(partialUpdatedCursos));
    }

    @Test
    @Transactional
    void patchNonExistingCursos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cursos.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCursosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cursos.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cursos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cursos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCursos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cursos.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCursosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cursos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cursos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCursos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cursos.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCursosMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cursos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cursos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCursos() throws Exception {
        // Initialize the database
        insertedCursos = cursosRepository.saveAndFlush(cursos);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cursos
        restCursosMockMvc
            .perform(delete(ENTITY_API_URL_ID, cursos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cursosRepository.count();
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

    protected Cursos getPersistedCursos(Cursos cursos) {
        return cursosRepository.findById(cursos.getId()).orElseThrow();
    }

    protected void assertPersistedCursosToMatchAllProperties(Cursos expectedCursos) {
        assertCursosAllPropertiesEquals(expectedCursos, getPersistedCursos(expectedCursos));
    }

    protected void assertPersistedCursosToMatchUpdatableProperties(Cursos expectedCursos) {
        assertCursosAllUpdatablePropertiesEquals(expectedCursos, getPersistedCursos(expectedCursos));
    }
}
