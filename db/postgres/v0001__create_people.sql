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
CREATE INDEX CONCURRENTLY IF NOT EXISTS people_search_idx ON people USING GIST (search GIST_TRGM_OPS(SIGLEN=64));
-- CREATE INDEX CONCURRENTLY IF NOT EXISTS people_search_idx ON people USING GIN (search gin_trgm_ops);
-- CREATE INDEX CONCURRENTLY IF NOT EXISTS people_search_idx ON people USING GIN (search gin_trgm_ops) WITH (fastupdate = off);

-- Disable Logs
ALTER SYSTEM SET log_statement TO 'none';
ALTER SYSTEM SET log_lock_waits TO 'on';
ALTER SYSTEM SET log_min_messages TO 'panic';
ALTER SYSTEM SET log_min_error_statement TO 'panic';

-- Debug
-- CREATE EXTENSION pg_stat_statements;
-- ALTER SYSTEM SET shared_preload_libraries = 'pg_stat_statements';

-- Update config
ALTER SYSTEM SET autovacuum = 'on';
ALTER SYSTEM SET superuser_reserved_connections = '3';
ALTER SYSTEM SET shared_buffers = '512MB';
ALTER SYSTEM SET maintenance_work_mem = '256MB';
ALTER SYSTEM SET wal_buffers = '64MB';
ALTER SYSTEM SET work_mem = '2MB';
ALTER SYSTEM SET max_connections = '500';
ALTER SYSTEM SET random_page_cost = '4.0';
ALTER SYSTEM SET effective_io_concurrency = '2';