package com.hardwaredeals.collector;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.collector")
public class CollectorProperties {
    private boolean enabled;
    private String feedUrl;
    private String cron = "0 0 * * * *";

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public String getFeedUrl() { return feedUrl; }
    public void setFeedUrl(String feedUrl) { this.feedUrl = feedUrl; }
    public String getCron() { return cron; }
    public void setCron(String cron) { this.cron = cron; }
}
