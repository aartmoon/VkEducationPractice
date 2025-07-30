package org.example.vkedupractice.service;

import org.example.vkedupractice.dto.CreateSegmentRequest;
import org.example.vkedupractice.dto.SegmentDto;
import org.example.vkedupractice.model.Segment;
import org.example.vkedupractice.model.User;
import org.example.vkedupractice.repository.SegmentRepository;
import org.example.vkedupractice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SegmentServiceTest {

    @Mock
    private SegmentRepository segmentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SegmentService segmentService;

    private Segment testSegment;
    private User testUser;
    private CreateSegmentRequest createRequest;

    @BeforeEach
    void setUp() {
        testSegment = Segment.builder()
                .id(1L)
                .name("TEST_SEGMENT")
                .description("Test segment")
                .createdAt(now())
                .build();

        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .createdAt(now())
                .segments(new HashSet<>())
                .build();

        createRequest = CreateSegmentRequest.builder()
                .name("NEW_SEGMENT")
                .description("New test segment")
                .percentage(30)
                .build();
    }

    @Test
    void getAllSegments_ShouldReturnAllSegments() {
        // Given
        List<Segment> segments = Arrays.asList(testSegment);
        when(segmentRepository.findAll()).thenReturn(segments);

        // When
        List<SegmentDto> result = segmentService.getAllSegments();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testSegment.getId(), result.get(0).getId());
        assertEquals(testSegment.getName(), result.get(0).getName());
        assertEquals(testSegment.getDescription(), result.get(0).getDescription());

        verify(segmentRepository).findAll();
    }

    @Test
    void getSegmentById_WhenSegmentExists_ShouldReturnSegment() {
        // Given
        when(segmentRepository.findById(1L)).thenReturn(Optional.of(testSegment));

        // When
        Optional<SegmentDto> result = segmentService.getSegmentById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testSegment.getId(), result.get().getId());
        assertEquals(testSegment.getName(), result.get().getName());

        verify(segmentRepository).findById(1L);
    }

    @Test
    void getSegmentById_WhenSegmentNotExists_ShouldReturnEmpty() {
        // Given
        when(segmentRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<SegmentDto> result = segmentService.getSegmentById(999L);

        // Then
        assertFalse(result.isPresent());
        verify(segmentRepository).findById(999L);
    }

    @Test
    void getSegmentByName_WhenSegmentExists_ShouldReturnSegment() {
        // Given
        when(segmentRepository.findByName("TEST_SEGMENT")).thenReturn(Optional.of(testSegment));

        // When
        Optional<SegmentDto> result = segmentService.getSegmentByName("TEST_SEGMENT");

        // Then
        assertTrue(result.isPresent());
        assertEquals(testSegment.getName(), result.get().getName());

        verify(segmentRepository).findByName("TEST_SEGMENT");
    }

    @Test
    void createSegment_WhenSegmentNotExists_ShouldCreateSegment() {
        // Given
        //when(segmentRepository.existsByName("NEW_SEGMENT")).thenReturn(false);
        Segment savedSegment = Segment.builder()
                .id(1L)
                .name("NEW_SEGMENT")
                .description("New test segment")
                .createdAt(now())
                .build();
        when(segmentRepository.save(any(Segment.class))).thenReturn(savedSegment);
        when(segmentRepository.findById(1L)).thenReturn(Optional.of(savedSegment));
        when(userRepository.findAllWithSegments()).thenReturn(Arrays.asList(testUser));

        // When
        SegmentDto result = segmentService.createSegment(createRequest);

        // Then
        assertNotNull(result);
        assertEquals(savedSegment.getId(), result.getId());
        assertEquals(savedSegment.getName(), result.getName());

        verify(segmentRepository).save(any(Segment.class));
        verify(segmentRepository).findById(1L);
        verify(userRepository).findAllWithSegments();
    }

    @Test
    void createSegment_WhenSegmentExists_ShouldReturnDto() {
        // Given
        CreateSegmentRequest req = new CreateSegmentRequest("NEW_SEGMENT", "desc", 0);
        Segment saved = new Segment(1L, "NEW_SEGMENT", "desc", now(), Set.of());
        when(segmentRepository.save(any())).thenReturn(saved);
        when(segmentRepository.findById(1L)).thenReturn(Optional.of(saved));
        when(userRepository.findAllWithSegments()).thenReturn(Collections.emptyList());

        // When
        SegmentDto result = segmentService.createSegment(req);

        // Then
        assertEquals(1L, result.getId());
        verify(segmentRepository).save(any(Segment.class));
        verify(segmentRepository).findById(1L);
    }

    @Test
    void updateSegment_WhenSegmentExists_ShouldUpdateSegment() {
        // Given
        when(segmentRepository.findById(1L)).thenReturn(Optional.of(testSegment));
        when(segmentRepository.existsByName("UPDATED_SEGMENT")).thenReturn(false);
        when(segmentRepository.save(any(Segment.class))).thenReturn(testSegment);

        // When
        SegmentDto result = segmentService.updateSegment(1L, "UPDATED_SEGMENT", "Updated description");

        // Then
        assertNotNull(result);
        assertEquals(testSegment.getId(), result.getId());

        verify(segmentRepository).findById(1L);
        verify(segmentRepository).existsByName("UPDATED_SEGMENT");
        verify(segmentRepository).save(any(Segment.class));
    }

    @Test
    void updateSegment_WhenSegmentNotExists_ShouldThrowException() {
        // Given
        when(segmentRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> segmentService.updateSegment(999L, "NEW_NAME", "description"));
        verify(segmentRepository).findById(999L);
        verify(segmentRepository, never()).save(any(Segment.class));
    }

    @Test
    void deleteSegment_WhenSegmentExists_ShouldDeleteSegment() {
        // Given
        Segment segmentToDelete = Segment.builder()
                .id(1L)
                .name("TEST_SEGMENT")
                .users(new HashSet<>(Arrays.asList(testUser)))
                .build();
        testUser.getSegments().add(segmentToDelete);

        when(segmentRepository.findById(1L)).thenReturn(Optional.of(segmentToDelete));

        // When
        segmentService.deleteSegment(1L);

        // Then
        verify(segmentRepository).findById(1L);
        verify(userRepository).save(testUser);
        verify(segmentRepository).delete(segmentToDelete);
    }
    @Test
    void deleteSegment_WhenSegmentNotExists_ShouldThrowException() {
        // Given
        when(segmentRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> segmentService.deleteSegment(999L));
        verify(segmentRepository).findById(999L);
        verify(segmentRepository, never()).deleteById(anyLong());
    }

    @Test
    void getUsersInSegmentCount_ShouldReturnCorrectCount() {
        // Given
        when(segmentRepository.countUsersInSegment("TEST_SEGMENT")).thenReturn(5L);

        // When
        long result = segmentService.getUsersInSegmentCount("TEST_SEGMENT");

        // Then
        assertEquals(5L, result);
        verify(segmentRepository).countUsersInSegment("TEST_SEGMENT");
    }
}
