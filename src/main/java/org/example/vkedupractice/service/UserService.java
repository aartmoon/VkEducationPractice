package org.example.vkedupractice.service;

import org.example.vkedupractice.dto.UserDto;
import org.example.vkedupractice.dto.UserSegmentsResponse;
import org.example.vkedupractice.model.User;
import org.example.vkedupractice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDto);
    }

    public UserSegmentsResponse getUserSegments(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        Set<String> segmentNames = user.getSegments().stream()
                .map(segment -> segment.getName())
                .collect(Collectors.toSet());
        
        return UserSegmentsResponse.builder()
                .userId(userId)
                .username(user.getUsername())
                .segments(segmentNames)
                .build();
    }

    public List<UserDto> getUsersBySegment(String segmentName) {
        return userRepository.findUsersBySegmentName(segmentName).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public long getTotalUserCount() {
        return userRepository.countAllUsers();
    }

    private UserDto convertToDto(User user) {
        Set<String> segmentNames = user.getSegments().stream()
                .map(segment -> segment.getName())
                .collect(Collectors.toSet());
        
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .segmentNames(segmentNames)
                .build();
    }
} 