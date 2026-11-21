package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CursosTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Cursos getCursosSample1() {
        return new Cursos().id(1L).curso("curso1").usuarioCreacion("usuarioCreacion1").usuarioModificacion("usuarioModificacion1");
    }

    public static Cursos getCursosSample2() {
        return new Cursos().id(2L).curso("curso2").usuarioCreacion("usuarioCreacion2").usuarioModificacion("usuarioModificacion2");
    }

    public static Cursos getCursosRandomSampleGenerator() {
        return new Cursos()
            .id(longCount.incrementAndGet())
            .curso(UUID.randomUUID().toString())
            .usuarioCreacion(UUID.randomUUID().toString())
            .usuarioModificacion(UUID.randomUUID().toString());
    }
}
