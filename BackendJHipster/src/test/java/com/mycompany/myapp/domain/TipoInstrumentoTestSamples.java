package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TipoInstrumentoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static TipoInstrumento getTipoInstrumentoSample1() {
        return new TipoInstrumento()
            .id(1L)
            .nombre("nombre1")
            .descripcion("descripcion1")
            .usuarioCreacion("usuarioCreacion1")
            .usuarioModificacion("usuarioModificacion1");
    }

    public static TipoInstrumento getTipoInstrumentoSample2() {
        return new TipoInstrumento()
            .id(2L)
            .nombre("nombre2")
            .descripcion("descripcion2")
            .usuarioCreacion("usuarioCreacion2")
            .usuarioModificacion("usuarioModificacion2");
    }

    public static TipoInstrumento getTipoInstrumentoRandomSampleGenerator() {
        return new TipoInstrumento()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString())
            .usuarioCreacion(UUID.randomUUID().toString())
            .usuarioModificacion(UUID.randomUUID().toString());
    }
}
