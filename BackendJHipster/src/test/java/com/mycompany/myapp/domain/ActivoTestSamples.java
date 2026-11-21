package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ActivoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Activo getActivoSample1() {
        return new Activo()
            .id(1L)
            .codigo("codigo1")
            .nombre("nombre1")
            .marca("marca1")
            .modelo("modelo1")
            .numeroSerie("numeroSerie1")
            .rango("rango1")
            .resolucion("resolucion1")
            .ubicacion("ubicacion1")
            .estado("estado1")
            .observaciones("observaciones1")
            .usuarioCreacion("usuarioCreacion1")
            .usuarioModificacion("usuarioModificacion1");
    }

    public static Activo getActivoSample2() {
        return new Activo()
            .id(2L)
            .codigo("codigo2")
            .nombre("nombre2")
            .marca("marca2")
            .modelo("modelo2")
            .numeroSerie("numeroSerie2")
            .rango("rango2")
            .resolucion("resolucion2")
            .ubicacion("ubicacion2")
            .estado("estado2")
            .observaciones("observaciones2")
            .usuarioCreacion("usuarioCreacion2")
            .usuarioModificacion("usuarioModificacion2");
    }

    public static Activo getActivoRandomSampleGenerator() {
        return new Activo()
            .id(longCount.incrementAndGet())
            .codigo(UUID.randomUUID().toString())
            .nombre(UUID.randomUUID().toString())
            .marca(UUID.randomUUID().toString())
            .modelo(UUID.randomUUID().toString())
            .numeroSerie(UUID.randomUUID().toString())
            .rango(UUID.randomUUID().toString())
            .resolucion(UUID.randomUUID().toString())
            .ubicacion(UUID.randomUUID().toString())
            .estado(UUID.randomUUID().toString())
            .observaciones(UUID.randomUUID().toString())
            .usuarioCreacion(UUID.randomUUID().toString())
            .usuarioModificacion(UUID.randomUUID().toString());
    }
}
