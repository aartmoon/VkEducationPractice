CREATE TABLE IF NOT EXISTS people
(
    id        BIGSERIAL PRIMARY KEY,
    nickname  TEXT NOT NULL,
    create_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS segments
(
    id      BIGSERIAL PRIMARY KEY,
    bio     TEXT NOT NULL,
    percent INT
);

CREATE TABLE IF NOT EXISTS info
(
    idUser    BIGINT,
    idSegment BIGINT,
    PRIMARY KEY (idUser, idSegment),
    FOREIGN KEY (idUser) REFERENCES people (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (idSegment) REFERENCES segments (id) ON DELETE CASCADE ON UPDATE CASCADE
);

