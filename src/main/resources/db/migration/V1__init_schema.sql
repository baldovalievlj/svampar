CREATE SCHEMA IF NOT EXISTS configuration;
CREATE SCHEMA IF NOT EXISTS "order";
CREATE SCHEMA IF NOT EXISTS seller;
CREATE SCHEMA IF NOT EXISTS "user";

CREATE TABLE IF NOT EXISTS configuration.configuration
(
    id    SERIAL PRIMARY KEY,
    key   VARCHAR(255) NOT NULL UNIQUE,
    value TEXT         NOT NULL,
    type  INTEGER      NOT NULL
);

CREATE TABLE IF NOT EXISTS "user"."user"
(
    id           SERIAL PRIMARY KEY,
    username     VARCHAR(128)          NOT NULL
        CONSTRAINT user_username_unique UNIQUE,
    password     VARCHAR(128)          NOT NULL,
    email        VARCHAR(128)          NOT NULL,
    first_name   VARCHAR(30)           NOT NULL,
    last_name    VARCHAR(30)           NOT NULL,
    phone_number VARCHAR(30),
    role         INTEGER               NOT NULL,
    deleted      BOOLEAN DEFAULT FALSE NOT NULL
);

CREATE TABLE IF NOT EXISTS "order".type
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(30)           NOT NULL,
    description TEXT,
    deleted     BOOLEAN DEFAULT FALSE NOT NULL
);

CREATE TABLE IF NOT EXISTS seller.seller
(
    id                     SERIAL PRIMARY KEY,
    name                   VARCHAR(128)          NOT NULL,
    address                VARCHAR(128),
    phone_number           VARCHAR(15),
    deleted                BOOLEAN DEFAULT FALSE NOT NULL,
    social_security_number VARCHAR(10)           NOT NULL
        CONSTRAINT seller_social_security_number UNIQUE,
    email                  VARCHAR(30)           NOT NULL,
    additional_info        VARCHAR(300)
);

CREATE TABLE IF NOT EXISTS "order"."order"
(
    id           SERIAL PRIMARY KEY,
    user_id      INTEGER   NOT NULL
        CONSTRAINT fk_order_user_id__id REFERENCES "user"."user" ON UPDATE RESTRICT ON DELETE RESTRICT,
    date_created TIMESTAMP NOT NULL,
    details      TEXT,
    seller_id    INTEGER   NOT NULL
        CONSTRAINT fk_order_seller_id__id REFERENCES seller.seller ON UPDATE RESTRICT ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS "order".order_item
(
    id       SERIAL PRIMARY KEY,
    order_id INTEGER        NOT NULL
        CONSTRAINT fk_order_item_order_id__id REFERENCES "order"."order" ON UPDATE RESTRICT ON DELETE RESTRICT,
    comment  TEXT,
    type_id  INTEGER        NOT NULL
        CONSTRAINT fk_order_item_category_id__id REFERENCES "order".type ON UPDATE RESTRICT ON DELETE RESTRICT,
    amount   DECIMAL(10, 2) NOT NULL,
    price    DECIMAL(10, 2) NOT NULL
);
