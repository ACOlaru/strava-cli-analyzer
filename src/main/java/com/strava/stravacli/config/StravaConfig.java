package com.strava.stravacli.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import io.github.cdimascio.dotenv.Dotenv;


@Component
@ConfigurationProperties(prefix = "strava")
public class StravaConfig {

    private String accessToken;
    private String baseUrl;
    private String clientId;
    private String clientSecret;
    private String redirectUri;

    public StravaConfig() {
        Dotenv dotenv = Dotenv.configure().filename(".env").load();
        clientId = dotenv.get("STRAVA_CLIENT_ID");
        clientSecret = dotenv.get("STRAVA_CLIENT_SECRET");
        redirectUri = dotenv.get("STRAVA_REDIRECT_URI");
    }

    // Getter and Setter for accessToken
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    // Getter and Setter for baseUrl
    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
}
