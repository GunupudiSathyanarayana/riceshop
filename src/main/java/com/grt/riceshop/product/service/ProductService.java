package com.grt.riceshop.product.service;

import com.grt.riceshop.product.dto.CreateProductRequest;
import com.grt.riceshop.product.dto.ProductResponse;
import com.grt.riceshop.product.entity.Category;
import com.grt.riceshop.product.entity.Origin;
import com.grt.riceshop.product.entity.Product;
import com.grt.riceshop.product.entity.RiceVariety;
import com.grt.riceshop.product.entity.UsageType;
import com.grt.riceshop.product.enums.AromaType;
import com.grt.riceshop.product.enums.GrainType;
import com.grt.riceshop.product.enums.ProcessingType;
import com.grt.riceshop.product.enums.QualityGrade;
import com.grt.riceshop.product.mapper.ProductMapper;
import com.grt.riceshop.product.repository.CategoryRepository;
import com.grt.riceshop.product.repository.OriginRepository;
import com.grt.riceshop.product.repository.ProductRepository;
import com.grt.riceshop.product.repository.RiceVarietyRepository;
import com.grt.riceshop.product.repository.UsageTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final RiceVarietyRepository riceVarietyRepository;
    private final OriginRepository originRepository;
    private final UsageTypeRepository usageTypeRepository;
    private final ProductMapper productMapper;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            RiceVarietyRepository riceVarietyRepository,
            OriginRepository originRepository,
            UsageTypeRepository usageTypeRepository,
            ProductMapper productMapper
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.riceVarietyRepository = riceVarietyRepository;
        this.originRepository = originRepository;
        this.usageTypeRepository = usageTypeRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        Instant now = Instant.now();

        Category category = findCategory(request.category());
        RiceVariety variety = findVariety(request.variety());
        Origin origin = findOrigin(request.origin());
        List<UsageType> usageTypes = findUsageTypes(request.usageTypes());

        Product product = new Product();
        product.productName = request.productName();
        product.brandName = request.brandName();
        product.category = category;
        product.variety = variety;
        product.origin = origin;
        product.grainType = GrainType.valueOf(normalizeEnumValue(request.grainType(), GrainType.class));
        product.processingType = ProcessingType.valueOf(normalizeEnumValue(request.processingType(), ProcessingType.class));
        product.aromaType = AromaType.valueOf(normalizeEnumValue(request.aromaType(), AromaType.class));
        product.qualityGrade = QualityGrade.valueOf(normalizeEnumValue(request.qualityGrade(), QualityGrade.class));
        product.weightKg = request.weightKg();
        product.price = request.price();
        product.stockQuantity = request.stockQuantity() == null ? 0 : request.stockQuantity();
        product.description = request.description();
        product.isActive = true;
        product.createdAt = now;
        product.updatedAt = now;
        product.usageTypes = Set.copyOf(usageTypes);

        Product savedProduct = productRepository.save(product);
        return productMapper.mapToDTO(savedProduct);
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.mapToDTO(product);
    }

    public List<ProductResponse> getAllProducts() {
        return productMapper.mapToDTOs(productRepository.findAll());
    }

    private Category findCategory(String text) {
        return categoryRepository.findByNameIgnoreCase(text)
                .or(() -> categoryRepository.findByNameContainingIgnoreCase(text))
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + text));
    }

    private RiceVariety findVariety(String text) {
        return riceVarietyRepository.findByNameIgnoreCase(text)
                .or(() -> riceVarietyRepository.findByNameContainingIgnoreCase(text))
                .orElseThrow(() -> new IllegalArgumentException("Variety not found: " + text));
    }

    private Origin findOrigin(String text) {
        return originRepository.findByNameIgnoreCase(text)
                .or(() -> originRepository.findByNameContainingIgnoreCase(text))
                .orElseThrow(() -> new IllegalArgumentException("Origin not found: " + text));
    }

    private List<UsageType> findUsageTypes(List<String> rawNames) {
        if (rawNames == null || rawNames.isEmpty()) {
            return List.of();
        }

        List<UsageType> usageTypes = new ArrayList<>();
        for (String rawName : rawNames) {
            String value = Objects.toString(rawName, "").trim();
            if (value.isEmpty()) {
                continue;
            }

            UsageType usageType = usageTypeRepository.findByNameInIgnoreCase(List.of(value)).stream()
                    .findFirst()
                    .or(() -> usageTypeRepository.findByNameContainingIgnoreCase(value))
                    .orElseThrow(() -> new IllegalArgumentException("Usage type not found: " + value));

            usageTypes.add(usageType);
        }

        return usageTypes;
    }

    private <E extends Enum<E>> String normalizeEnumValue(String rawValue, Class<E> enumType) {
        String normalized = rawValue == null ? "" : rawValue.trim();
        for (E enumConstant : enumType.getEnumConstants()) {
            if (enumConstant.name().equalsIgnoreCase(normalized)
                    || enumConstant.name().replace("_", " ").equalsIgnoreCase(normalized.replace("_", " "))
                    || enumConstant.name().replace("_", "-").equalsIgnoreCase(normalized.replace("_", "-"))) {
                return enumConstant.name();
            }
        }

        throw new IllegalArgumentException("Invalid value for " + enumType.getSimpleName() + ": " + rawValue);
    }
}
