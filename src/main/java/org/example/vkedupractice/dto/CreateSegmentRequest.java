package org.example.vkedupractice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSegmentRequest {
    
    @NotBlank(message = "Segment name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Percentage is required")
    @Min(value = 1, message = "Percentage must be at least 1")
    @Max(value = 100, message = "Percentage cannot exceed 100")
    private Integer percentage;
} 