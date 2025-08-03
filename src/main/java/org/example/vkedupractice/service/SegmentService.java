package org.example.vkedupractice.service;

import lombok.RequiredArgsConstructor;
import org.example.vkedupractice.dto.CreateSegmentRequest;
import org.example.vkedupractice.dto.SegmentDto;
import org.example.vkedupractice.model.Segment;
import org.example.vkedupractice.model.User;
import org.example.vkedupractice.repository.SegmentRepository;
import org.example.vkedupractice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SegmentService {

    private final SegmentRepository segmentRepository;
    private final UserRepository userRepository;

    public List<SegmentDto> getAllSegments() {
        return segmentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<SegmentDto> getSegmentById(Long id) {
        return segmentRepository.findById(id)
                .map(this::convertToDto);
    }

    public Optional<SegmentDto> getSegmentByName(String name) {
        return segmentRepository.findByName(name)
                .map(this::convertToDto);
    }


    public SegmentDto updateSegment(Long id, String name, String description) {
        Segment segment = segmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Segment not found with id: " + id));

        if (name != null && !name.equals(segment.getName())) {
            if (segmentRepository.existsByName(name)) {
                throw new RuntimeException("Segment with name '" + name + "' already exists");
            }
            segment.setName(name);
        }

        if (description != null) {
            segment.setDescription(description);
        }

        segment = segmentRepository.save(segment);
        return convertToDto(segment);
    }

    public void deleteSegment(Long id) {
        Segment segment = segmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Segment not found with id: " + id));

        for (User u : segment.getUsers()) {
            u.getSegments().remove(segment);
            userRepository.save(u);
        }
        segmentRepository.delete(segment);
    }

    @Transactional
    public SegmentDto createSegment(CreateSegmentRequest request) {
        Segment segment = Segment.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        segment = segmentRepository.save(segment);
        segment = segmentRepository.findById(segment.getId()).orElseThrow();
        assignSegmentToRandomUsers(segment, request.getPercentage());

        return SegmentDto.from(segment);
    }


    @Transactional
    public void assignSegmentToRandomUsers(Segment segment, int percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }

        List<User> allUsers = userRepository.findAllWithSegments();
        if (allUsers.isEmpty()) return;

        int targetUserCount = (int) Math.ceil(allUsers.size() * percentage / 100.0);
        int currentUserCount = (int) segmentRepository.countUsersInSegment(segment.getName());
        if (currentUserCount >= targetUserCount) return;

        int usersToAdd = targetUserCount - currentUserCount;

        List<User> availableUsers = allUsers.stream()
                .filter(u -> u.getSegments().stream()
                        .noneMatch(s -> s.getId().equals(segment.getId())))
                .collect(Collectors.toList());

        if (availableUsers.isEmpty()) return;

        Collections.shuffle(availableUsers);
        List<User> usersToUpdate = availableUsers.subList(0, Math.min(usersToAdd, availableUsers.size()));

        for (User user : usersToUpdate) {
            user.getSegments().add(segment);
        }
        userRepository.saveAll(usersToUpdate);
    }


    public long getUsersInSegmentCount(String segmentName) {
        return segmentRepository.countUsersInSegment(segmentName);
    }

    private SegmentDto convertToDto(Segment segment) {
        return SegmentDto.builder()
                .id(segment.getId())
                .name(segment.getName())
                .description(segment.getDescription())
                .createdAt(segment.getCreatedAt())
                .build();
    }
}