package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.RecuperacionPasswordTestSamples.*;
import static com.mycompany.myapp.domain.UsuarioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecuperacionPasswordTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecuperacionPassword.class);
        RecuperacionPassword recuperacionPassword1 = getRecuperacionPasswordSample1();
        RecuperacionPassword recuperacionPassword2 = new RecuperacionPassword();
        assertThat(recuperacionPassword1).isNotEqualTo(recuperacionPassword2);

        recuperacionPassword2.setId(recuperacionPassword1.getId());
        assertThat(recuperacionPassword1).isEqualTo(recuperacionPassword2);

        recuperacionPassword2 = getRecuperacionPasswordSample2();
        assertThat(recuperacionPassword1).isNotEqualTo(recuperacionPassword2);
    }

    @Test
    void usuarioTest() {
        RecuperacionPassword recuperacionPassword = getRecuperacionPasswordRandomSampleGenerator();
        Usuario usuarioBack = getUsuarioRandomSampleGenerator();

        recuperacionPassword.setUsuario(usuarioBack);
        assertThat(recuperacionPassword.getUsuario()).isEqualTo(usuarioBack);

        recuperacionPassword.usuario(null);
        assertThat(recuperacionPassword.getUsuario()).isNull();
    }
}
