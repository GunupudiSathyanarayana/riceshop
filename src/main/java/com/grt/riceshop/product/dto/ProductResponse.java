package com.grt.riceshop.product.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponse(
        Long id,
        String productName,
        String brandName,
        String category,
        String variety,
        String origin,
        String grainType,
        String processingType,
        String aromaType,
        String qualityGrade,
        BigDecimal weightKg,
        BigDecimal price,
        Integer stockQuantity,
        String description,
        boolean isActive,
        List<String> usageTypes
) {}
