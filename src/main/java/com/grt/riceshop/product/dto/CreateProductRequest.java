package com.grt.riceshop.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record CreateProductRequest(
        @NotBlank
        @Size(max = 255)
        String productName,

        @Size(max = 100)
        String brandName,

        @NotBlank
        String category,

        @NotBlank
        String variety,

        @NotBlank
        String origin,

        @NotBlank
        String grainType,

        @NotBlank
        String processingType,

        @NotBlank
        String aromaType,

        @NotBlank
        String qualityGrade,

        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal weightKg,

        @NotNull
        @DecimalMin(value = "0.00")
        BigDecimal price,

        Integer stockQuantity,

        String description,

        List<String> usageTypes
) {}
