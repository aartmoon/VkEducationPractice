package org.example.vkedupractice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
public class VkEduPracticeApplicationTest {

    @Test
    void contextLoads() {
    }

    @Test
    void mainRunsWithoutException() {
        assertThatCode(() -> VkEduPracticeApplication.main(new String[]{}))
                .doesNotThrowAnyException();
    }
}
