package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RecuperacionPasswordTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static RecuperacionPassword getRecuperacionPasswordSample1() {
        return new RecuperacionPassword().id(1L).token("token1");
    }

    public static RecuperacionPassword getRecuperacionPasswordSample2() {
        return new RecuperacionPassword().id(2L).token("token2");
    }

    public static RecuperacionPassword getRecuperacionPasswordRandomSampleGenerator() {
        return new RecuperacionPassword().id(longCount.incrementAndGet()).token(UUID.randomUUID().toString());
    }
}
