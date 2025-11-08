package com.strava.stravacli.token;

public class TokenData {
    private String accessToken;
    private String refreshToken;
    private long expiresAt;

    public TokenData() {}

    public TokenData(String accessToken, String refreshToken, long expiresAt) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }

    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public long getExpiresAt() { return expiresAt; }

    public void setAccessToken(String t) { this.accessToken = t; }
    public void setRefreshToken(String t) { this.refreshToken = t; }
    public void setExpiresAt(long e) { this.expiresAt = e; }
}
