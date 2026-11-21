package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LaboratorioCalibracionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static LaboratorioCalibracion getLaboratorioCalibracionSample1() {
        return new LaboratorioCalibracion()
            .id(1L)
            .nombre("nombre1")
            .ruc("ruc1")
            .direccion("direccion1")
            .telefono("telefono1")
            .correo("correo1")
            .estado("estado1")
            .usuarioCreacion("usuarioCreacion1")
            .usuarioModificacion("usuarioModificacion1");
    }

    public static LaboratorioCalibracion getLaboratorioCalibracionSample2() {
        return new LaboratorioCalibracion()
            .id(2L)
            .nombre("nombre2")
            .ruc("ruc2")
            .direccion("direccion2")
            .telefono("telefono2")
            .correo("correo2")
            .estado("estado2")
            .usuarioCreacion("usuarioCreacion2")
            .usuarioModificacion("usuarioModificacion2");
    }

    public static LaboratorioCalibracion getLaboratorioCalibracionRandomSampleGenerator() {
        return new LaboratorioCalibracion()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .ruc(UUID.randomUUID().toString())
            .direccion(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString())
            .correo(UUID.randomUUID().toString())
            .estado(UUID.randomUUID().toString())
            .usuarioCreacion(UUID.randomUUID().toString())
            .usuarioModificacion(UUID.randomUUID().toString());
    }
}
