package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ActivoAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Activo;
import com.mycompany.myapp.domain.GrupoInstrumental;
import com.mycompany.myapp.domain.TipoInstrumento;
import com.mycompany.myapp.repository.ActivoRepository;
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
 * Integration tests for the {@link ActivoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ActivoResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_MARCA = "AAAAAAAAAA";
    private static final String UPDATED_MARCA = "BBBBBBBBBB";

    private static final String DEFAULT_MODELO = "AAAAAAAAAA";
    private static final String UPDATED_MODELO = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_SERIE = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_SERIE = "BBBBBBBBBB";

    private static final String DEFAULT_RANGO = "AAAAAAAAAA";
    private static final String UPDATED_RANGO = "BBBBBBBBBB";

    private static final String DEFAULT_RESOLUCION = "AAAAAAAAAA";
    private static final String UPDATED_RESOLUCION = "BBBBBBBBBB";

    private static final String DEFAULT_UBICACION = "AAAAAAAAAA";
    private static final String UPDATED_UBICACION = "BBBBBBBBBB";

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/activos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ActivoRepository activoRepository;

    @Mock
    private ActivoRepository activoRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActivoMockMvc;

    private Activo activo;

    private Activo insertedActivo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Activo createEntity(EntityManager em) {
        Activo activo = new Activo()
            .codigo(DEFAULT_CODIGO)
            .nombre(DEFAULT_NOMBRE)
            .marca(DEFAULT_MARCA)
            .modelo(DEFAULT_MODELO)
            .numeroSerie(DEFAULT_NUMERO_SERIE)
            .rango(DEFAULT_RANGO)
            .resolucion(DEFAULT_RESOLUCION)
            .ubicacion(DEFAULT_UBICACION)
            .estado(DEFAULT_ESTADO)
            .observaciones(DEFAULT_OBSERVACIONES)
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .usuarioCreacion(DEFAULT_USUARIO_CREACION)
            .fechaModificacion(DEFAULT_FECHA_MODIFICACION)
            .usuarioModificacion(DEFAULT_USUARIO_MODIFICACION);
        // Add required entity
        GrupoInstrumental grupoInstrumental;
        if (TestUtil.findAll(em, GrupoInstrumental.class).isEmpty()) {
            grupoInstrumental = GrupoInstrumentalResourceIT.createEntity();
            em.persist(grupoInstrumental);
            em.flush();
        } else {
            grupoInstrumental = TestUtil.findAll(em, GrupoInstrumental.class).getFirst();
        }
        activo.setGrupo(grupoInstrumental);
        // Add required entity
        TipoInstrumento tipoInstrumento;
        if (TestUtil.findAll(em, TipoInstrumento.class).isEmpty()) {
            tipoInstrumento = TipoInstrumentoResourceIT.createEntity();
            em.persist(tipoInstrumento);
            em.flush();
        } else {
            tipoInstrumento = TestUtil.findAll(em, TipoInstrumento.class).getFirst();
        }
        activo.setTipoInstrumento(tipoInstrumento);
        return activo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Activo createUpdatedEntity(EntityManager em) {
        Activo updatedActivo = new Activo()
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .marca(UPDATED_MARCA)
            .modelo(UPDATED_MODELO)
            .numeroSerie(UPDATED_NUMERO_SERIE)
            .rango(UPDATED_RANGO)
            .resolucion(UPDATED_RESOLUCION)
            .ubicacion(UPDATED_UBICACION)
            .estado(UPDATED_ESTADO)
            .observaciones(UPDATED_OBSERVACIONES)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION);
        // Add required entity
        GrupoInstrumental grupoInstrumental;
        if (TestUtil.findAll(em, GrupoInstrumental.class).isEmpty()) {
            grupoInstrumental = GrupoInstrumentalResourceIT.createUpdatedEntity();
            em.persist(grupoInstrumental);
            em.flush();
        } else {
            grupoInstrumental = TestUtil.findAll(em, GrupoInstrumental.class).getFirst();
        }
        updatedActivo.setGrupo(grupoInstrumental);
        // Add required entity
        TipoInstrumento tipoInstrumento;
        if (TestUtil.findAll(em, TipoInstrumento.class).isEmpty()) {
            tipoInstrumento = TipoInstrumentoResourceIT.createUpdatedEntity();
            em.persist(tipoInstrumento);
            em.flush();
        } else {
            tipoInstrumento = TestUtil.findAll(em, TipoInstrumento.class).getFirst();
        }
        updatedActivo.setTipoInstrumento(tipoInstrumento);
        return updatedActivo;
    }

    @BeforeEach
    void initTest() {
        activo = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedActivo != null) {
            activoRepository.delete(insertedActivo);
            insertedActivo = null;
        }
    }

    @Test
    @Transactional
    void createActivo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Activo
        var returnedActivo = om.readValue(
            restActivoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activo)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Activo.class
        );

        // Validate the Activo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertActivoUpdatableFieldsEquals(returnedActivo, getPersistedActivo(returnedActivo));

        insertedActivo = returnedActivo;
    }

    @Test
    @Transactional
    void createActivoWithExistingId() throws Exception {
        // Create the Activo with an existing ID
        activo.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restActivoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activo)))
            .andExpect(status().isBadRequest());

        // Validate the Activo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        activo.setCodigo(null);

        // Create the Activo, which fails.

        restActivoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        activo.setNombre(null);

        // Create the Activo, which fails.

        restActivoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        activo.setEstado(null);

        // Create the Activo, which fails.

        restActivoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllActivos() throws Exception {
        // Initialize the database
        insertedActivo = activoRepository.saveAndFlush(activo);

        // Get all the activoList
        restActivoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activo.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].marca").value(hasItem(DEFAULT_MARCA)))
            .andExpect(jsonPath("$.[*].modelo").value(hasItem(DEFAULT_MODELO)))
            .andExpect(jsonPath("$.[*].numeroSerie").value(hasItem(DEFAULT_NUMERO_SERIE)))
            .andExpect(jsonPath("$.[*].rango").value(hasItem(DEFAULT_RANGO)))
            .andExpect(jsonPath("$.[*].resolucion").value(hasItem(DEFAULT_RESOLUCION)))
            .andExpect(jsonPath("$.[*].ubicacion").value(hasItem(DEFAULT_UBICACION)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].usuarioCreacion").value(hasItem(DEFAULT_USUARIO_CREACION)))
            .andExpect(jsonPath("$.[*].fechaModificacion").value(hasItem(DEFAULT_FECHA_MODIFICACION.toString())))
            .andExpect(jsonPath("$.[*].usuarioModificacion").value(hasItem(DEFAULT_USUARIO_MODIFICACION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllActivosWithEagerRelationshipsIsEnabled() throws Exception {
        when(activoRepositoryMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restActivoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(activoRepositoryMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllActivosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(activoRepositoryMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restActivoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(activoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getActivo() throws Exception {
        // Initialize the database
        insertedActivo = activoRepository.saveAndFlush(activo);

        // Get the activo
        restActivoMockMvc
            .perform(get(ENTITY_API_URL_ID, activo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(activo.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.marca").value(DEFAULT_MARCA))
            .andExpect(jsonPath("$.modelo").value(DEFAULT_MODELO))
            .andExpect(jsonPath("$.numeroSerie").value(DEFAULT_NUMERO_SERIE))
            .andExpect(jsonPath("$.rango").value(DEFAULT_RANGO))
            .andExpect(jsonPath("$.resolucion").value(DEFAULT_RESOLUCION))
            .andExpect(jsonPath("$.ubicacion").value(DEFAULT_UBICACION))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.observaciones").value(DEFAULT_OBSERVACIONES))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.usuarioCreacion").value(DEFAULT_USUARIO_CREACION))
            .andExpect(jsonPath("$.fechaModificacion").value(DEFAULT_FECHA_MODIFICACION.toString()))
            .andExpect(jsonPath("$.usuarioModificacion").value(DEFAULT_USUARIO_MODIFICACION));
    }

    @Test
    @Transactional
    void getNonExistingActivo() throws Exception {
        // Get the activo
        restActivoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingActivo() throws Exception {
        // Initialize the database
        insertedActivo = activoRepository.saveAndFlush(activo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activo
        Activo updatedActivo = activoRepository.findById(activo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedActivo are not directly saved in db
        em.detach(updatedActivo);
        updatedActivo
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .marca(UPDATED_MARCA)
            .modelo(UPDATED_MODELO)
            .numeroSerie(UPDATED_NUMERO_SERIE)
            .rango(UPDATED_RANGO)
            .resolucion(UPDATED_RESOLUCION)
            .ubicacion(UPDATED_UBICACION)
            .estado(UPDATED_ESTADO)
            .observaciones(UPDATED_OBSERVACIONES)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION);

        restActivoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedActivo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedActivo))
            )
            .andExpect(status().isOk());

        // Validate the Activo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedActivoToMatchAllProperties(updatedActivo);
    }

    @Test
    @Transactional
    void putNonExistingActivo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activo.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivoMockMvc
            .perform(put(ENTITY_API_URL_ID, activo.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activo)))
            .andExpect(status().isBadRequest());

        // Validate the Activo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchActivo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(activo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamActivo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Activo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateActivoWithPatch() throws Exception {
        // Initialize the database
        insertedActivo = activoRepository.saveAndFlush(activo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activo using partial update
        Activo partialUpdatedActivo = new Activo();
        partialUpdatedActivo.setId(activo.getId());

        partialUpdatedActivo
            .nombre(UPDATED_NOMBRE)
            .marca(UPDATED_MARCA)
            .modelo(UPDATED_MODELO)
            .numeroSerie(UPDATED_NUMERO_SERIE)
            .resolucion(UPDATED_RESOLUCION)
            .ubicacion(UPDATED_UBICACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION);

        restActivoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActivo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedActivo))
            )
            .andExpect(status().isOk());

        // Validate the Activo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertActivoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedActivo, activo), getPersistedActivo(activo));
    }

    @Test
    @Transactional
    void fullUpdateActivoWithPatch() throws Exception {
        // Initialize the database
        insertedActivo = activoRepository.saveAndFlush(activo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activo using partial update
        Activo partialUpdatedActivo = new Activo();
        partialUpdatedActivo.setId(activo.getId());

        partialUpdatedActivo
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .marca(UPDATED_MARCA)
            .modelo(UPDATED_MODELO)
            .numeroSerie(UPDATED_NUMERO_SERIE)
            .rango(UPDATED_RANGO)
            .resolucion(UPDATED_RESOLUCION)
            .ubicacion(UPDATED_UBICACION)
            .estado(UPDATED_ESTADO)
            .observaciones(UPDATED_OBSERVACIONES)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION);

        restActivoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActivo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedActivo))
            )
            .andExpect(status().isOk());

        // Validate the Activo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertActivoUpdatableFieldsEquals(partialUpdatedActivo, getPersistedActivo(partialUpdatedActivo));
    }

    @Test
    @Transactional
    void patchNonExistingActivo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activo.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, activo.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(activo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchActivo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(activo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamActivo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(activo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Activo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteActivo() throws Exception {
        // Initialize the database
        insertedActivo = activoRepository.saveAndFlush(activo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the activo
        restActivoMockMvc
            .perform(delete(ENTITY_API_URL_ID, activo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return activoRepository.count();
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

    protected Activo getPersistedActivo(Activo activo) {
        return activoRepository.findById(activo.getId()).orElseThrow();
    }

    protected void assertPersistedActivoToMatchAllProperties(Activo expectedActivo) {
        assertActivoAllPropertiesEquals(expectedActivo, getPersistedActivo(expectedActivo));
    }

    protected void assertPersistedActivoToMatchUpdatableProperties(Activo expectedActivo) {
        assertActivoAllUpdatablePropertiesEquals(expectedActivo, getPersistedActivo(expectedActivo));
    }
}
