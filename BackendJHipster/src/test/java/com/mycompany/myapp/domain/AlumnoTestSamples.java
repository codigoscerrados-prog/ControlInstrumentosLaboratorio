package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlumnoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Alumno getAlumnoSample1() {
        return new Alumno()
            .id(1L)
            .codigoAlumno("codigoAlumno1")
            .apellidoP("apellidoP1")
            .apellidoM("apellidoM1")
            .nombre("nombre1")
            .usuarioCreacion("usuarioCreacion1")
            .usuarioModificacion("usuarioModificacion1");
    }

    public static Alumno getAlumnoSample2() {
        return new Alumno()
            .id(2L)
            .codigoAlumno("codigoAlumno2")
            .apellidoP("apellidoP2")
            .apellidoM("apellidoM2")
            .nombre("nombre2")
            .usuarioCreacion("usuarioCreacion2")
            .usuarioModificacion("usuarioModificacion2");
    }

    public static Alumno getAlumnoRandomSampleGenerator() {
        return new Alumno()
            .id(longCount.incrementAndGet())
            .codigoAlumno(UUID.randomUUID().toString())
            .apellidoP(UUID.randomUUID().toString())
            .apellidoM(UUID.randomUUID().toString())
            .nombre(UUID.randomUUID().toString())
            .usuarioCreacion(UUID.randomUUID().toString())
            .usuarioModificacion(UUID.randomUUID().toString());
    }
}
