package org.example.vkedupractice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSegmentsResponse {
    private Long userId;
    private String username;
    private Set<String> segments;
} 