CREATE TABLE people (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    nick VARCHAR(32) NOT NULL,
    birth_date DATE NOT NULL,
    stack TEXT NOT NULL,
    search TEXT NOT NULL,
    FULLTEXT KEY (search),
    CONSTRAINT unique_nick UNIQUE (nick)
) engine="MyISAM";
