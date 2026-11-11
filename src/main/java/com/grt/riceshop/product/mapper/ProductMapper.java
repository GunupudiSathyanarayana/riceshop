package com.grt.riceshop.product.mapper;

import com.grt.riceshop.product.dto.ProductResponse;
import com.grt.riceshop.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "category", source = "category.name")
    @Mapping(target = "variety", source = "variety.name")
    @Mapping(target = "origin", source = "origin.name")
    @Mapping(target = "grainType", expression = "java(product.grainType != null ? product.grainType.name() : null)")
    @Mapping(target = "processingType", expression = "java(product.processingType != null ? product.processingType.name() : null)")
    @Mapping(target = "aromaType", expression = "java(product.aromaType != null ? product.aromaType.name() : null)")
    @Mapping(target = "qualityGrade", expression = "java(product.qualityGrade != null ? product.qualityGrade.name() : null)")
    @Mapping(target = "usageTypes", expression = "java(product.usageTypes == null ? java.util.List.of() : product.usageTypes.stream().map(u -> u.name).toList())")
    ProductResponse mapToDTO(Product product);

    List<ProductResponse> mapToDTOs(List<Product> products);
}
