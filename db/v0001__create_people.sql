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

