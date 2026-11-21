package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.AlumnoAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Alumno;
import com.mycompany.myapp.repository.AlumnoRepository;
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
 * Integration tests for the {@link AlumnoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlumnoResourceIT {

    private static final String DEFAULT_CODIGO_ALUMNO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_ALUMNO = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO_P = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO_P = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO_M = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO_M = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_USUARIO_CREACION = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_CREACION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.ofEpochMilli(1790641792953L);

    private static final String DEFAULT_usuario_MODIFICACION = "AAAAAAAAAA";
    private static final String UPDATED_usuario_MODIFICACION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_MODIFICACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_MODIFICACION = Instant.ofEpochMilli(1790641792953L);

    private static final String ENTITY_API_URL = "/api/alumnos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlumnoMockMvc;

    private Alumno alumno;

    private Alumno insertedAlumno;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alumno createEntity() {
        return new Alumno()
            .codigoAlumno(DEFAULT_CODIGO_ALUMNO)
            .apellidoP(DEFAULT_APELLIDO_P)
            .apellidoM(DEFAULT_APELLIDO_M)
            .nombre(DEFAULT_NOMBRE)
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
    public static Alumno createUpdatedEntity() {
        return new Alumno()
            .codigoAlumno(UPDATED_CODIGO_ALUMNO)
            .apellidoP(UPDATED_APELLIDO_P)
            .apellidoM(UPDATED_APELLIDO_M)
            .nombre(UPDATED_NOMBRE)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_usuario_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);
    }

    @BeforeEach
    void initTest() {
        alumno = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAlumno != null) {
            alumnoRepository.delete(insertedAlumno);
            insertedAlumno = null;
        }
    }

    @Test
    @Transactional
    void createAlumno() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Alumno
        var returnedAlumno = om.readValue(
            restAlumnoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alumno)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Alumno.class
        );

        // Validate the Alumno in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlumnoUpdatableFieldsEquals(returnedAlumno, getPersistedAlumno(returnedAlumno));

        insertedAlumno = returnedAlumno;
    }

    @Test
    @Transactional
    void createAlumnoWithExistingId() throws Exception {
        // Create the Alumno with an existing ID
        alumno.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlumnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alumno)))
            .andExpect(status().isBadRequest());

        // Validate the Alumno in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlumnos() throws Exception {
        // Initialize the database
        insertedAlumno = alumnoRepository.saveAndFlush(alumno);

        // Get all the alumnoList
        restAlumnoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alumno.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigoAlumno").value(hasItem(DEFAULT_CODIGO_ALUMNO)))
            .andExpect(jsonPath("$.[*].apellidoP").value(hasItem(DEFAULT_APELLIDO_P)))
            .andExpect(jsonPath("$.[*].apellidoM").value(hasItem(DEFAULT_APELLIDO_M)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].usuarioCreacion").value(hasItem(DEFAULT_USUARIO_CREACION)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].usuarioModificacion").value(hasItem(DEFAULT_usuario_MODIFICACION)))
            .andExpect(jsonPath("$.[*].fechaModificacion").value(hasItem(DEFAULT_FECHA_MODIFICACION.toString())));
    }

    @Test
    @Transactional
    void getAlumno() throws Exception {
        // Initialize the database
        insertedAlumno = alumnoRepository.saveAndFlush(alumno);

        // Get the alumno
        restAlumnoMockMvc
            .perform(get(ENTITY_API_URL_ID, alumno.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alumno.getId().intValue()))
            .andExpect(jsonPath("$.codigoAlumno").value(DEFAULT_CODIGO_ALUMNO))
            .andExpect(jsonPath("$.apellidoP").value(DEFAULT_APELLIDO_P))
            .andExpect(jsonPath("$.apellidoM").value(DEFAULT_APELLIDO_M))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.usuarioCreacion").value(DEFAULT_USUARIO_CREACION))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.usuarioModificacion").value(DEFAULT_usuario_MODIFICACION))
            .andExpect(jsonPath("$.fechaModificacion").value(DEFAULT_FECHA_MODIFICACION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAlumno() throws Exception {
        // Get the alumno
        restAlumnoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlumno() throws Exception {
        // Initialize the database
        insertedAlumno = alumnoRepository.saveAndFlush(alumno);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alumno
        Alumno updatedAlumno = alumnoRepository.findById(alumno.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlumno are not directly saved in db
        em.detach(updatedAlumno);
        updatedAlumno
            .codigoAlumno(UPDATED_CODIGO_ALUMNO)
            .apellidoP(UPDATED_APELLIDO_P)
            .apellidoM(UPDATED_APELLIDO_M)
            .nombre(UPDATED_NOMBRE)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_usuario_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);

        restAlumnoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlumno.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlumno))
            )
            .andExpect(status().isOk());

        // Validate the Alumno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlumnoToMatchAllProperties(updatedAlumno);
    }

    @Test
    @Transactional
    void putNonExistingAlumno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alumno.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlumnoMockMvc
            .perform(put(ENTITY_API_URL_ID, alumno.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alumno)))
            .andExpect(status().isBadRequest());

        // Validate the Alumno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlumno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alumno.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlumnoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alumno))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alumno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlumno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alumno.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlumnoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alumno)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alumno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlumnoWithPatch() throws Exception {
        // Initialize the database
        insertedAlumno = alumnoRepository.saveAndFlush(alumno);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alumno using partial update
        Alumno partialUpdatedAlumno = new Alumno();
        partialUpdatedAlumno.setId(alumno.getId());

        partialUpdatedAlumno
            .apellidoM(UPDATED_APELLIDO_M)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_usuario_MODIFICACION);

        restAlumnoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlumno.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlumno))
            )
            .andExpect(status().isOk());

        // Validate the Alumno in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlumnoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAlumno, alumno), getPersistedAlumno(alumno));
    }

    @Test
    @Transactional
    void fullUpdateAlumnoWithPatch() throws Exception {
        // Initialize the database
        insertedAlumno = alumnoRepository.saveAndFlush(alumno);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alumno using partial update
        Alumno partialUpdatedAlumno = new Alumno();
        partialUpdatedAlumno.setId(alumno.getId());

        partialUpdatedAlumno
            .codigoAlumno(UPDATED_CODIGO_ALUMNO)
            .apellidoP(UPDATED_APELLIDO_P)
            .apellidoM(UPDATED_APELLIDO_M)
            .nombre(UPDATED_NOMBRE)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_usuario_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);

        restAlumnoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlumno.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlumno))
            )
            .andExpect(status().isOk());

        // Validate the Alumno in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlumnoUpdatableFieldsEquals(partialUpdatedAlumno, getPersistedAlumno(partialUpdatedAlumno));
    }

    @Test
    @Transactional
    void patchNonExistingAlumno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alumno.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlumnoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alumno.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alumno))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alumno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlumno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alumno.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlumnoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alumno))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alumno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlumno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alumno.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlumnoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alumno)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alumno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlumno() throws Exception {
        // Initialize the database
        insertedAlumno = alumnoRepository.saveAndFlush(alumno);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alumno
        restAlumnoMockMvc
            .perform(delete(ENTITY_API_URL_ID, alumno.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alumnoRepository.count();
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

    protected Alumno getPersistedAlumno(Alumno alumno) {
        return alumnoRepository.findById(alumno.getId()).orElseThrow();
    }

    protected void assertPersistedAlumnoToMatchAllProperties(Alumno expectedAlumno) {
        assertAlumnoAllPropertiesEquals(expectedAlumno, getPersistedAlumno(expectedAlumno));
    }

    protected void assertPersistedAlumnoToMatchUpdatableProperties(Alumno expectedAlumno) {
        assertAlumnoAllUpdatablePropertiesEquals(expectedAlumno, getPersistedAlumno(expectedAlumno));
    }
}
