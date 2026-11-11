CREATE INDEX idx_products_category_id
    ON products(category_id);

CREATE INDEX idx_products_variety_id
    ON products(variety_id);

CREATE INDEX idx_products_origin_id
    ON products(origin_id);

CREATE INDEX idx_product_usage_product_id
    ON product_usage_types(product_id);

CREATE INDEX idx_product_usage_usage_type_id
    ON product_usage_types(usage_type_id);