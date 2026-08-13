-- V1.6 Create price_history table
CREATE TABLE price_history (
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL,
    store_id UUID NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    collected_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_price_history_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_price_history_store FOREIGN KEY (store_id) REFERENCES stores(id)
);

CREATE INDEX idx_price_history_product_id ON price_history(product_id);
CREATE INDEX idx_price_history_store_id ON price_history(store_id);
CREATE INDEX idx_price_history_collected_at ON price_history(collected_at);
