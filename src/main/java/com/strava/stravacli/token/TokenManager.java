package com.strava.stravacli.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strava.stravacli.config.StravaConfig;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.*;
import java.time.Instant;
import java.util.Map;

@Service
public class TokenManager {

    private static final String TOKEN_FILE = ".strava_token.json";
    private static final String STRAVA_TOKEN_URL = "https://www.strava.com/api/v3/oauth/token";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    public TokenManager(StravaConfig config) {
        this.clientId = config.getClientId();
        this.clientSecret = config.getClientSecret();
        this.redirectUri = config.getRedirectUri();
    }

    /** Load token from local file if exists */
    public TokenData loadToken() {
        Path path = Paths.get(TOKEN_FILE);
        if (Files.exists(path)) {
            try {
                return objectMapper.readValue(Files.readString(path), TokenData.class);
            } catch (IOException e) {
                System.out.println("⚠️ Could not read token file: " + e.getMessage());
            }
        }
        return null;
    }

    /** Save token to local file */
    public void saveToken(TokenData token) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(Paths.get(TOKEN_FILE).toFile(), token);
        } catch (IOException e) {
            System.out.println("⚠️ Could not save token: " + e.getMessage());
        }
    }

    /** Delete saved token */
    public void clearToken() {
        try {
            Files.deleteIfExists(Paths.get(TOKEN_FILE));
            System.out.println("✅ Token cleared successfully.");
        } catch (IOException e) {
            System.out.println("⚠️ Could not delete token: " + e.getMessage());
        }
    }

    /** Check if token is expired */
    public boolean isExpired(TokenData token) {
        return Instant.now().getEpochSecond() >= token.getExpiresAt();
    }

    public TokenData exchangeAuthorizationCode(String code) {
        System.out.println("🔁 Exchanging authorization code for token...");

        String body = String.format(
                "client_id=%s&client_secret=%s&code=%s&grant_type=authorization_code",
                clientId, clientSecret, code
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.strava.com/oauth/token"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Map<String, Object> map = objectMapper.readValue(response.body(), Map.class);
                TokenData tokenData = new TokenData(
                        (String) map.get("access_token"),
                        (String) map.get("refresh_token"),
                        ((Number) map.get("expires_at")).longValue()
                );
                saveToken(tokenData);
                System.out.println("✅ Token received and saved successfully!");
                return tokenData;
            } else {
                System.out.println("❌ Failed to exchange token. Status: " + response.statusCode());
                System.out.println(response.body());
            }
        } catch (Exception e) {
            System.out.println("❌ Error exchanging code: " + e.getMessage());
        }

        return null;
    }

    /** Refresh an expired token */
    public TokenData refreshToken(TokenData oldToken) {
        System.out.println("🔄 Refreshing expired token...");

        String body = String.format(
                "client_id=%s&client_secret=%s&grant_type=refresh_token&refresh_token=%s",
                clientId, clientSecret, oldToken.getRefreshToken()
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(STRAVA_TOKEN_URL))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                Map<String, Object> map = objectMapper.readValue(response.body(), Map.class);
                TokenData newToken = new TokenData(
                        (String) map.get("access_token"),
                        (String) map.get("refresh_token"),
                        ((Number) map.get("expires_at")).longValue()
                );
                saveToken(newToken);
                System.out.println("✅ Token refreshed successfully.");
                return newToken;
            } else {
                System.out.println("❌ Failed to refresh token: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            System.out.println("❌ Error refreshing token: " + e.getMessage());
        }

        return null;
    }
}
