package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class GrupoInstrumentalTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static GrupoInstrumental getGrupoInstrumentalSample1() {
        return new GrupoInstrumental()
            .id(1L)
            .codigo("codigo1")
            .nombre("nombre1")
            .descripcion("descripcion1")
            .usuarioCreacion("usuarioCreacion1")
            .usuarioModificacion("usuarioModificacion1");
    }

    public static GrupoInstrumental getGrupoInstrumentalSample2() {
        return new GrupoInstrumental()
            .id(2L)
            .codigo("codigo2")
            .nombre("nombre2")
            .descripcion("descripcion2")
            .usuarioCreacion("usuarioCreacion2")
            .usuarioModificacion("usuarioModificacion2");
    }

    public static GrupoInstrumental getGrupoInstrumentalRandomSampleGenerator() {
        return new GrupoInstrumental()
            .id(longCount.incrementAndGet())
            .codigo(UUID.randomUUID().toString())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString())
            .usuarioCreacion(UUID.randomUUID().toString())
            .usuarioModificacion(UUID.randomUUID().toString());
    }
}
