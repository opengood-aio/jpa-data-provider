CREATE TABLE dbo.products
(
    id   uuid PRIMARY KEY NOT NULL,
    name varchar(256)     NOT NULL,
    sku  varchar(256)     NOT NULL
);