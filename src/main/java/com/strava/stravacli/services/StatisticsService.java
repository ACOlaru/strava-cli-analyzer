package com.strava.stravacli.services;

import com.strava.stravacli.model.Activity;
import com.strava.stravacli.model.ActivityStats;
import com.strava.stravacli.util.ActivityWrapper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {
    public ActivityStats getStatistics(List<Activity> activities) {
        return new ActivityStats(
                totalActivities(activities),
                totalDistanceKm(activities),
                averageDistanceKm(activities),
                longestDistanceKm(activities),
                totalMovingTime(activities),
                averageSpeedKmh(activities),
                totalElevationGain(activities),
                mostFrequentActivityType(activities),
                medianDistanceKm(activities),
                bestWeekNumber(activities),
                bestWeekDistance(activities),
                longestStreakDays(activities),
                elevationPerKm(activities),
                averagePacePerKm(activities)
        );
    }

    private List<ActivityWrapper> wrap(List<Activity> activities) {
        return activities.stream()
                .map(ActivityWrapper::new)
                .toList();
    }
    /**
     * Counts the total number of activities in the list.
     */
    public int totalActivities(List<Activity> activities) {
        return activities.size();
    }

    /**
     * Computes the total distance of all activities in kilometers.
     */
    public double totalDistanceKm(List<Activity> activities) {
        return activities.stream()
                .mapToDouble(Activity::getDistance)
                .sum() / 1000.0;
    }

    /**
     * Computes the average distance per activity in kilometers.
     */
    public double averageDistanceKm(List<Activity> activities) {
        int total = activities.size();
        return total == 0 ? 0.0 : totalDistanceKm(activities) / total;
    }

    /**
     * Returns the longest single activity distance in kilometers.
     */
    public double longestDistanceKm(List<Activity> activities) {
        return activities.stream()
                .mapToDouble(Activity::getDistance)
                .max()
                .orElse(0.0) / 1000.0;
    }

    /**
     * Computes total moving time across all activities as a Duration.
     */
    public Duration totalMovingTime(List<Activity> activities) {
        long seconds = activities.stream()
                .mapToLong(Activity::getMovingTime)
                .sum();
        return Duration.ofSeconds(seconds);
    }

    /**
     * Computes the average speed across all activities in km/h.
     */
    public double averageSpeedKmh(List<Activity> activities) {
        Duration totalTime = totalMovingTime(activities);
        double hours = totalTime.toSeconds() / 3600.0;
        double km = totalDistanceKm(activities);
        return hours == 0 ? 0.0 : km / hours;
    }

    /**
     * Computes the total elevation gain across all activities in meters.
     */
    public double totalElevationGain(List<Activity> activities) {
        return activities.stream()
                .mapToDouble(Activity::getElevationGain)
                .sum();
    }

    /**
     * Determines the most frequent activity type (e.g., Ride, Run).
     */
    public String mostFrequentActivityType(List<Activity> activities) {
        return activities.stream()
                .collect(Collectors.groupingBy(Activity::getType, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
    }

    /**
     * Computes the median distance across all activities (robust central tendency).
     */
    public double medianDistanceKm(List<Activity> activities) {
        double[] distances = activities.stream()
                .mapToDouble(Activity::getDistance)
                .sorted()
                .toArray();
        int n = distances.length;
        if (n == 0) return 0.0;
        double median;
        if (n % 2 == 1) {
            median = distances[n / 2];
        } else {
            median = (distances[(n / 2) - 1] + distances[n / 2]) / 2.0;
        }
        return median / 1000.0;
    }

    /**
     * Computes the ISO week number of the week with the highest total distance.
     * This method groups all activities by their ISO week (Monday-Sunday),
     * sums the distances for each week, and then finds the week with the maximum total distance.
     * Useful for identifying your most active week in terms of distance.
     *
     * @return ISO week number of the best week; returns 0 if the activity list is empty
     */
    public int bestWeekNumber(List<Activity> activities) {
        Map<Integer, Double> weekDistance = activities.stream()
                .collect(Collectors.groupingBy(
                        activity -> {
                            LocalDate date = LocalDateTime.parse(
                                    activity.getStartDateLocal(),
                                    DateTimeFormatter.ISO_DATE_TIME
                            ).toLocalDate();
                            return date.get(WeekFields.ISO.weekOfWeekBasedYear());
                        },
                        Collectors.summingDouble(activity -> activity.getDistance() / 1000.0)
                ));

        return weekDistance.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0);
    }

    /**
     * Returns the total distance (in km) of the week with the highest total distance.
     * Useful to quickly see how far you rode/run in your most active week.
     *
     * @return Total distance in kilometers of the best week; 0 if no activities exist
     */
    public double bestWeekDistance(List<Activity> activities) {
        Map<Integer, Double> weekDistance = activities.stream()
                .collect(Collectors.groupingBy(
                        activity -> {
                            LocalDate date = LocalDateTime.parse(
                                    activity.getStartDateLocal(),
                                    DateTimeFormatter.ISO_DATE_TIME
                            ).toLocalDate();
                            return date.get(WeekFields.ISO.weekOfWeekBasedYear());
                        },
                        Collectors.summingDouble(activity -> activity.getDistance() / 1000.0)
                ));

        return weekDistance.values().stream()
                .max(Double::compare)
                .orElse(0.0);
    }

    /**
     * Computes the longest streak of consecutive days with activity.
     */
    public int longestStreakDays(List<Activity> activities) {
        List<ActivityWrapper> wrapped = wrap(activities);
        List<LocalDate> dates = wrapped.stream().map(ActivityWrapper::getStartDate).distinct().sorted().toList();

        int maxStreak = 0, currentStreak = 0;
        LocalDate prev = null;

        for (LocalDate date : dates) {
            if (prev == null || prev.plusDays(1).equals(date)) currentStreak++;
            else currentStreak = 1;
            maxStreak = Math.max(maxStreak, currentStreak);
            prev = date;
        }
        return maxStreak;
    }

    /**
     * Computes average elevation gain per kilometer across all activities.
     */
    public double elevationPerKm(List<Activity> activities) {
        double km = totalDistanceKm(activities);
        return km == 0 ? 0.0 : totalElevationGain(activities) / km;
    }

    /**
     * Computes the average pace (time per km) as a Duration.
     */
    public Duration averagePacePerKm(List<Activity> activities) {
        double km = totalDistanceKm(activities);
        if (km == 0) return Duration.ZERO;
        long totalSeconds = totalMovingTime(activities).getSeconds();
        long paceSeconds = (long)(totalSeconds / km);
        return Duration.ofSeconds(paceSeconds);
    }
}
