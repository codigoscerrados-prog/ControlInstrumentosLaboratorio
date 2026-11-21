package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UsuarioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Usuario getUsuarioSample1() {
        return new Usuario()
            .id(1L)
            .username("username1")
            .password("password1")
            .correo("correo1")
            .estado("estado1")
            .usuarioCreacion("usuarioCreacion1")
            .usuarioModificacion("usuarioModificacion1");
    }

    public static Usuario getUsuarioSample2() {
        return new Usuario()
            .id(2L)
            .username("username2")
            .password("password2")
            .correo("correo2")
            .estado("estado2")
            .usuarioCreacion("usuarioCreacion2")
            .usuarioModificacion("usuarioModificacion2");
    }

    public static Usuario getUsuarioRandomSampleGenerator() {
        return new Usuario()
            .id(longCount.incrementAndGet())
            .username(UUID.randomUUID().toString())
            .password(UUID.randomUUID().toString())
            .correo(UUID.randomUUID().toString())
            .estado(UUID.randomUUID().toString())
            .usuarioCreacion(UUID.randomUUID().toString())
            .usuarioModificacion(UUID.randomUUID().toString());
    }
}
