package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.CalibracionAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Activo;
import com.mycompany.myapp.domain.Calibracion;
import com.mycompany.myapp.domain.LaboratorioCalibracion;
import com.mycompany.myapp.repository.CalibracionRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
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
 * Integration tests for the {@link CalibracionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CalibracionResourceIT {

    private static final LocalDate DEFAULT_FECHA_ENVIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ENVIO = LocalDate.parse("2026-09-29");

    private static final LocalDate DEFAULT_FECHA_CALIBRACION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_CALIBRACION = LocalDate.parse("2026-09-29");

    private static final LocalDate DEFAULT_FECHA_RETORNO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_RETORNO = LocalDate.parse("2026-09-29");

    private static final LocalDate DEFAULT_FECHA_VENCIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_VENCIMIENTO = LocalDate.parse("2026-09-29");

    private static final String DEFAULT_NUMERO_CERTIFICADO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_CERTIFICADO = "BBBBBBBBBB";

    private static final String DEFAULT_RESULTADO = "AAAAAAAAAA";
    private static final String UPDATED_RESULTADO = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.ofEpochMilli(1790641792953L);

    private static final String DEFAULT_USUARIO_CREACION = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_CREACION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_MODIFICACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_MODIFICACION = Instant.ofEpochMilli(1790641792953L);

    private static final String DEFAULT_USUARIO_MODIFICACION = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_MODIFICACION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/calibracions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CalibracionRepository calibracionRepository;

    @Mock
    private CalibracionRepository calibracionRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCalibracionMockMvc;

    private Calibracion calibracion;

    private Calibracion insertedCalibracion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Calibracion createEntity(EntityManager em) {
        Calibracion calibracion = new Calibracion()
            .fechaEnvio(DEFAULT_FECHA_ENVIO)
            .fechaCalibracion(DEFAULT_FECHA_CALIBRACION)
            .fechaRetorno(DEFAULT_FECHA_RETORNO)
            .fechaVencimiento(DEFAULT_FECHA_VENCIMIENTO)
            .numeroCertificado(DEFAULT_NUMERO_CERTIFICADO)
            .resultado(DEFAULT_RESULTADO)
            .observaciones(DEFAULT_OBSERVACIONES)
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .usuarioCreacion(DEFAULT_USUARIO_CREACION)
            .fechaModificacion(DEFAULT_FECHA_MODIFICACION)
            .usuarioModificacion(DEFAULT_USUARIO_MODIFICACION);
        // Add required entity
        Activo activo;
        if (TestUtil.findAll(em, Activo.class).isEmpty()) {
            activo = ActivoResourceIT.createEntity(em);
            em.persist(activo);
            em.flush();
        } else {
            activo = TestUtil.findAll(em, Activo.class).getFirst();
        }
        calibracion.setActivo(activo);
        // Add required entity
        LaboratorioCalibracion laboratorioCalibracion;
        if (TestUtil.findAll(em, LaboratorioCalibracion.class).isEmpty()) {
            laboratorioCalibracion = LaboratorioCalibracionResourceIT.createEntity();
            em.persist(laboratorioCalibracion);
            em.flush();
        } else {
            laboratorioCalibracion = TestUtil.findAll(em, LaboratorioCalibracion.class).getFirst();
        }
        calibracion.setLaboratorio(laboratorioCalibracion);
        return calibracion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Calibracion createUpdatedEntity(EntityManager em) {
        Calibracion updatedCalibracion = new Calibracion()
            .fechaEnvio(UPDATED_FECHA_ENVIO)
            .fechaCalibracion(UPDATED_FECHA_CALIBRACION)
            .fechaRetorno(UPDATED_FECHA_RETORNO)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO)
            .numeroCertificado(UPDATED_NUMERO_CERTIFICADO)
            .resultado(UPDATED_RESULTADO)
            .observaciones(UPDATED_OBSERVACIONES)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION);
        // Add required entity
        Activo activo;
        if (TestUtil.findAll(em, Activo.class).isEmpty()) {
            activo = ActivoResourceIT.createUpdatedEntity(em);
            em.persist(activo);
            em.flush();
        } else {
            activo = TestUtil.findAll(em, Activo.class).getFirst();
        }
        updatedCalibracion.setActivo(activo);
        // Add required entity
        LaboratorioCalibracion laboratorioCalibracion;
        if (TestUtil.findAll(em, LaboratorioCalibracion.class).isEmpty()) {
            laboratorioCalibracion = LaboratorioCalibracionResourceIT.createUpdatedEntity();
            em.persist(laboratorioCalibracion);
            em.flush();
        } else {
            laboratorioCalibracion = TestUtil.findAll(em, LaboratorioCalibracion.class).getFirst();
        }
        updatedCalibracion.setLaboratorio(laboratorioCalibracion);
        return updatedCalibracion;
    }

    @BeforeEach
    void initTest() {
        calibracion = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCalibracion != null) {
            calibracionRepository.delete(insertedCalibracion);
            insertedCalibracion = null;
        }
    }

    @Test
    @Transactional
    void createCalibracion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Calibracion
        var returnedCalibracion = om.readValue(
            restCalibracionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(calibracion)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Calibracion.class
        );

        // Validate the Calibracion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCalibracionUpdatableFieldsEquals(returnedCalibracion, getPersistedCalibracion(returnedCalibracion));

        insertedCalibracion = returnedCalibracion;
    }

    @Test
    @Transactional
    void createCalibracionWithExistingId() throws Exception {
        // Create the Calibracion with an existing ID
        calibracion.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCalibracionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(calibracion)))
            .andExpect(status().isBadRequest());

        // Validate the Calibracion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCalibracions() throws Exception {
        // Initialize the database
        insertedCalibracion = calibracionRepository.saveAndFlush(calibracion);

        // Get all the calibracionList
        restCalibracionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calibracion.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaEnvio").value(hasItem(DEFAULT_FECHA_ENVIO.toString())))
            .andExpect(jsonPath("$.[*].fechaCalibracion").value(hasItem(DEFAULT_FECHA_CALIBRACION.toString())))
            .andExpect(jsonPath("$.[*].fechaRetorno").value(hasItem(DEFAULT_FECHA_RETORNO.toString())))
            .andExpect(jsonPath("$.[*].fechaVencimiento").value(hasItem(DEFAULT_FECHA_VENCIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].numeroCertificado").value(hasItem(DEFAULT_NUMERO_CERTIFICADO)))
            .andExpect(jsonPath("$.[*].resultado").value(hasItem(DEFAULT_RESULTADO)))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].usuarioCreacion").value(hasItem(DEFAULT_USUARIO_CREACION)))
            .andExpect(jsonPath("$.[*].fechaModificacion").value(hasItem(DEFAULT_FECHA_MODIFICACION.toString())))
            .andExpect(jsonPath("$.[*].usuarioModificacion").value(hasItem(DEFAULT_USUARIO_MODIFICACION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCalibracionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(calibracionRepositoryMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restCalibracionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(calibracionRepositoryMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCalibracionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(calibracionRepositoryMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restCalibracionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(calibracionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCalibracion() throws Exception {
        // Initialize the database
        insertedCalibracion = calibracionRepository.saveAndFlush(calibracion);

        // Get the calibracion
        restCalibracionMockMvc
            .perform(get(ENTITY_API_URL_ID, calibracion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(calibracion.getId().intValue()))
            .andExpect(jsonPath("$.fechaEnvio").value(DEFAULT_FECHA_ENVIO.toString()))
            .andExpect(jsonPath("$.fechaCalibracion").value(DEFAULT_FECHA_CALIBRACION.toString()))
            .andExpect(jsonPath("$.fechaRetorno").value(DEFAULT_FECHA_RETORNO.toString()))
            .andExpect(jsonPath("$.fechaVencimiento").value(DEFAULT_FECHA_VENCIMIENTO.toString()))
            .andExpect(jsonPath("$.numeroCertificado").value(DEFAULT_NUMERO_CERTIFICADO))
            .andExpect(jsonPath("$.resultado").value(DEFAULT_RESULTADO))
            .andExpect(jsonPath("$.observaciones").value(DEFAULT_OBSERVACIONES))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.usuarioCreacion").value(DEFAULT_USUARIO_CREACION))
            .andExpect(jsonPath("$.fechaModificacion").value(DEFAULT_FECHA_MODIFICACION.toString()))
            .andExpect(jsonPath("$.usuarioModificacion").value(DEFAULT_USUARIO_MODIFICACION));
    }

    @Test
    @Transactional
    void getNonExistingCalibracion() throws Exception {
        // Get the calibracion
        restCalibracionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCalibracion() throws Exception {
        // Initialize the database
        insertedCalibracion = calibracionRepository.saveAndFlush(calibracion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the calibracion
        Calibracion updatedCalibracion = calibracionRepository.findById(calibracion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCalibracion are not directly saved in db
        em.detach(updatedCalibracion);
        updatedCalibracion
            .fechaEnvio(UPDATED_FECHA_ENVIO)
            .fechaCalibracion(UPDATED_FECHA_CALIBRACION)
            .fechaRetorno(UPDATED_FECHA_RETORNO)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO)
            .numeroCertificado(UPDATED_NUMERO_CERTIFICADO)
            .resultado(UPDATED_RESULTADO)
            .observaciones(UPDATED_OBSERVACIONES)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION);

        restCalibracionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCalibracion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCalibracion))
            )
            .andExpect(status().isOk());

        // Validate the Calibracion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCalibracionToMatchAllProperties(updatedCalibracion);
    }

    @Test
    @Transactional
    void putNonExistingCalibracion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        calibracion.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalibracionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, calibracion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(calibracion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calibracion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCalibracion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        calibracion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalibracionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(calibracion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calibracion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCalibracion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        calibracion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalibracionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(calibracion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Calibracion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCalibracionWithPatch() throws Exception {
        // Initialize the database
        insertedCalibracion = calibracionRepository.saveAndFlush(calibracion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the calibracion using partial update
        Calibracion partialUpdatedCalibracion = new Calibracion();
        partialUpdatedCalibracion.setId(calibracion.getId());

        partialUpdatedCalibracion
            .fechaRetorno(UPDATED_FECHA_RETORNO)
            .resultado(UPDATED_RESULTADO)
            .observaciones(UPDATED_OBSERVACIONES)
            .fechaCreacion(UPDATED_FECHA_CREACION);

        restCalibracionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCalibracion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCalibracion))
            )
            .andExpect(status().isOk());

        // Validate the Calibracion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCalibracionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCalibracion, calibracion),
            getPersistedCalibracion(calibracion)
        );
    }

    @Test
    @Transactional
    void fullUpdateCalibracionWithPatch() throws Exception {
        // Initialize the database
        insertedCalibracion = calibracionRepository.saveAndFlush(calibracion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the calibracion using partial update
        Calibracion partialUpdatedCalibracion = new Calibracion();
        partialUpdatedCalibracion.setId(calibracion.getId());

        partialUpdatedCalibracion
            .fechaEnvio(UPDATED_FECHA_ENVIO)
            .fechaCalibracion(UPDATED_FECHA_CALIBRACION)
            .fechaRetorno(UPDATED_FECHA_RETORNO)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO)
            .numeroCertificado(UPDATED_NUMERO_CERTIFICADO)
            .resultado(UPDATED_RESULTADO)
            .observaciones(UPDATED_OBSERVACIONES)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION);

        restCalibracionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCalibracion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCalibracion))
            )
            .andExpect(status().isOk());

        // Validate the Calibracion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCalibracionUpdatableFieldsEquals(partialUpdatedCalibracion, getPersistedCalibracion(partialUpdatedCalibracion));
    }

    @Test
    @Transactional
    void patchNonExistingCalibracion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        calibracion.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalibracionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, calibracion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(calibracion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calibracion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCalibracion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        calibracion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalibracionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(calibracion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calibracion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCalibracion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        calibracion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalibracionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(calibracion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Calibracion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCalibracion() throws Exception {
        // Initialize the database
        insertedCalibracion = calibracionRepository.saveAndFlush(calibracion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the calibracion
        restCalibracionMockMvc
            .perform(delete(ENTITY_API_URL_ID, calibracion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return calibracionRepository.count();
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

    protected Calibracion getPersistedCalibracion(Calibracion calibracion) {
        return calibracionRepository.findById(calibracion.getId()).orElseThrow();
    }

    protected void assertPersistedCalibracionToMatchAllProperties(Calibracion expectedCalibracion) {
        assertCalibracionAllPropertiesEquals(expectedCalibracion, getPersistedCalibracion(expectedCalibracion));
    }

    protected void assertPersistedCalibracionToMatchUpdatableProperties(Calibracion expectedCalibracion) {
        assertCalibracionAllUpdatablePropertiesEquals(expectedCalibracion, getPersistedCalibracion(expectedCalibracion));
    }
}
