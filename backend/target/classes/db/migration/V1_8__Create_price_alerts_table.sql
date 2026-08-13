-- V1.8 Create price_alerts table
CREATE TABLE price_alerts (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    product_id UUID NOT NULL,
    target_price DECIMAL(19, 2) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    last_notified_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_price_alerts_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_price_alerts_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT unique_user_product_alert UNIQUE (user_id, product_id)
);

CREATE INDEX idx_user_product_unique ON price_alerts(user_id, product_id);
