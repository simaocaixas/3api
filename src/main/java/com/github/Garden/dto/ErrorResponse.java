package com.github.Garden.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Schema(description = "Standard error response format")
public record ErrorResponse(
        @Schema(description = "HTTP status code", example = "400")
        int status,

        @Schema(description = "Error message", example = "Invalid input data")
        String message,

        @Schema(description = "Timestamp of the error", example = "2025-05-01T15:26:30")
        LocalDateTime timestamp,

        @Schema(description = "Validation errors if applicable", example = "{\"age\": \"must be positive\"}")
        Map<String, String> fieldErrors
) {}
