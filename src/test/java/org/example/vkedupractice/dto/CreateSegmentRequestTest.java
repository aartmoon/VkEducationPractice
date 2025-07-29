package org.example.vkedupractice.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CreateSegmentRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validRequest_ShouldPassValidation() {
        // Given
        CreateSegmentRequest request = CreateSegmentRequest.builder()
                .name("VALID_SEGMENT")
                .description("Valid description")
                .percentage(50)
                .build();

        // When
        Set<ConstraintViolation<CreateSegmentRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void nullName_ShouldFailValidation() {
        // Given
        CreateSegmentRequest request = CreateSegmentRequest.builder()
                .name(null)
                .description("Valid description")
                .percentage(50)
                .build();

        // When
        Set<ConstraintViolation<CreateSegmentRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void emptyName_ShouldFailValidation() {
        // Given
        CreateSegmentRequest request = CreateSegmentRequest.builder()
                .name("")
                .description("Valid description")
                .percentage(50)
                .build();

        // When
        Set<ConstraintViolation<CreateSegmentRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void blankName_ShouldFailValidation() {
        // Given
        CreateSegmentRequest request = CreateSegmentRequest.builder()
                .name("   ")
                .description("Valid description")
                .percentage(50)
                .build();

        // When
        Set<ConstraintViolation<CreateSegmentRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void nullPercentage_ShouldFailValidation() {
        // Given
        CreateSegmentRequest request = CreateSegmentRequest.builder()
                .name("VALID_SEGMENT")
                .description("Valid description")
                .percentage(null)
                .build();

        // When
        Set<ConstraintViolation<CreateSegmentRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("percentage")));
    }

    @Test
    void percentageBelowMin_ShouldFailValidation() {
        // Given
        CreateSegmentRequest request = CreateSegmentRequest.builder()
                .name("VALID_SEGMENT")
                .description("Valid description")
                .percentage(0)
                .build();

        // When
        Set<ConstraintViolation<CreateSegmentRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("percentage")));
    }

    @Test
    void percentageAboveMax_ShouldFailValidation() {
        // Given
        CreateSegmentRequest request = CreateSegmentRequest.builder()
                .name("VALID_SEGMENT")
                .description("Valid description")
                .percentage(101)
                .build();

        // When
        Set<ConstraintViolation<CreateSegmentRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("percentage")));
    }

    @Test
    void percentageAtMin_ShouldPassValidation() {
        // Given
        CreateSegmentRequest request = CreateSegmentRequest.builder()
                .name("VALID_SEGMENT")
                .description("Valid description")
                .percentage(1)
                .build();

        // When
        Set<ConstraintViolation<CreateSegmentRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void percentageAtMax_ShouldPassValidation() {
        // Given
        CreateSegmentRequest request = CreateSegmentRequest.builder()
                .name("VALID_SEGMENT")
                .description("Valid description")
                .percentage(100)
                .build();

        // When
        Set<ConstraintViolation<CreateSegmentRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void nullDescription_ShouldPassValidation() {
        // Given
        CreateSegmentRequest request = CreateSegmentRequest.builder()
                .name("VALID_SEGMENT")
                .description(null)
                .percentage(50)
                .build();

        // When
        Set<ConstraintViolation<CreateSegmentRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty());
    }
} 