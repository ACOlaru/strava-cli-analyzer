package com.strava.stravacli.cli;

import com.strava.stravacli.config.StravaConfig;
import com.strava.stravacli.client.StravaClient;
import com.strava.stravacli.model.Activity;
import com.strava.stravacli.services.PredictionService;
import com.strava.stravacli.services.StatisticsService;
import com.strava.stravacli.services.StravaService;
import com.strava.stravacli.token.TokenData;
import com.strava.stravacli.token.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class CliRunner implements CommandLineRunner {
    private StravaConfig stravaConfig;
    private StravaClient stravaClient;
    private StatisticsService statisticsService;
    private PredictionService predictionService;
    private StravaService stravaService;
    private TokenManager tokenManager;
    private static Scanner scanner = new Scanner(System.in);
    private String token;

    @Autowired
    public CliRunner(StravaConfig stravaConfig, StravaClient stravaClient, StatisticsService statisticsService, PredictionService predictionService, StravaService stravaService, TokenManager tokenManager) {
        this.stravaConfig = stravaConfig;
        this.stravaClient = stravaClient;
        this.statisticsService = statisticsService;
        this.predictionService = predictionService;
        this.stravaService = stravaService;
        this.tokenManager = tokenManager;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Welcome to Strava Analyzer");
        System.out.println("----------------------------------");

        token = initializeToken();
        processMenu();
    }

    private String initializeToken() {
        TokenData token = tokenManager.loadToken();

        if (token != null) {
            if (tokenManager.isExpired(token)) {
                System.out.println("Token expired, refreshing...");
                token = tokenManager.refreshToken(token);
                System.out.println("Token refreshed successfully.");
            } else {
                System.out.println("Loaded existing token.");
            }
            return token.getAccessToken();
        }

        System.out.println("No valid token found. Please authorize:");
        token = authorize();
        System.out.println("Token saved successfully.");
        return token.getAccessToken();
    }

    private TokenData authorize() {
        String authUrl = String.format(
                "https://www.strava.com/oauth/authorize?client_id=%s&response_type=code&redirect_uri=%s&approval_prompt=force&scope=activity:read_all",
                stravaConfig.getClientId(), stravaConfig.getRedirectUri()
        );

        System.out.println("👉 Open this URL in your browser to authorize:");
        System.out.println(authUrl);
        System.out.println("\nAfter approving, copy the 'code' from the redirected URL and paste it below.");

        System.out.print("Enter authorization code: ");
        String code = scanner.nextLine().trim();

        return tokenManager.exchangeAuthorizationCode(code);
    }

    private void processMenu() {
        while (true) {
            printMenu();
            selectMenuItem();
            shouldContinueApplication();
        }
    }

    private void selectMenuItem() {
        String option = scanner.nextLine().trim().toLowerCase();
        switch (option) {
            case "0":
                tokenManager.clearToken();
                token = initializeToken();
                break;
            case "1":
                fetchActivities();
                break;
            case "2":
                showStatistics();
                break;
            case "3":
                showPredictions();
                break;
            case "4":
                System.exit(0);
            default:
                System.out.println("Invalid option");
        }
    }

    private String getToken() {
        System.out.println("Enter your Strava Access Token:");
        return scanner.nextLine().trim();
    }

    private static void printMenu() {
        System.out.println("Please choose one of the following options:");
        System.out.println("0. Reset token");
        System.out.println("1. Fetch activities");
        System.out.println("2. Print statistics");
        System.out.println("3. Print predictions");
        System.out.println("4. Exit");
    }

    private static boolean askYesNo(String message, boolean defaultValue) {
        while (true) {
            String defaultHint = defaultValue ? " (Y/n/exit): " : " (y/N/exit): ";
            System.out.print(message + defaultHint);

            String input = scanner.nextLine().trim().toLowerCase();

            if (input.isEmpty()) {
                return defaultValue;
            }

            if (input.startsWith("y")) return true;
            if (input.startsWith("n")) return false;
            if (input.startsWith("e")) {
                System.out.println("Exiting....");
                System.exit(0);
            };

            System.out.println("Please answer 'y', 'n' or 'exit'.");
        }
    }

    private void shouldContinueApplication() {
        boolean shouldContinue = askYesNo("Do you want to continue", true);
        if (!shouldContinue) {
            System.out.println("Exiting....");
            System.exit(0);
        }
    }

    private void showPredictions() {
        System.out.println("Will be released");
    }

    private void showStatistics() {
        System.out.println("Will be released");
    }

    private void fetchActivities() {
        List<Activity> activities = stravaClient.fetchRecentActivities(token);
        System.out.println(activities);
    }
}
