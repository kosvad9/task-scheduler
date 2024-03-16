--liquibase formatted sql

--changeset kosvad9:1
CREATE TABLE users(
    id BIGSERIAL PRIMARY KEY,
    login VARCHAR(64) UNIQUE NOT NULL ,
    password VARCHAR NOT NULL
);

--changeset kosvad9:2
CREATE TABLE tasks(
    id BIGSERIAL PRIMARY KEY,
    header VARCHAR(64) DEFAULT 'Без названия',
    text VARCHAR,
    status BOOLEAN DEFAULT FALSE,
    complete_time timestamp,
    id_user BIGINT REFERENCES users(id) NOT NULL
);

--changeset kosvad9:3
CREATE TABLE deactivated_tokens(
    id uuid PRIMARY KEY,
    expires_at timestamp
)