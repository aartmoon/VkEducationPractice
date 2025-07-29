package org.example.vkedupractice.controller;

import org.example.vkedupractice.dto.CreateSegmentRequest;
import org.example.vkedupractice.dto.SegmentDto;
import org.example.vkedupractice.service.SegmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SegmentControllerTest {

    @Mock
    private SegmentService segmentService;

    @InjectMocks
    private SegmentController segmentController;

    private SegmentDto testSegmentDto;
    private CreateSegmentRequest createRequest;

    @BeforeEach
    void setUp() {
        testSegmentDto = SegmentDto.builder()
                .id(1L)
                .name("TEST_SEGMENT")
                .description("Test segment")
                .createdAt(LocalDateTime.now())
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
        List<SegmentDto> segments = Arrays.asList(testSegmentDto);
        when(segmentService.getAllSegments()).thenReturn(segments);

        // When
        ResponseEntity<List<SegmentDto>> response = segmentController.getAllSegments();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(testSegmentDto.getId(), response.getBody().get(0).getId());
        
        verify(segmentService).getAllSegments();
    }

    @Test
    void getSegmentById_WhenSegmentExists_ShouldReturnSegment() {
        // Given
        when(segmentService.getSegmentById(1L)).thenReturn(Optional.of(testSegmentDto));

        // When
        ResponseEntity<SegmentDto> response = segmentController.getSegmentById(1L);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testSegmentDto.getId(), response.getBody().getId());
        
        verify(segmentService).getSegmentById(1L);
    }

    @Test
    void getSegmentById_WhenSegmentNotExists_ShouldReturnNotFound() {
        // Given
        when(segmentService.getSegmentById(999L)).thenReturn(Optional.empty());

        // When
        ResponseEntity<SegmentDto> response = segmentController.getSegmentById(999L);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        
        verify(segmentService).getSegmentById(999L);
    }

    @Test
    void getSegmentByName_WhenSegmentExists_ShouldReturnSegment() {
        // Given
        when(segmentService.getSegmentByName("TEST_SEGMENT")).thenReturn(Optional.of(testSegmentDto));

        // When
        ResponseEntity<SegmentDto> response = segmentController.getSegmentByName("TEST_SEGMENT");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testSegmentDto.getName(), response.getBody().getName());
        
        verify(segmentService).getSegmentByName("TEST_SEGMENT");
    }

    @Test
    void getSegmentByName_WhenSegmentNotExists_ShouldReturnNotFound() {
        // Given
        when(segmentService.getSegmentByName("NONEXISTENT")).thenReturn(Optional.empty());

        // When
        ResponseEntity<SegmentDto> response = segmentController.getSegmentByName("NONEXISTENT");

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        
        verify(segmentService).getSegmentByName("NONEXISTENT");
    }

    @Test
    void createSegment_WhenValidRequest_ShouldCreateSegment() {
        // Given
        when(segmentService.createSegment(createRequest)).thenReturn(testSegmentDto);

        // When
        ResponseEntity<SegmentDto> response = segmentController.createSegment(createRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testSegmentDto.getId(), response.getBody().getId());
        
        verify(segmentService).createSegment(createRequest);
    }

    @Test
    void createSegment_WhenServiceThrowsException_ShouldReturnBadRequest() {
        // Given
        when(segmentService.createSegment(createRequest)).thenThrow(new RuntimeException("Error"));

        // When
        ResponseEntity<SegmentDto> response = segmentController.createSegment(createRequest);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        
        verify(segmentService).createSegment(createRequest);
    }

    @Test
    void updateSegment_WhenSegmentExists_ShouldUpdateSegment() {
        // Given
        when(segmentService.updateSegment(1L, "UPDATED_NAME", "Updated description"))
                .thenReturn(testSegmentDto);

        // When
        ResponseEntity<SegmentDto> response = segmentController.updateSegment(1L, "UPDATED_NAME", "Updated description");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testSegmentDto.getId(), response.getBody().getId());
        
        verify(segmentService).updateSegment(1L, "UPDATED_NAME", "Updated description");
    }

    @Test
    void updateSegment_WhenSegmentNotExists_ShouldReturnNotFound() {
        // Given
        when(segmentService.updateSegment(999L, "NEW_NAME", "description"))
                .thenThrow(new RuntimeException("Segment not found"));

        // When
        ResponseEntity<SegmentDto> response = segmentController.updateSegment(999L, "NEW_NAME", "description");

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        
        verify(segmentService).updateSegment(999L, "NEW_NAME", "description");
    }

    @Test
    void deleteSegment_WhenSegmentExists_ShouldDeleteSegment() {
        // Given
        doNothing().when(segmentService).deleteSegment(1L);

        // When
        ResponseEntity<Void> response = segmentController.deleteSegment(1L);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        
        verify(segmentService).deleteSegment(1L);
    }

    @Test
    void deleteSegment_WhenSegmentNotExists_ShouldReturnNotFound() {
        // Given
        doThrow(new RuntimeException("Segment not found")).when(segmentService).deleteSegment(999L);

        // When
        ResponseEntity<Void> response = segmentController.deleteSegment(999L);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        
        verify(segmentService).deleteSegment(999L);
    }

    @Test
    void getUsersInSegmentCount_ShouldReturnCorrectCount() {
        // Given
        when(segmentService.getUsersInSegmentCount("TEST_SEGMENT")).thenReturn(5L);

        // When
        ResponseEntity<Long> response = segmentController.getUsersInSegmentCount("TEST_SEGMENT");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5L, response.getBody());
        
        verify(segmentService).getUsersInSegmentCount("TEST_SEGMENT");
    }
} 