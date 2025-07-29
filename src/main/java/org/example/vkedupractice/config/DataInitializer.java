package org.example.vkedupractice.config;

import org.example.vkedupractice.model.User;
import org.example.vkedupractice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Create test users if none exist
        if (userRepository.count() == 0) {
            createTestUsers();
        }
    }

    private void createTestUsers() {
        List<User> testUsers = Arrays.asList(
            User.builder()
                .username("user1")
                .email("user1@example.com")
                .build(),
            User.builder()
                .username("user2")
                .email("user2@example.com")
                .build(),
            User.builder()
                .username("user3")
                .email("user3@example.com")
                .build(),
            User.builder()
                .username("user4")
                .email("user4@example.com")
                .build(),
            User.builder()
                .username("user5")
                .email("user5@example.com")
                .build(),
            User.builder()
                .username("user6")
                .email("user6@example.com")
                .build(),
            User.builder()
                .username("user7")
                .email("user7@example.com")
                .build(),
            User.builder()
                .username("user8")
                .email("user8@example.com")
                .build(),
            User.builder()
                .username("user9")
                .email("user9@example.com")
                .build(),
            User.builder()
                .username("user10")
                .email("user10@example.com")
                .build()
        );

        userRepository.saveAll(testUsers);
        System.out.println("Created " + testUsers.size() + " test users");
    }
} 