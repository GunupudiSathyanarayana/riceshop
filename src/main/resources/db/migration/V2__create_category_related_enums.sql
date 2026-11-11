CREATE TYPE grain_type AS ENUM (
    'LONG_GRAIN',
    'MEDIUM_GRAIN',
    'SHORT_GRAIN'
);

CREATE TYPE processing_type AS ENUM (
    'RAW',
    'PARBOILED',
    'WHITE',
    'BROWN'
);

CREATE TYPE aroma_type AS ENUM (
    'AROMATIC',
    'NON_AROMATIC'
);

CREATE TYPE quality_grade AS ENUM (
    'PREMIUM',
    'STANDARD',
    'ECONOMY'
);