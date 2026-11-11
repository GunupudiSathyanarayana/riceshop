CREATE TABLE products (
                          id BIGSERIAL PRIMARY KEY,
                          product_name VARCHAR(255) NOT NULL,
                          brand_name VARCHAR(100),
                          category_id BIGINT NOT NULL,
                          variety_id BIGINT NOT NULL,
                          origin_id BIGINT NOT NULL,
                          grain_type grain_type NOT NULL,
                          processing_type processing_type NOT NULL,
                          aroma_type aroma_type NOT NULL,
                          quality_grade quality_grade NOT NULL,
                          weight_kg NUMERIC(10,2) NOT NULL,
                          price NUMERIC(12,2) NOT NULL,
                          stock_quantity INTEGER NOT NULL DEFAULT 0,
                          description TEXT,
                          is_active BOOLEAN NOT NULL DEFAULT TRUE,
                          created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                          updated_at TIMESTAMP NOT NULL DEFAULT NOW(),

                          CONSTRAINT chk_products_price
                              CHECK (price >= 0),

                          CONSTRAINT chk_products_stock_quantity
                              CHECK (stock_quantity >= 0),

                          CONSTRAINT chk_products_weight
                              CHECK (weight_kg > 0),

                          CONSTRAINT fk_products_category
                              FOREIGN KEY (category_id)
                                  REFERENCES categories(id),

                          CONSTRAINT fk_products_variety
                              FOREIGN KEY (variety_id)
                                  REFERENCES rice_varieties(id),

                          CONSTRAINT fk_products_origin
                              FOREIGN KEY (origin_id)
                                  REFERENCES origins(id)
);


CREATE TABLE product_usage_types (
                                     product_id BIGINT NOT NULL,
                                     usage_type_id BIGINT NOT NULL,

                                     PRIMARY KEY (
                                                  product_id,
                                                  usage_type_id
                                         ),

                                     CONSTRAINT fk_product_usage_product
                                         FOREIGN KEY (product_id)
                                             REFERENCES products(id)
                                             ON DELETE CASCADE,

                                     CONSTRAINT fk_product_usage_usage_type
                                         FOREIGN KEY (usage_type_id)
                                             REFERENCES usage_types(id)
                                             ON DELETE CASCADE
);