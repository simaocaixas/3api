package com.github.Garden.dto;

import com.github.Garden.domain.LeafType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
@Schema(name = "Tree")
public record TreeDTO(

        @Schema(description = "Species of the tree", example = "Oak")
        @NotNull(message = "field specie is mandatory.")
        @NotBlank(message = "field specie is mandatory and can not be empty")
        String specie,

        @Schema(description = "Height of the tree in meters", example = "5.5", minimum = "0.1", maximum = "200")
        @DecimalMin(value = "0.1", message = "Height must be at least 0.1")
        @Max(value = 200, message = "Height must be at most 200")
        @NotNull(message = "field height is mandatory.")
        Double height,

        @Schema(description = "Age of the tree in years", example = "12", minimum = "0")
        @PositiveOrZero(message = "field age must be positive.")
        @NotNull(message = "field age is mandatory.")
        Integer age,

        @Schema(description = "Type of leaf", example = "DECIDUOUS")
        @NotNull(message = "field leaf type is mandatory.")
        LeafType leafType
) {}