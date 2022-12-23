--liquibase formatted sql

--changeset dev:1
CREATE TABLE payment
(
    id          SERIAL PRIMARY KEY,
    payment_uid uuid UNIQUE NOT NULL,
    status      VARCHAR(20) NOT NULL
        CHECK (status IN ('PAID', 'CANCELED')),
    price       INT         NOT NULL
);
--rollback DROP TABLE IF EXISTS payment;
