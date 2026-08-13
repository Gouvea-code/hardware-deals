-- V1.5 Create offers table
CREATE TABLE offers (
    id UUID PRIMARY KEY,
    store_product_id UUID NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    original_price DECIMAL(19, 2) NOT NULL,
    coupon VARCHAR(255),
    available BOOLEAN NOT NULL DEFAULT true,
    collected_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_offers_store_product FOREIGN KEY (store_product_id) REFERENCES store_products(id)
);

CREATE INDEX idx_store_product_id ON offers(store_product_id);
CREATE INDEX idx_collected_at ON offers(collected_at);
