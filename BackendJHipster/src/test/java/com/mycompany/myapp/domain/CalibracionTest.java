package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ActivoTestSamples.*;
import static com.mycompany.myapp.domain.CalibracionTestSamples.*;
import static com.mycompany.myapp.domain.LaboratorioCalibracionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CalibracionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Calibracion.class);
        Calibracion calibracion1 = getCalibracionSample1();
        Calibracion calibracion2 = new Calibracion();
        assertThat(calibracion1).isNotEqualTo(calibracion2);

        calibracion2.setId(calibracion1.getId());
        assertThat(calibracion1).isEqualTo(calibracion2);

        calibracion2 = getCalibracionSample2();
        assertThat(calibracion1).isNotEqualTo(calibracion2);
    }

    @Test
    void activoTest() {
        Calibracion calibracion = getCalibracionRandomSampleGenerator();
        Activo activoBack = getActivoRandomSampleGenerator();

        calibracion.setActivo(activoBack);
        assertThat(calibracion.getActivo()).isEqualTo(activoBack);

        calibracion.activo(null);
        assertThat(calibracion.getActivo()).isNull();
    }

    @Test
    void laboratorioTest() {
        Calibracion calibracion = getCalibracionRandomSampleGenerator();
        LaboratorioCalibracion laboratorioCalibracionBack = getLaboratorioCalibracionRandomSampleGenerator();

        calibracion.setLaboratorio(laboratorioCalibracionBack);
        assertThat(calibracion.getLaboratorio()).isEqualTo(laboratorioCalibracionBack);

        calibracion.laboratorio(null);
        assertThat(calibracion.getLaboratorio()).isNull();
    }
}
