package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.GrupoInstrumentalTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GrupoInstrumentalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GrupoInstrumental.class);
        GrupoInstrumental grupoInstrumental1 = getGrupoInstrumentalSample1();
        GrupoInstrumental grupoInstrumental2 = new GrupoInstrumental();
        assertThat(grupoInstrumental1).isNotEqualTo(grupoInstrumental2);

        grupoInstrumental2.setId(grupoInstrumental1.getId());
        assertThat(grupoInstrumental1).isEqualTo(grupoInstrumental2);

        grupoInstrumental2 = getGrupoInstrumentalSample2();
        assertThat(grupoInstrumental1).isNotEqualTo(grupoInstrumental2);
    }
}
