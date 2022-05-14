CREATE TABLE airplanes
(
    id       BIGSERIAL PRIMARY KEY,
    name     TEXT    NOT NULL UNIQUE,
    maxSpeed INT     NOT NULL,
    weight   INT     NOT NULL,
    year     INT     NOT NULL,
    photo    TEXT    NOT NULL,
    removed  BOOLEAN NOT NULL DEFAULT FALSE,
    price    INT     NOT NULL CHECK ( price >= 0 )
);

CREATE TABLE users
(
    id       BIGSERIAL PRIMARY KEY,
    login    TEXT    NOT NULL UNIQUE,
    password TEXT    NOT NULL,
    role     TEXT    NOT NULL,
    removed  BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE applications
(
    id          BIGSERIAL PRIMARY KEY,
    airplane_id BIGINT REFERENCES airplanes NOT NULL,
    email       TEXT                        NOT NULL,
    processed   BOOLEAN DEFAULT FALSE
);
