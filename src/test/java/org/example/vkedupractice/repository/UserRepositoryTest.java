package org.example.vkedupractice.repository;

import org.example.vkedupractice.model.Segment;
import org.example.vkedupractice.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SegmentRepository segmentRepository;

    private User testUser1;
    private User testUser2;
    private Segment testSegment;

    @BeforeEach
    void setUp() {
        // Очищаем базу данных
        userRepository.deleteAll();
        segmentRepository.deleteAll();

        // Создаем тестовый сегмент
        testSegment = Segment.builder()
                .name("TEST_SEGMENT")
                .description("Test segment")
                .createdAt(LocalDateTime.now())
                .build();
        testSegment = entityManager.persistAndFlush(testSegment);

        // Создаем тестовых пользователей
        testUser1 = User.builder()
                .username("user1")
                .email("user1@test.com")
                .createdAt(LocalDateTime.now())
                .segments(new HashSet<>())
                .build();
        testUser1 = entityManager.persistAndFlush(testUser1);

        testUser2 = User.builder()
                .username("user2")
                .email("user2@test.com")
                .createdAt(LocalDateTime.now())
                .segments(new HashSet<>())
                .build();
        testUser2 = entityManager.persistAndFlush(testUser2);
    }

    @Test
    void findByUsername_WhenUserExists_ShouldReturnUser() {
        // When
        Optional<User> result = userRepository.findByUsername("user1");

        // Then
        assertTrue(result.isPresent());
        assertEquals("user1", result.get().getUsername());
        assertEquals("user1@test.com", result.get().getEmail());
    }

    @Test
    void findByUsername_WhenUserNotExists_ShouldReturnEmpty() {
        // When
        Optional<User> result = userRepository.findByUsername("nonexistent");

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void findByEmail_WhenUserExists_ShouldReturnUser() {
        // When
        Optional<User> result = userRepository.findByEmail("user1@test.com");

        // Then
        assertTrue(result.isPresent());
        assertEquals("user1", result.get().getUsername());
        assertEquals("user1@test.com", result.get().getEmail());
    }

    @Test
    void findByEmail_WhenUserNotExists_ShouldReturnEmpty() {
        // When
        Optional<User> result = userRepository.findByEmail("nonexistent@test.com");

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void existsByUsername_WhenUserExists_ShouldReturnTrue() {
        // When
        boolean exists = userRepository.existsByUsername("user1");

        // Then
        assertTrue(exists);
    }

    @Test
    void existsByUsername_WhenUserNotExists_ShouldReturnFalse() {
        // When
        boolean exists = userRepository.existsByUsername("nonexistent");

        // Then
        assertFalse(exists);
    }

    @Test
    void existsByEmail_WhenUserExists_ShouldReturnTrue() {
        // When
        boolean exists = userRepository.existsByEmail("user1@test.com");

        // Then
        assertTrue(exists);
    }

    @Test
    void existsByEmail_WhenUserNotExists_ShouldReturnFalse() {
        // When
        boolean exists = userRepository.existsByEmail("nonexistent@test.com");

        // Then
        assertFalse(exists);
    }

    @Test
    void findUsersBySegmentName_WhenUsersInSegment_ShouldReturnUsers() {
        // Given
        testUser1.getSegments().add(testSegment);
        testUser2.getSegments().add(testSegment);
        entityManager.persistAndFlush(testUser1);
        entityManager.persistAndFlush(testUser2);

        // When
        List<User> users = userRepository.findUsersBySegmentName("TEST_SEGMENT");

        // Then
        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("user1")));
        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("user2")));
    }

    @Test
    void findUsersBySegmentName_WhenNoUsersInSegment_ShouldReturnEmptyList() {
        // When
        List<User> users = userRepository.findUsersBySegmentName("TEST_SEGMENT");

        // Then
        assertTrue(users.isEmpty());
    }

    @Test
    void countAllUsers_ShouldReturnCorrectCount() {
        // When
        long count = userRepository.countAllUsers();

        // Then
        assertEquals(2, count);
    }

    @Test
    void saveUser_ShouldPersistUser() {
        // Given
        User newUser = User.builder()
                .username("newuser")
                .email("newuser@test.com")
                .createdAt(LocalDateTime.now())
                .segments(new HashSet<>())
                .build();

        // When
        User savedUser = userRepository.save(newUser);

        // Then
        assertNotNull(savedUser.getId());
        assertEquals("newuser", savedUser.getUsername());
        assertEquals("newuser@test.com", savedUser.getEmail());

        // Проверяем, что пользователь действительно сохранен в базе
        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("newuser", foundUser.get().getUsername());
    }

    @Test
    void deleteUser_ShouldRemoveUser() {
        // Given
        Long userId = testUser1.getId();

        // When
        userRepository.deleteById(userId);

        // Then
        Optional<User> deletedUser = userRepository.findById(userId);
        assertFalse(deletedUser.isPresent());
    }
} 