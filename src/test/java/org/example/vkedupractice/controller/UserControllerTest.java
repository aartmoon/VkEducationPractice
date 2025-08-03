package org.example.vkedupractice.controller;

import org.example.vkedupractice.dto.UserDto;
import org.example.vkedupractice.dto.UserSegmentsResponse;
import org.example.vkedupractice.service.UserService;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserDto testUserDto;
    private UserSegmentsResponse testUserSegmentsResponse;

    @BeforeEach
    void setUp() {
        testUserDto = UserDto.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .createdAt(LocalDateTime.now())
                .segmentNames(Set.of("TEST_SEGMENT"))
                .build();

        testUserSegmentsResponse = UserSegmentsResponse.builder()
                .userId(1L)
                .username("testuser")
                .segments(Set.of("TEST_SEGMENT"))
                .build();
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        List<UserDto> users = Arrays.asList(testUserDto);
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<UserDto>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(testUserDto.getId(), response.getBody().get(0).getId());
        
        verify(userService).getAllUsers();
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        when(userService.getUserById(1L)).thenReturn(Optional.of(testUserDto));

        ResponseEntity<UserDto> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testUserDto.getId(), response.getBody().getId());
        
        verify(userService).getUserById(1L);
    }

    @Test
    void getUserById_WhenUserNotExists_ShouldReturnNotFound() {
        when(userService.getUserById(999L)).thenReturn(Optional.empty());

        ResponseEntity<UserDto> response = userController.getUserById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        
        verify(userService).getUserById(999L);
    }

    @Test
    void getUserSegments_WhenUserExists_ShouldReturnUserSegments() {
        when(userService.getUserSegments(1L)).thenReturn(testUserSegmentsResponse);

        ResponseEntity<UserSegmentsResponse> response = userController.getUserSegments(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testUserSegmentsResponse.getUserId(), response.getBody().getUserId());
        assertEquals(testUserSegmentsResponse.getUsername(), response.getBody().getUsername());
        assertEquals(testUserSegmentsResponse.getSegments(), response.getBody().getSegments());
        
        verify(userService).getUserSegments(1L);
    }

    @Test
    void getUserSegments_WhenUserNotExists_ShouldReturnNotFound() {
        when(userService.getUserSegments(999L)).thenThrow(new RuntimeException("User not found"));

        ResponseEntity<UserSegmentsResponse> response = userController.getUserSegments(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        
        verify(userService).getUserSegments(999L);
    }

    @Test
    void getUsersBySegment_ShouldReturnUsersInSegment() {
        List<UserDto> users = Arrays.asList(testUserDto);
        when(userService.getUsersBySegment("TEST_SEGMENT")).thenReturn(users);

        ResponseEntity<List<UserDto>> response = userController.getUsersBySegment("TEST_SEGMENT");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(testUserDto.getId(), response.getBody().get(0).getId());
        
        verify(userService).getUsersBySegment("TEST_SEGMENT");
    }

    @Test
    void getTotalUserCount_ShouldReturnCorrectCount() {
        when(userService.getTotalUserCount()).thenReturn(10L);

        ResponseEntity<Long> response = userController.getTotalUserCount();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(10L, response.getBody());
        
        verify(userService).getTotalUserCount();
    }
} 