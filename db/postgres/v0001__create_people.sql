-- Create table
CREATE TABLE people (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    nick VARCHAR(32) NOT NULL,
    birth_date DATE NOT NULL,
    stack VARCHAR(32)[],
    search TEXT NOT NULL,
    CONSTRAINT unique_nick UNIQUE (nick)
);

-- Create index
CREATE EXTENSION pg_trgm;
CREATE INDEX CONCURRENTLY IF NOT EXISTS people_search_idx ON people USING GIN (search gin_trgm_ops);
-- CREATE INDEX CONCURRENTLY IF NOT EXISTS people_search_idx ON people USING GIST (search GIST_TRGM_OPS(SIGLEN=64));
-- CREATE INDEX CONCURRENTLY IF NOT EXISTS people_search_idx ON people USING GIN (search gin_trgm_ops) WITH (fastupdate = off);

-- Disable Logs
ALTER SYSTEM SET log_statement TO 'none';

-- Debug
CREATE EXTENSION pg_stat_statements;
ALTER SYSTEM SET shared_preload_libraries = 'pg_stat_statements';

-- Update config
ALTER SYSTEM set shared_buffers = '600MB';
ALTER SYSTEM set work_mem = '8MB';
