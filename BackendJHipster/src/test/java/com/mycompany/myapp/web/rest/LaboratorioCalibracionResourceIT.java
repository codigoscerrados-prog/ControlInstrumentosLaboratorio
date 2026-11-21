package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.LaboratorioCalibracionAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.LaboratorioCalibracion;
import com.mycompany.myapp.repository.LaboratorioCalibracionRepository;
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
 * Integration tests for the {@link LaboratorioCalibracionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LaboratorioCalibracionResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_RUC = "AAAAAAAAAA";
    private static final String UPDATED_RUC = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/laboratorio-calibracions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LaboratorioCalibracionRepository laboratorioCalibracionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLaboratorioCalibracionMockMvc;

    private LaboratorioCalibracion laboratorioCalibracion;

    private LaboratorioCalibracion insertedLaboratorioCalibracion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LaboratorioCalibracion createEntity() {
        return new LaboratorioCalibracion()
            .nombre(DEFAULT_NOMBRE)
            .ruc(DEFAULT_RUC)
            .direccion(DEFAULT_DIRECCION)
            .telefono(DEFAULT_TELEFONO)
            .correo(DEFAULT_CORREO)
            .estado(DEFAULT_ESTADO)
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
    public static LaboratorioCalibracion createUpdatedEntity() {
        return new LaboratorioCalibracion()
            .nombre(UPDATED_NOMBRE)
            .ruc(UPDATED_RUC)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .correo(UPDATED_CORREO)
            .estado(UPDATED_ESTADO)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION);
    }

    @BeforeEach
    void initTest() {
        laboratorioCalibracion = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedLaboratorioCalibracion != null) {
            laboratorioCalibracionRepository.delete(insertedLaboratorioCalibracion);
            insertedLaboratorioCalibracion = null;
        }
    }

    @Test
    @Transactional
    void createLaboratorioCalibracion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LaboratorioCalibracion
        var returnedLaboratorioCalibracion = om.readValue(
            restLaboratorioCalibracionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(laboratorioCalibracion)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LaboratorioCalibracion.class
        );

        // Validate the LaboratorioCalibracion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertLaboratorioCalibracionUpdatableFieldsEquals(
            returnedLaboratorioCalibracion,
            getPersistedLaboratorioCalibracion(returnedLaboratorioCalibracion)
        );

        insertedLaboratorioCalibracion = returnedLaboratorioCalibracion;
    }

    @Test
    @Transactional
    void createLaboratorioCalibracionWithExistingId() throws Exception {
        // Create the LaboratorioCalibracion with an existing ID
        laboratorioCalibracion.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLaboratorioCalibracionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(laboratorioCalibracion)))
            .andExpect(status().isBadRequest());

        // Validate the LaboratorioCalibracion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        laboratorioCalibracion.setNombre(null);

        // Create the LaboratorioCalibracion, which fails.

        restLaboratorioCalibracionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(laboratorioCalibracion)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        laboratorioCalibracion.setEstado(null);

        // Create the LaboratorioCalibracion, which fails.

        restLaboratorioCalibracionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(laboratorioCalibracion)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLaboratorioCalibracions() throws Exception {
        // Initialize the database
        insertedLaboratorioCalibracion = laboratorioCalibracionRepository.saveAndFlush(laboratorioCalibracion);

        // Get all the laboratorioCalibracionList
        restLaboratorioCalibracionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(laboratorioCalibracion.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].ruc").value(hasItem(DEFAULT_RUC)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].correo").value(hasItem(DEFAULT_CORREO)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].usuarioCreacion").value(hasItem(DEFAULT_USUARIO_CREACION)))
            .andExpect(jsonPath("$.[*].fechaModificacion").value(hasItem(DEFAULT_FECHA_MODIFICACION.toString())))
            .andExpect(jsonPath("$.[*].usuarioModificacion").value(hasItem(DEFAULT_USUARIO_MODIFICACION)));
    }

    @Test
    @Transactional
    void getLaboratorioCalibracion() throws Exception {
        // Initialize the database
        insertedLaboratorioCalibracion = laboratorioCalibracionRepository.saveAndFlush(laboratorioCalibracion);

        // Get the laboratorioCalibracion
        restLaboratorioCalibracionMockMvc
            .perform(get(ENTITY_API_URL_ID, laboratorioCalibracion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(laboratorioCalibracion.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.ruc").value(DEFAULT_RUC))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.correo").value(DEFAULT_CORREO))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.usuarioCreacion").value(DEFAULT_USUARIO_CREACION))
            .andExpect(jsonPath("$.fechaModificacion").value(DEFAULT_FECHA_MODIFICACION.toString()))
            .andExpect(jsonPath("$.usuarioModificacion").value(DEFAULT_USUARIO_MODIFICACION));
    }

    @Test
    @Transactional
    void getNonExistingLaboratorioCalibracion() throws Exception {
        // Get the laboratorioCalibracion
        restLaboratorioCalibracionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLaboratorioCalibracion() throws Exception {
        // Initialize the database
        insertedLaboratorioCalibracion = laboratorioCalibracionRepository.saveAndFlush(laboratorioCalibracion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the laboratorioCalibracion
        LaboratorioCalibracion updatedLaboratorioCalibracion = laboratorioCalibracionRepository
            .findById(laboratorioCalibracion.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedLaboratorioCalibracion are not directly saved in db
        em.detach(updatedLaboratorioCalibracion);
        updatedLaboratorioCalibracion
            .nombre(UPDATED_NOMBRE)
            .ruc(UPDATED_RUC)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .correo(UPDATED_CORREO)
            .estado(UPDATED_ESTADO)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION);

        restLaboratorioCalibracionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLaboratorioCalibracion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedLaboratorioCalibracion))
            )
            .andExpect(status().isOk());

        // Validate the LaboratorioCalibracion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLaboratorioCalibracionToMatchAllProperties(updatedLaboratorioCalibracion);
    }

    @Test
    @Transactional
    void putNonExistingLaboratorioCalibracion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        laboratorioCalibracion.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLaboratorioCalibracionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, laboratorioCalibracion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(laboratorioCalibracion))
            )
            .andExpect(status().isBadRequest());

        // Validate the LaboratorioCalibracion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLaboratorioCalibracion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        laboratorioCalibracion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLaboratorioCalibracionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(laboratorioCalibracion))
            )
            .andExpect(status().isBadRequest());

        // Validate the LaboratorioCalibracion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLaboratorioCalibracion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        laboratorioCalibracion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLaboratorioCalibracionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(laboratorioCalibracion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LaboratorioCalibracion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLaboratorioCalibracionWithPatch() throws Exception {
        // Initialize the database
        insertedLaboratorioCalibracion = laboratorioCalibracionRepository.saveAndFlush(laboratorioCalibracion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the laboratorioCalibracion using partial update
        LaboratorioCalibracion partialUpdatedLaboratorioCalibracion = new LaboratorioCalibracion();
        partialUpdatedLaboratorioCalibracion.setId(laboratorioCalibracion.getId());

        partialUpdatedLaboratorioCalibracion
            .ruc(UPDATED_RUC)
            .correo(UPDATED_CORREO)
            .estado(UPDATED_ESTADO)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);

        restLaboratorioCalibracionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLaboratorioCalibracion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLaboratorioCalibracion))
            )
            .andExpect(status().isOk());

        // Validate the LaboratorioCalibracion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLaboratorioCalibracionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLaboratorioCalibracion, laboratorioCalibracion),
            getPersistedLaboratorioCalibracion(laboratorioCalibracion)
        );
    }

    @Test
    @Transactional
    void fullUpdateLaboratorioCalibracionWithPatch() throws Exception {
        // Initialize the database
        insertedLaboratorioCalibracion = laboratorioCalibracionRepository.saveAndFlush(laboratorioCalibracion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the laboratorioCalibracion using partial update
        LaboratorioCalibracion partialUpdatedLaboratorioCalibracion = new LaboratorioCalibracion();
        partialUpdatedLaboratorioCalibracion.setId(laboratorioCalibracion.getId());

        partialUpdatedLaboratorioCalibracion
            .nombre(UPDATED_NOMBRE)
            .ruc(UPDATED_RUC)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .correo(UPDATED_CORREO)
            .estado(UPDATED_ESTADO)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION);

        restLaboratorioCalibracionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLaboratorioCalibracion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLaboratorioCalibracion))
            )
            .andExpect(status().isOk());

        // Validate the LaboratorioCalibracion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLaboratorioCalibracionUpdatableFieldsEquals(
            partialUpdatedLaboratorioCalibracion,
            getPersistedLaboratorioCalibracion(partialUpdatedLaboratorioCalibracion)
        );
    }

    @Test
    @Transactional
    void patchNonExistingLaboratorioCalibracion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        laboratorioCalibracion.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLaboratorioCalibracionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, laboratorioCalibracion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(laboratorioCalibracion))
            )
            .andExpect(status().isBadRequest());

        // Validate the LaboratorioCalibracion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLaboratorioCalibracion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        laboratorioCalibracion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLaboratorioCalibracionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(laboratorioCalibracion))
            )
            .andExpect(status().isBadRequest());

        // Validate the LaboratorioCalibracion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLaboratorioCalibracion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        laboratorioCalibracion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLaboratorioCalibracionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(laboratorioCalibracion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LaboratorioCalibracion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLaboratorioCalibracion() throws Exception {
        // Initialize the database
        insertedLaboratorioCalibracion = laboratorioCalibracionRepository.saveAndFlush(laboratorioCalibracion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the laboratorioCalibracion
        restLaboratorioCalibracionMockMvc
            .perform(delete(ENTITY_API_URL_ID, laboratorioCalibracion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return laboratorioCalibracionRepository.count();
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

    protected LaboratorioCalibracion getPersistedLaboratorioCalibracion(LaboratorioCalibracion laboratorioCalibracion) {
        return laboratorioCalibracionRepository.findById(laboratorioCalibracion.getId()).orElseThrow();
    }

    protected void assertPersistedLaboratorioCalibracionToMatchAllProperties(LaboratorioCalibracion expectedLaboratorioCalibracion) {
        assertLaboratorioCalibracionAllPropertiesEquals(
            expectedLaboratorioCalibracion,
            getPersistedLaboratorioCalibracion(expectedLaboratorioCalibracion)
        );
    }

    protected void assertPersistedLaboratorioCalibracionToMatchUpdatableProperties(LaboratorioCalibracion expectedLaboratorioCalibracion) {
        assertLaboratorioCalibracionAllUpdatablePropertiesEquals(
            expectedLaboratorioCalibracion,
            getPersistedLaboratorioCalibracion(expectedLaboratorioCalibracion)
        );
    }
}
