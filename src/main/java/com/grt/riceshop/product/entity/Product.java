package com.grt.riceshop.product.entity;

import com.grt.riceshop.product.enums.AromaType;
import com.grt.riceshop.product.enums.GrainType;
import com.grt.riceshop.product.enums.ProcessingType;
import com.grt.riceshop.product.enums.QualityGrade;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "product_name", nullable = false, length = 255)
    public String productName;

    @Column(name = "brand_name", length = 100)
    public String brandName;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    public Category category;

    @ManyToOne
    @JoinColumn(name = "variety_id", nullable = false)
    public RiceVariety variety;

    @ManyToOne
    @JoinColumn(name = "origin_id", nullable = false)
    public Origin origin;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Enumerated(EnumType.STRING)
    @Column(name = "grain_type", nullable = false, columnDefinition = "grain_type")
    public GrainType grainType;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Enumerated(EnumType.STRING)
    @Column(name = "processing_type", nullable = false, columnDefinition = "processing_type")
    public ProcessingType processingType;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Enumerated(EnumType.STRING)
    @Column(name = "aroma_type", nullable = false, columnDefinition = "aroma_type")
    public AromaType aromaType;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Enumerated(EnumType.STRING)
    @Column(name = "quality_grade", nullable = false, columnDefinition = "quality_grade")
    public QualityGrade qualityGrade;

    @Column(name = "weight_kg", nullable = false, precision = 10, scale = 2)
    public BigDecimal weightKg;

    @Column(nullable = false, precision = 12, scale = 2)
    public BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    public Integer stockQuantity = 0;

    @Column(columnDefinition = "TEXT")
    public String description;

    @Column(name = "is_active", nullable = false)
    public Boolean isActive = true;

    @Column(name = "created_at", nullable = false)
    public Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt;

    @ManyToMany
    @JoinTable(
            name = "product_usage_types",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "usage_type_id")
    )
    public Set<UsageType> usageTypes = new HashSet<>();
}
