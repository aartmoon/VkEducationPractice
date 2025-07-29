package org.example.vkedupractice.service;

import org.example.vkedupractice.dto.CreateSegmentRequest;
import org.example.vkedupractice.dto.SegmentDto;
import org.example.vkedupractice.model.Segment;
import org.example.vkedupractice.model.User;
import org.example.vkedupractice.repository.SegmentRepository;
import org.example.vkedupractice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class SegmentService {

    @Autowired
    private SegmentRepository segmentRepository;

    @Autowired
    private UserRepository userRepository;

    private final Random random = new Random();

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

    public SegmentDto createSegment(CreateSegmentRequest request) {
        if (segmentRepository.existsByName(request.getName())) {
            throw new RuntimeException("Segment with name '" + request.getName() + "' already exists");
        }

        Segment segment = Segment.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        segment = segmentRepository.save(segment);

        // Assign segment to random percentage of users
        assignSegmentToRandomUsers(segment, request.getPercentage());

        return convertToDto(segment);
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
        if (!segmentRepository.existsById(id)) {
            throw new RuntimeException("Segment not found with id: " + id);
        }
        segmentRepository.deleteById(id);
    }

    public void assignSegmentToRandomUsers(Segment segment, int percentage) {
        List<User> allUsers = userRepository.findAll();
        if (allUsers.isEmpty()) {
            return;
        }

        int targetUserCount = (int) Math.ceil(allUsers.size() * percentage / 100.0);
        int currentUserCount = (int) segmentRepository.countUsersInSegment(segment.getName());

        if (currentUserCount >= targetUserCount) {
            return;
        }

        int usersToAdd = targetUserCount - currentUserCount;
        
        // Get users not in this segment
        List<User> availableUsers = allUsers.stream()
                .filter(user -> user.getSegments().stream()
                        .noneMatch(s -> s.getName().equals(segment.getName())))
                .collect(Collectors.toList());

        // Randomly select users
        for (int i = 0; i < Math.min(usersToAdd, availableUsers.size()); i++) {
            int randomIndex = random.nextInt(availableUsers.size());
            User user = availableUsers.remove(randomIndex);
            user.getSegments().add(segment);
            userRepository.save(user);
        }
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