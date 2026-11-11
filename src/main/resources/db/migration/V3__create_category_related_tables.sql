CREATE TABLE categories (
                            id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            description TEXT,

                            created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                            updated_at TIMESTAMP NOT NULL DEFAULT NOW(),

                            CONSTRAINT uq_categories_name UNIQUE(name)
);

CREATE TABLE rice_varieties (
                                id BIGSERIAL PRIMARY KEY,
                                name VARCHAR(100) NOT NULL,
                                description TEXT,

                                CONSTRAINT uq_rice_varieties_name UNIQUE(name)
);

CREATE TABLE origins (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(100) NOT NULL UNIQUE,

                         created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                         updated_at TIMESTAMP NOT NULL DEFAULT NOW(),

                         CONSTRAINT uq_origins_name UNIQUE(name)
);

CREATE TABLE usage_types (
                             id BIGSERIAL PRIMARY KEY,
                             name VARCHAR(50) NOT NULL,

                             CONSTRAINT uq_usage_types_name UNIQUE (name)
);

INSERT INTO categories (name, description) VALUES
            ('Basmati Rice', 'Premium aromatic rice'),
            ('Sona Masoori Rice', 'Lightweight daily-use rice'),
            ('Ponni Rice', 'Popular South Indian rice'),
            ('Brown Rice', 'Whole grain rice'),
            ('Organic Rice', 'Organically cultivated rice');

INSERT INTO rice_varieties (name) VALUES
            ('Basmati'),
            ('Sona Masoori'),
            ('Ponni'),
            ('Jeera Samba'),
            ('Jasmine Rice');

INSERT INTO origins (name) VALUES
            ('Andhra Pradesh'),
            ('Tamil Nadu'),
            ('Punjab'),
            ('Haryana'),
            ('West Bengal');

INSERT INTO usage_types (name) VALUES
            ('Daily Use'),
            ('Biryani'),
            ('Idli/Dosa'),
            ('Sushi');