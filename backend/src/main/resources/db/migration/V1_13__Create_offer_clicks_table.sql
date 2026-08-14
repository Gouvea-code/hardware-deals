CREATE TABLE offer_clicks (
    id UUID PRIMARY KEY,
    user_id UUID,
    product_id UUID NOT NULL,
    store_id UUID NOT NULL,
    offer_id UUID NOT NULL,
    clicked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_offer_clicks_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_offer_clicks_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_offer_clicks_store FOREIGN KEY (store_id) REFERENCES stores(id),
    CONSTRAINT fk_offer_clicks_offer FOREIGN KEY (offer_id) REFERENCES offers(id)
);

CREATE INDEX idx_offer_clicks_offer_id ON offer_clicks(offer_id);
CREATE INDEX idx_offer_clicks_user_id ON offer_clicks(user_id);
CREATE INDEX idx_offer_clicks_clicked_at ON offer_clicks(clicked_at);
