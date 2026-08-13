-- V1.4 Create store_products table
CREATE TABLE store_products (
    id UUID PRIMARY KEY,
    store_id UUID NOT NULL,
    product_id UUID NOT NULL,
    external_id VARCHAR(255) NOT NULL,
    sku VARCHAR(255) NOT NULL,
    external_name VARCHAR(255) NOT NULL,
    url VARCHAR(500) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    CONSTRAINT fk_store_products_store FOREIGN KEY (store_id) REFERENCES stores(id),
    CONSTRAINT fk_store_products_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT unique_store_product UNIQUE (store_id, product_id)
);

CREATE INDEX idx_store_products_store_product ON store_products(store_id, product_id);
CREATE INDEX idx_store_products_external_id ON store_products(external_id);
