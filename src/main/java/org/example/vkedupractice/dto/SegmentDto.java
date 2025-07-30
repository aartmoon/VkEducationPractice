package org.example.vkedupractice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.vkedupractice.model.Segment;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SegmentDto {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;

    public static SegmentDto from(Segment segment) {
        return SegmentDto.builder()
                .id(segment.getId())
                .name(segment.getName())
                .description(segment.getDescription())
                .build();
    }
} 