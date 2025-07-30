package org.example.vkedupractice.service;

import org.example.vkedupractice.repository.SegmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.example.vkedupractice.dto.UserDto;
import org.example.vkedupractice.dto.UserSegmentsResponse;
import org.example.vkedupractice.model.Segment;
import org.example.vkedupractice.model.User;
import org.example.vkedupractice.repository.UserRepository;

import java.util.HashSet;

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
    private SegmentRepository segmentRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAllWithSegments()        // ← Жёстко JOIN FETCH
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private UserDto convertToDto(User u) {
        return UserDto.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .createdAt(u.getCreatedAt())
                .segmentNames(
                        u.getSegments()
                                .stream()
                                .map(Segment::getName)
                                .collect(Collectors.toSet())
                )
                .build();
    }


    @Transactional(readOnly = true)
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    // Принудительно загрузить сегменты
                    user.getSegments().size();
                    return convertToDto(user);
                });
    }

    @Transactional(readOnly = true)
    public UserSegmentsResponse getUserSegments(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Принудительно загрузить сегменты
        user.getSegments().size();

        Set<String> segmentNames = user.getSegments().stream()
                .map(Segment::getName)
                .collect(Collectors.toSet());

        return UserSegmentsResponse.builder()
                .userId(userId)
                .username(user.getUsername())
                .segments(segmentNames)
                .build();
    }

    @Transactional(readOnly = true)
    public List<UserDto> getUsersBySegment(String segmentName) {
        List<User> users = userRepository.findUsersBySegmentNameWithSegments(segmentName);
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public long getTotalUserCount() {
        return userRepository.countAllUsers();
    }

    @Transactional
    public void addUsersToSegment(String segmentName, List<Long> userIds) {
        Segment segment = segmentRepository.findByName(segmentName)
                .orElseThrow(() -> new EntityNotFoundException("Segment not found"));

        List<User> users = userRepository.findAllById(userIds);
        users.forEach(user -> user.getSegments().add(segment));
        userRepository.saveAll(users);
    }
} 