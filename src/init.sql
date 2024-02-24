CREATE TABLE shoppinglist (
id               BIGSERIAL        PRIMARY KEY,
nameProduct      TEXT             NOT NULL,
quantity         TEXT,
price            TEXT,
created          timestamp        DEFAULT now()
)