package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.LaboratorioCalibracionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LaboratorioCalibracionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LaboratorioCalibracion.class);
        LaboratorioCalibracion laboratorioCalibracion1 = getLaboratorioCalibracionSample1();
        LaboratorioCalibracion laboratorioCalibracion2 = new LaboratorioCalibracion();
        assertThat(laboratorioCalibracion1).isNotEqualTo(laboratorioCalibracion2);

        laboratorioCalibracion2.setId(laboratorioCalibracion1.getId());
        assertThat(laboratorioCalibracion1).isEqualTo(laboratorioCalibracion2);

        laboratorioCalibracion2 = getLaboratorioCalibracionSample2();
        assertThat(laboratorioCalibracion1).isNotEqualTo(laboratorioCalibracion2);
    }
}
