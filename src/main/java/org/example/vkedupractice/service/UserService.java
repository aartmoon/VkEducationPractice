package org.example.vkedupractice.service;

import lombok.RequiredArgsConstructor;
import org.example.vkedupractice.dto.UserDto;
import org.example.vkedupractice.dto.UserSegmentsResponse;
import org.example.vkedupractice.model.Segment;
import org.example.vkedupractice.model.User;
import org.example.vkedupractice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAllWithSegments()
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
                    user.getSegments().size();
                    return convertToDto(user);
                });
    }

    @Transactional(readOnly = true)
    public UserSegmentsResponse getUserSegments(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

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

} 