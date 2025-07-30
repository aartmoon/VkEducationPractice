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
        // 1) загрузим сам сегмент (связи lazy)
        Segment segment = segmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Segment not found with id: " + id));

        // 2) отчистим все связи с пользователями
        for (User u : segment.getUsers()) {
            u.getSegments().remove(segment);
            userRepository.save(u);
        }
        // 3) теперь можно спокойно удалить сам сегмент
        segmentRepository.delete(segment);
    }

    @Transactional
    public SegmentDto createSegment(CreateSegmentRequest request) {
        // 1) сохраняем или обновляем сам сегмент
        Segment segment = Segment.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        segment = segmentRepository.save(segment);
        segment = segmentRepository.findById(segment.getId()).orElseThrow();
        // 2) запускаем вашу функцию, которая сама берёт процент и кидает на нужных пользователей
        assignSegmentToRandomUsers(segment, request.getPercentage());

        // 3) конвертим в DTO
        return SegmentDto.from(segment);
    }

    // File: src/main/java/org/example/vkedupractice/service/SegmentService.java

    @Transactional
    public void assignSegmentToRandomUsers(Segment segment, int percentage) {
        List<User> allUsers = userRepository.findAllWithSegments();
        if (allUsers.isEmpty()) return;

        int targetUserCount = (int) Math.ceil(allUsers.size() * percentage / 100.0);
        int currentUserCount = (int) segmentRepository.countUsersInSegment(segment.getName());
        if (currentUserCount >= targetUserCount) return;

        int usersToAdd = targetUserCount - currentUserCount;

        // <-- здесь: фильтруем по id, чтобы взять только тех, у кого ещё НЕТ этого сегмента
        List<User> availableUsers = allUsers.stream()
                .filter(u -> u.getSegments().stream()
                        .noneMatch(s -> s.getId().equals(segment.getId())))  // <-- здесь
                .collect(Collectors.toList());

        Random rnd = new Random();
        for (int i = 0; i < Math.min(usersToAdd, availableUsers.size()); i++) {
            User u = availableUsers.remove(rnd.nextInt(availableUsers.size()));
            u.getSegments().add(segment);      // добавляем сегмент на "владеющей" стороне
            userRepository.save(u);            // сохраняем только пользователя
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