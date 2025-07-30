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
class SegmentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SegmentRepository segmentRepository;

    @Autowired
    private UserRepository userRepository;

    private Segment testSegment1;
    private Segment testSegment2;
    private User testUser;

    @BeforeEach
    void setUp() {
        // Очищаем базу данных
        segmentRepository.deleteAll();
        userRepository.deleteAll();

        // Создаем тестового пользователя
        testUser = User.builder()
                .username("testuser")
                .email("testuser@test.com")
                .createdAt(LocalDateTime.now())
                .segments(new HashSet<>())
                .build();
        testUser = entityManager.persistAndFlush(testUser);

        // Создаем тестовые сегменты
        testSegment1 = Segment.builder()
                .name("TEST_SEGMENT_1")
                .description("First test segment")
                .createdAt(LocalDateTime.now())
                .build();
        testSegment1 = entityManager.persistAndFlush(testSegment1);

        testSegment2 = Segment.builder()
                .name("TEST_SEGMENT_2")
                .description("Second test segment")
                .createdAt(LocalDateTime.now())
                .build();
        testSegment2 = entityManager.persistAndFlush(testSegment2);
    }

    @Test
    void findByName_WhenSegmentExists_ShouldReturnSegment() {
        // When
        Optional<Segment> result = segmentRepository.findByName("TEST_SEGMENT_1");

        // Then
        assertTrue(result.isPresent());
        assertEquals("TEST_SEGMENT_1", result.get().getName());
        assertEquals("First test segment", result.get().getDescription());
    }

    @Test
    void findByName_WhenSegmentNotExists_ShouldReturnEmpty() {
        // When
        Optional<Segment> result = segmentRepository.findByName("NONEXISTENT_SEGMENT");

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void existsByName_WhenSegmentExists_ShouldReturnTrue() {
        // When
        boolean exists = segmentRepository.existsByName("TEST_SEGMENT_1");

        // Then
        assertTrue(exists);
    }

    @Test
    void existsByName_WhenSegmentNotExists_ShouldReturnFalse() {
        // When
        boolean exists = segmentRepository.existsByName("NONEXISTENT_SEGMENT");

        // Then
        assertFalse(exists);
    }

//    @Test
//    void findSegmentsByUserId_WhenUserHasSegments_ShouldReturnSegments() {
//        // Связываем юзера и сегменты с двух сторон
//        testUser.getSegments().add(testSegment1);
//        testUser.getSegments().add(testSegment2);
//
//        testSegment1.getUsers().add(testUser);
//        testSegment2.getUsers().add(testUser);
//
//        // Сохраняем сначала сегменты, чтобы избежать transient errors
//        entityManager.persist(testSegment1);
//        entityManager.persist(testSegment2);
//
//        entityManager.persistAndFlush(testUser); // flush точно сохраняет связь в user_segments
//
//        // Запрос
//        List<Segment> segments = segmentRepository.findSegmentsByUserId(testUser.getId());
//
//        // Проверки
//        assertEquals(2, segments.size());
//        assertTrue(segments.stream().anyMatch(s -> s.getName().equals("TEST_SEGMENT_1")));
//        assertTrue(segments.stream().anyMatch(s -> s.getName().equals("TEST_SEGMENT_2")));
//    }




    @Test
    void findSegmentsByUserId_WhenUserHasNoSegments_ShouldReturnEmptyList() {
        // When
        List<Segment> segments = segmentRepository.findSegmentsByUserId(testUser.getId());

        // Then
        assertTrue(segments.isEmpty());
    }

    @Test
    void countUsersInSegment_WhenUsersInSegment_ShouldReturnCorrectCount() {
        // Given
        testUser.getSegments().add(testSegment1);
        entityManager.persistAndFlush(testUser);

        // When
        long count = segmentRepository.countUsersInSegment("TEST_SEGMENT_1");

        // Then
        assertEquals(1, count);
    }

    @Test
    void countUsersInSegment_WhenNoUsersInSegment_ShouldReturnZero() {
        // When
        long count = segmentRepository.countUsersInSegment("TEST_SEGMENT_1");

        // Then
        assertEquals(0, count);
    }

    @Test
    void saveSegment_ShouldPersistSegment() {
        // Given
        Segment newSegment = Segment.builder()
                .name("NEW_SEGMENT")
                .description("New test segment")
                .createdAt(LocalDateTime.now())
                .build();

        // When
        Segment savedSegment = segmentRepository.save(newSegment);

        // Then
        assertNotNull(savedSegment.getId());
        assertEquals("NEW_SEGMENT", savedSegment.getName());
        assertEquals("New test segment", savedSegment.getDescription());

        // Проверяем, что сегмент действительно сохранен в базе
        Optional<Segment> foundSegment = segmentRepository.findById(savedSegment.getId());
        assertTrue(foundSegment.isPresent());
        assertEquals("NEW_SEGMENT", foundSegment.get().getName());
    }

    @Test
    void deleteSegment_ShouldRemoveSegment() {
        // Given
        Long segmentId = testSegment1.getId();

        // When
        segmentRepository.deleteById(segmentId);

        // Then
        Optional<Segment> deletedSegment = segmentRepository.findById(segmentId);
        assertFalse(deletedSegment.isPresent());
    }

    @Test
    void findAll_ShouldReturnAllSegments() {
        // When
        List<Segment> segments = segmentRepository.findAll();

        // Then
        assertEquals(2, segments.size());
        assertTrue(segments.stream().anyMatch(s -> s.getName().equals("TEST_SEGMENT_1")));
        assertTrue(segments.stream().anyMatch(s -> s.getName().equals("TEST_SEGMENT_2")));
    }

    @Test
    void findById_WhenSegmentExists_ShouldReturnSegment() {
        // When
        Optional<Segment> result = segmentRepository.findById(testSegment1.getId());

        // Then
        assertTrue(result.isPresent());
        assertEquals("TEST_SEGMENT_1", result.get().getName());
    }

    @Test
    void findById_WhenSegmentNotExists_ShouldReturnEmpty() {
        // When
        Optional<Segment> result = segmentRepository.findById(999L);

        // Then
        assertFalse(result.isPresent());
    }
} 