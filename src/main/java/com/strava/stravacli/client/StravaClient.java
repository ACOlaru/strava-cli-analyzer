package com.strava.stravacli.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.strava.stravacli.config.StravaConfig;
import com.strava.stravacli.model.Activity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

@Service
public class StravaClient {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final StravaConfig config;

    public StravaClient(StravaConfig config) {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.config = config;
    }

    public List<Activity> fetchRecentActivities(String accessToken) {
        try {
            System.out.println("\n🔄 Fetching your recent Strava activities...");

            String url = config.getBaseUrl() + "/athlete/activities?per_page=10";
            HttpRequest request = getRequest(accessToken, url);

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<Activity> activities = objectMapper.readValue(
                        response.body(),
                        new TypeReference<>() {}
                );

                System.out.println("✅ Successfully fetched " + activities.size() + " activities.");
                return activities;

            } else if (response.statusCode() == 401) {
                System.out.println("⚠️  Unauthorized: Invalid or expired access token.");
                throw new RuntimeException("Invalid token");
            } else {
                System.out.println("❌  Error fetching data: HTTP " + response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("🚫  Network error while fetching activities: " + e.getMessage());
        }

        return Collections.emptyList();
    }

    private static HttpRequest getRequest(String accessToken, String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .header("Accept", "application/json")
                .GET()
                .build();
    }
}
