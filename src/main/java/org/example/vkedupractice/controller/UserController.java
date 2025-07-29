package org.example.vkedupractice.controller;

import org.example.vkedupractice.dto.UserDto;
import org.example.vkedupractice.dto.UserSegmentsResponse;
import org.example.vkedupractice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/segments")
    public ResponseEntity<UserSegmentsResponse> getUserSegments(@PathVariable Long id) {
        try {
            UserSegmentsResponse response = userService.getUserSegments(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/segment/{segmentName}")
    public ResponseEntity<List<UserDto>> getUsersBySegment(@PathVariable String segmentName) {
        List<UserDto> users = userService.getUsersBySegment(segmentName);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalUserCount() {
        long count = userService.getTotalUserCount();
        return ResponseEntity.ok(count);
    }
} 