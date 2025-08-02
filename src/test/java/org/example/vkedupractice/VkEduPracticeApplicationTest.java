package org.example.vkedupractice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
public class VkEduPracticeApplicationTest {

    @Test
    void contextLoads() {
        // Проверяем, что Spring-контекст успешно инициализируется
    }

    @Test
    void mainRunsWithoutException() {
        // Проверяем, что метод main запускается без исключений
        assertThatCode(() -> VkEduPracticeApplication.main(new String[]{}))
                .doesNotThrowAnyException();
    }
}
