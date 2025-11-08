package com.strava.stravacli.services;

import com.strava.stravacli.client.StravaClient;
import com.strava.stravacli.model.Activity;
import com.strava.stravacli.token.TokenData;
import com.strava.stravacli.token.TokenManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StravaService {
    private final StravaClient client;
    private final TokenManager tokenManager;

    public StravaService(StravaClient client, TokenManager tokenManager) {
        this.client = client;
        this.tokenManager = tokenManager;
    }

    public List<Activity> getActivities() throws Exception {
        TokenData tokenData = tokenManager.loadToken();

        if (tokenData == null) throw new RuntimeException("No token found");

        if (tokenManager.isExpired(tokenData)) {
            tokenData = tokenManager.refreshToken(tokenData);
        }

        return client.fetchRecentActivities(tokenData.getAccessToken());
    }
}
