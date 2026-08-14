package com.hardwaredeals.collector;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.marketplaces")
public class MarketplaceProperties {
    private Channel mercadoLivre = new Channel();
    private Channel amazonBrasil = new Channel();
    private Channel kabum = new Channel();
    private Channel magazineLuiza = new Channel();

    public Channel getMercadoLivre() { return mercadoLivre; }
    public void setMercadoLivre(Channel value) { mercadoLivre = value; }
    public Channel getAmazonBrasil() { return amazonBrasil; }
    public void setAmazonBrasil(Channel value) { amazonBrasil = value; }
    public Channel getKabum() { return kabum; }
    public void setKabum(Channel value) { kabum = value; }
    public Channel getMagazineLuiza() { return magazineLuiza; }
    public void setMagazineLuiza(Channel value) { magazineLuiza = value; }

    public static class Channel {
        private boolean enabled;
        private String apiUrl;
        private String accessToken;
        private String query = "hardware computador";

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean value) { enabled = value; }
        public String getApiUrl() { return apiUrl; }
        public void setApiUrl(String value) { apiUrl = value; }
        public String getAccessToken() { return accessToken; }
        public void setAccessToken(String value) { accessToken = value; }
        public String getQuery() { return query; }
        public void setQuery(String value) { query = value; }
    }
}
