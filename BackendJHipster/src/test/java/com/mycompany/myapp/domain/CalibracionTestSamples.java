package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CalibracionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Calibracion getCalibracionSample1() {
        return new Calibracion()
            .id(1L)
            .numeroCertificado("numeroCertificado1")
            .resultado("resultado1")
            .observaciones("observaciones1")
            .usuarioCreacion("usuarioCreacion1")
            .usuarioModificacion("usuarioModificacion1");
    }

    public static Calibracion getCalibracionSample2() {
        return new Calibracion()
            .id(2L)
            .numeroCertificado("numeroCertificado2")
            .resultado("resultado2")
            .observaciones("observaciones2")
            .usuarioCreacion("usuarioCreacion2")
            .usuarioModificacion("usuarioModificacion2");
    }

    public static Calibracion getCalibracionRandomSampleGenerator() {
        return new Calibracion()
            .id(longCount.incrementAndGet())
            .numeroCertificado(UUID.randomUUID().toString())
            .resultado(UUID.randomUUID().toString())
            .observaciones(UUID.randomUUID().toString())
            .usuarioCreacion(UUID.randomUUID().toString())
            .usuarioModificacion(UUID.randomUUID().toString());
    }
}
