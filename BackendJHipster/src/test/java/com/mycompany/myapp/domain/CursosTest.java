package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CursosTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CursosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cursos.class);
        Cursos cursos1 = getCursosSample1();
        Cursos cursos2 = new Cursos();
        assertThat(cursos1).isNotEqualTo(cursos2);

        cursos2.setId(cursos1.getId());
        assertThat(cursos1).isEqualTo(cursos2);

        cursos2 = getCursosSample2();
        assertThat(cursos1).isNotEqualTo(cursos2);
    }
}
