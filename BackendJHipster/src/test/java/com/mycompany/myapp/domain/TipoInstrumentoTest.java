package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.TipoInstrumentoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoInstrumentoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoInstrumento.class);
        TipoInstrumento tipoInstrumento1 = getTipoInstrumentoSample1();
        TipoInstrumento tipoInstrumento2 = new TipoInstrumento();
        assertThat(tipoInstrumento1).isNotEqualTo(tipoInstrumento2);

        tipoInstrumento2.setId(tipoInstrumento1.getId());
        assertThat(tipoInstrumento1).isEqualTo(tipoInstrumento2);

        tipoInstrumento2 = getTipoInstrumentoSample2();
        assertThat(tipoInstrumento1).isNotEqualTo(tipoInstrumento2);
    }
}
