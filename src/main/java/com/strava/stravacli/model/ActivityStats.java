package com.strava.stravacli.model;

import java.time.Duration;

public record ActivityStats(
        int totalActivities,
        double totalDistanceKm,
        double averageDistanceKm,
        double longestDistanceKm,
        Duration totalMovingTime,
        double averageSpeedKmh,
        double totalElevationGain,
        String mostFrequentActivityType,
        double medianDistanceKm,
        int bestWeekNumber,
        double bestWeekDistanceKm,
        int longestStreakDays,
        double elevationPerKm,
        Duration averagePacePerKm
) {
    @Override
    public String toString() {
        return """
               📊 Activity Statistics
               ------------------------
               Total Activities      : %d
               Total Distance        : %.2f km
               Average Distance      : %.2f km
               Longest Distance      : %.2f km
               Total Elevation Gain  : %.2f m
               Average Speed         : %.2f km/h
               Longest Streak        : %d days
               Best Week Number      : %d
               Best Week Distance    : %.2f km
               """.formatted(
                totalActivities,
                totalDistanceKm,
                averageDistanceKm,
                longestDistanceKm,
                totalElevationGain,
                averageSpeedKmh,
                longestStreakDays,
                bestWeekNumber,
                bestWeekDistanceKm
        );
    }
}