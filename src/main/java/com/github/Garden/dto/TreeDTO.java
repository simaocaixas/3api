package com.github.Garden.dto;

import com.github.Garden.domain.LeafType;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record TreeDTO(
        @NotNull(message = "field specie is mandatory.")
        @NotBlank(message = "field specie is mandatory and can not be empty")
        String specie,

        @DecimalMin(value = "0.1", message = "Height must be at least 0.1")
        @Max(value = 200, message = "Height must be at most 200")
        @NotNull(message = "field height is mandatory.")
        Double height,

        @PositiveOrZero(message = "field age must be positive.")
        @NotNull(message = "field age is mandatory.")
        Integer age,

        @NotNull(message = "field leaf type is mandatory.")
        LeafType leafType) {
}
