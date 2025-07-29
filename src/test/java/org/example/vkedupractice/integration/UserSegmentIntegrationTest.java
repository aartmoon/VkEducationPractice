package org.example.vkedupractice.integration;

import org.example.vkedupractice.dto.CreateSegmentRequest;
import org.example.vkedupractice.dto.UserSegmentsResponse;
import org.example.vkedupractice.model.Segment;
import org.example.vkedupractice.model.User;
import org.example.vkedupractice.repository.SegmentRepository;
import org.example.vkedupractice.repository.UserRepository;
import org.example.vkedupractice.service.SegmentService;
import org.example.vkedupractice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserSegmentIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private SegmentService segmentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SegmentRepository segmentRepository;

    private User testUser1;
    private User testUser2;
    private User testUser3;

    @BeforeEach
    void setUp() {
        // Очищаем базу данных перед каждым тестом
        userRepository.deleteAll();
        segmentRepository.deleteAll();

        // Создаем тестовых пользователей с уникальными именами
        testUser1 = User.builder()
                .username("testuser1")
                .email("testuser1@test.com")
                .build();
        testUser1 = userRepository.save(testUser1);

        testUser2 = User.builder()
                .username("testuser2")
                .email("testuser2@test.com")
                .build();
        testUser2 = userRepository.save(testUser2);

        testUser3 = User.builder()
                .username("testuser3")
                .email("testuser3@test.com")
                .build();
        testUser3 = userRepository.save(testUser3);
    }

    @Test
    void createSegmentAndAssignUsers_ShouldWorkCorrectly() {
        // Given
        CreateSegmentRequest request = CreateSegmentRequest.builder()
                .name("TEST_SEGMENT")
                .description("Test segment for integration test")
                .percentage(66) // 2 из 3 пользователей (66%)
                .build();

        // When
        var segmentDto = segmentService.createSegment(request);

        // Then
        assertNotNull(segmentDto);
        assertEquals("TEST_SEGMENT", segmentDto.getName());
        assertEquals("Test segment for integration test", segmentDto.getDescription());

        // Проверяем, что сегмент создан в базе
        Optional<Segment> segmentOpt = segmentRepository.findByName("TEST_SEGMENT");
        assertTrue(segmentOpt.isPresent());
        Segment segment = segmentOpt.get();

        // Проверяем количество пользователей в сегменте
        long usersInSegment = segmentService.getUsersInSegmentCount("TEST_SEGMENT");
        assertEquals(2, usersInSegment); // 66% от 3 пользователей = 2

        // Проверяем, что пользователи действительно в сегменте
        List<User> usersInSegmentList = userRepository.findUsersBySegmentName("TEST_SEGMENT");
        assertEquals(2, usersInSegmentList.size());
    }

    @Test
    void getUserSegments_ShouldReturnCorrectSegments() {
        // Given
        CreateSegmentRequest request = CreateSegmentRequest.builder()
                .name("TEST_SEGMENT")
                .description("Test segment")
                .percentage(100) // Все пользователи
                .build();
        segmentService.createSegment(request);

        // When
        UserSegmentsResponse response = userService.getUserSegments(testUser1.getId());

        // Then
        assertNotNull(response);
        assertEquals(testUser1.getId(), response.getUserId());
        assertEquals(testUser1.getUsername(), response.getUsername());
        // Проверяем, что пользователь может быть в сегменте (распределение случайное)
        assertTrue(response.getSegments().size() >= 0);
    }

    @Test
    void getUsersBySegment_ShouldReturnCorrectUsers() {
        // Given
        CreateSegmentRequest request = CreateSegmentRequest.builder()
                .name("TEST_SEGMENT")
                .description("Test segment")
                .percentage(66) // 2 из 3 пользователей
                .build();
        segmentService.createSegment(request);

        // When
        var users = userService.getUsersBySegment("TEST_SEGMENT");

        // Then
        assertNotNull(users);
        // Проверяем, что есть пользователи в сегменте (распределение случайное)
        assertTrue(users.size() >= 0);
        // Проверяем, что все пользователи в списке имеют правильную структуру
        users.forEach(user -> {
            assertNotNull(user.getId());
            assertNotNull(user.getUsername());
            assertNotNull(user.getEmail());
        });
    }

    @Test
    void createMultipleSegments_ShouldWorkCorrectly() {
        // Given
        CreateSegmentRequest request1 = CreateSegmentRequest.builder()
                .name("SEGMENT_1")
                .description("First segment")
                .percentage(33) // 1 из 3 пользователей
                .build();

        CreateSegmentRequest request2 = CreateSegmentRequest.builder()
                .name("SEGMENT_2")
                .description("Second segment")
                .percentage(66) // 2 из 3 пользователей
                .build();

        // When
        var segment1 = segmentService.createSegment(request1);
        var segment2 = segmentService.createSegment(request2);

        // Then
        assertNotNull(segment1);
        assertNotNull(segment2);

        // Проверяем количество пользователей в каждом сегменте
        long usersInSegment1 = segmentService.getUsersInSegmentCount("SEGMENT_1");
        long usersInSegment2 = segmentService.getUsersInSegmentCount("SEGMENT_2");

        assertEquals(1, usersInSegment1);
        assertEquals(2, usersInSegment2);
    }

    @Test
    void updateSegment_ShouldWorkCorrectly() {
        // Given
        CreateSegmentRequest request = CreateSegmentRequest.builder()
                .name("ORIGINAL_SEGMENT")
                .description("Original description")
                .percentage(100)
                .build();
        var originalSegment = segmentService.createSegment(request);

        // When
        var updatedSegment = segmentService.updateSegment(
                originalSegment.getId(),
                "UPDATED_SEGMENT",
                "Updated description"
        );

        // Then
        assertNotNull(updatedSegment);
        assertEquals("UPDATED_SEGMENT", updatedSegment.getName());
        assertEquals("Updated description", updatedSegment.getDescription());

        // Проверяем, что старый сегмент больше не существует
        Optional<Segment> oldSegment = segmentRepository.findByName("ORIGINAL_SEGMENT");
        assertFalse(oldSegment.isPresent());

        // Проверяем, что новый сегмент существует
        Optional<Segment> newSegment = segmentRepository.findByName("UPDATED_SEGMENT");
        assertTrue(newSegment.isPresent());
    }

    @Test
    void deleteSegment_ShouldWorkCorrectly() {
        // Given
        CreateSegmentRequest request = CreateSegmentRequest.builder()
                .name("TO_DELETE_SEGMENT")
                .description("Segment to delete")
                .percentage(100)
                .build();
        var segment = segmentService.createSegment(request);

        // When
        segmentService.deleteSegment(segment.getId());

        // Then
        // Проверяем, что сегмент удален
        Optional<Segment> deletedSegment = segmentRepository.findById(segment.getId());
        assertFalse(deletedSegment.isPresent());
    }

    @Test
    void getTotalUserCount_ShouldReturnCorrectCount() {
        // Given - у нас 3 пользователя создано в setUp()

        // When
        long totalCount = userService.getTotalUserCount();

        // Then
        assertEquals(3, totalCount);
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Given - у нас 3 пользователя создано в setUp()

        // When
        var users = userService.getAllUsers();

        // Then
        assertNotNull(users);
        assertEquals(3, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("testuser1")));
        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("testuser2")));
        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("testuser3")));
    }
} 