package com.kims.goblinsis;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ApplicationTests {

    @Test
    @Ignore
    public void RandomStringTest() {
        String newRandomPw = RandomStringUtils.randomAlphabetic(10);
        System.out.println(newRandomPw);
    }

}
