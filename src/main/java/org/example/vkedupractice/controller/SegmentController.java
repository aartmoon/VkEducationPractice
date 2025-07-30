package org.example.vkedupractice.controller;

import org.example.vkedupractice.dto.CreateSegmentRequest;
import org.example.vkedupractice.dto.SegmentDto;
import org.example.vkedupractice.service.SegmentService;
import org.example.vkedupractice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/segments")
@CrossOrigin(origins = "*")
public class SegmentController {

    @Autowired
    private SegmentService segmentService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<SegmentDto>> getAllSegments() {
        List<SegmentDto> segments = segmentService.getAllSegments();
        return ResponseEntity.ok(segments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SegmentDto> getSegmentById(@PathVariable Long id) {
        return segmentService.getSegmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<SegmentDto> getSegmentByName(@PathVariable String name) {
        return segmentService.getSegmentByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SegmentDto> createSegment(
            @Validated @RequestBody CreateSegmentRequest request
    ) {
        // Вся логика создания сегмента + рандомной рассылки — внутри сервиса
        SegmentDto dto = segmentService.createSegment(request);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SegmentDto> updateSegment(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description) {
        try {
            SegmentDto segment = segmentService.updateSegment(id, name, description);
            return ResponseEntity.ok(segment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSegment(@PathVariable Long id) {
        try {
            segmentService.deleteSegment(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{name}/users/count")
    public ResponseEntity<Long> getUsersInSegmentCount(@PathVariable String name) {
        long count = segmentService.getUsersInSegmentCount(name);
        return ResponseEntity.ok(count);
    }
} 