CREATE TABLE analytics_events (
    id UUID PRIMARY KEY,
    event_type VARCHAR(50) NOT NULL,
    user_id UUID,
    product_id UUID,
    offer_id UUID,
    notification_id UUID,
    occurred_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_analytics_events_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_analytics_events_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_analytics_events_offer FOREIGN KEY (offer_id) REFERENCES offers(id),
    CONSTRAINT fk_analytics_events_notification FOREIGN KEY (notification_id) REFERENCES notifications(id),
    CONSTRAINT chk_analytics_events_type CHECK (event_type IN
        ('APP_OPEN','SEARCH','PRODUCT_VIEW','FAVORITE','ALERT_CREATED','NOTIFICATION_OPEN','OFFER_CLICK'))
);

CREATE INDEX idx_analytics_events_type_occurred ON analytics_events(event_type, occurred_at);
CREATE INDEX idx_analytics_events_user_id ON analytics_events(user_id);
CREATE INDEX idx_analytics_events_occurred_at ON analytics_events(occurred_at);
