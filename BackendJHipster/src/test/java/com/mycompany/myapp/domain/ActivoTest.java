package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ActivoTestSamples.*;
import static com.mycompany.myapp.domain.GrupoInstrumentalTestSamples.*;
import static com.mycompany.myapp.domain.TipoInstrumentoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ActivoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Activo.class);
        Activo activo1 = getActivoSample1();
        Activo activo2 = new Activo();
        assertThat(activo1).isNotEqualTo(activo2);

        activo2.setId(activo1.getId());
        assertThat(activo1).isEqualTo(activo2);

        activo2 = getActivoSample2();
        assertThat(activo1).isNotEqualTo(activo2);
    }

    @Test
    void grupoTest() {
        Activo activo = getActivoRandomSampleGenerator();
        GrupoInstrumental grupoInstrumentalBack = getGrupoInstrumentalRandomSampleGenerator();

        activo.setGrupo(grupoInstrumentalBack);
        assertThat(activo.getGrupo()).isEqualTo(grupoInstrumentalBack);

        activo.grupo(null);
        assertThat(activo.getGrupo()).isNull();
    }

    @Test
    void tipoInstrumentoTest() {
        Activo activo = getActivoRandomSampleGenerator();
        TipoInstrumento tipoInstrumentoBack = getTipoInstrumentoRandomSampleGenerator();

        activo.setTipoInstrumento(tipoInstrumentoBack);
        assertThat(activo.getTipoInstrumento()).isEqualTo(tipoInstrumentoBack);

        activo.tipoInstrumento(null);
        assertThat(activo.getTipoInstrumento()).isNull();
    }
}
