package org.example.vkedupractice.service;

import org.example.vkedupractice.dto.UserDto;
import org.example.vkedupractice.dto.UserSegmentsResponse;
import org.example.vkedupractice.model.Segment;
import org.example.vkedupractice.model.User;
import org.example.vkedupractice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private Segment testSegment;

    @BeforeEach
    void setUp() {
        testSegment = Segment.builder()
                .id(1L)
                .name("TEST_SEGMENT")
                .description("Test segment")
                .createdAt(LocalDateTime.now())
                .build();

        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .createdAt(LocalDateTime.now())
                .segments(new HashSet<>(Arrays.asList(testSegment)))
                .build();
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Given
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findAllWithSegments()).thenReturn(users);

        // When
        List<UserDto> result = userService.getAllUsers();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testUser.getId(), result.get(0).getId());
        assertEquals(testUser.getUsername(), result.get(0).getUsername());
        assertEquals(testUser.getEmail(), result.get(0).getEmail());
        assertTrue(result.get(0).getSegmentNames().contains(testSegment.getName()));
        
        verify(userRepository).findAllWithSegments();
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        Optional<UserDto> result = userService.getUserById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testUser.getId(), result.get().getId());
        assertEquals(testUser.getUsername(), result.get().getUsername());
        assertEquals(testUser.getEmail(), result.get().getEmail());
        
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_WhenUserNotExists_ShouldReturnEmpty() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<UserDto> result = userService.getUserById(999L);

        // Then
        assertFalse(result.isPresent());
        verify(userRepository).findById(999L);
    }

    @Test
    void getUserSegments_WhenUserExists_ShouldReturnUserSegments() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        UserSegmentsResponse result = userService.getUserSegments(1L);

        // Then
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getUserId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertTrue(result.getSegments().contains(testSegment.getName()));
        
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserSegments_WhenUserNotExists_ShouldThrowException() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> userService.getUserSegments(999L));
        verify(userRepository).findById(999L);
    }

    @Test
    void getUsersBySegment_ShouldReturnUsersInSegment() {
        // Given
        Segment segment = Segment.builder()
                .id(1L)
                .name("TEST_SEGMENT")
                .description("Test segment")
                .createdAt(LocalDateTime.now())
                .build();

        User userInSegment = User.builder()
                .id(2L)
                .username("userInSegment")
                .email("userInSegment@example.com")
                .createdAt(LocalDateTime.now())
                .segments(new HashSet<>(Arrays.asList(segment)))
                .build();

        List<User> usersInSegment = Collections.singletonList(userInSegment);
        // Fix: Use the correct method name here
        when(userRepository.findUsersBySegmentNameWithSegments("TEST_SEGMENT"))
                .thenReturn(usersInSegment);

        // When
        List<UserDto> result = userService.getUsersBySegment("TEST_SEGMENT");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userInSegment.getId(), result.get(0).getId());

        // Fix: Verify the correct method
        verify(userRepository).findUsersBySegmentNameWithSegments("TEST_SEGMENT");
    }

    @Test
    void getTotalUserCount_ShouldReturnCorrectCount() {
        // Given
        when(userRepository.countAllUsers()).thenReturn(10L);

        // When
        long result = userService.getTotalUserCount();

        // Then
        assertEquals(10L, result);
        verify(userRepository).countAllUsers();
    }
}
