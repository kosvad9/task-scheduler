package com.kosvad9.taskscheduler;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.gen.OctetSequenceKeyGenerator;
import org.junit.jupiter.api.Test;

public class GenerateKeyTest {
    @Test
    void generateKey() throws JOSEException {
        var generator = new OctetSequenceKeyGenerator(128);
        System.out.println(generator.generate().getKeyValue());
    }
}
