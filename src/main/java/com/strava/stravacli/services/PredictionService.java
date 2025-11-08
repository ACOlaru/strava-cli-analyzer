package com.strava.stravacli.services;

import com.strava.stravacli.model.Activity;
import com.strava.stravacli.model.ActivityPrediction;
import com.strava.stravacli.util.ActivityWrapper;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PredictionService {

    public ActivityPrediction getPredictions(List<Activity> activities) {
        return new ActivityPrediction(
                distanceGrowthPerWeek(activities),
                averageSpeedTrendKmh(activities),
                predictedNextWeekDistanceKm(activities)
        );
    }

    /** Distance growth per week (slope of weekly distance trend) */
    public double distanceGrowthPerWeek(List<Activity> activities) {
        Map<Integer, Double> weekDistance = activities.stream()
                .map(ActivityWrapper::new)
                .collect(Collectors.groupingBy(
                        ActivityWrapper::getWeekNumber,
                        Collectors.summingDouble(aw -> aw.getActivity().getDistance() / 1000.0)
                ));

        SimpleRegression regression = new SimpleRegression();
        weekDistance.forEach(regression::addData);

        return regression.getSlope(); // km/week
    }

    /** Average speed trend per week (slope of weekly average speed) */
    public double averageSpeedTrendKmh(List<Activity> activities) {
        Map<Integer, Double> weekAvgSpeed = activities.stream()
                .map(ActivityWrapper::new)
                .collect(Collectors.groupingBy(
                        ActivityWrapper::getWeekNumber,
                        Collectors.averagingDouble(aw -> {
                            double hours = aw.getActivity().getMovingTime() / 3600.0;
                            return hours == 0 ? 0.0 : (aw.getActivity().getDistance() / 1000.0) / hours;
                        })
                ));

        SimpleRegression regression = new SimpleRegression();
        weekAvgSpeed.forEach(regression::addData);

        return regression.getSlope(); // km/h per week
    }

    /** Predicts next week's total distance (km) based on weekly trend */
    public double predictedNextWeekDistanceKm(List<Activity> activities) {
        Map<Integer, Double> weekDistance = activities.stream()
                .map(ActivityWrapper::new)
                .collect(Collectors.groupingBy(
                        ActivityWrapper::getWeekNumber,
                        Collectors.summingDouble(aw -> aw.getActivity().getDistance() / 1000.0)
                ));

        if (weekDistance.isEmpty()) return 0.0;

        SimpleRegression regression = new SimpleRegression();
        weekDistance.forEach(regression::addData);

        int lastWeek = Collections.max(weekDistance.keySet());
        return regression.predict(lastWeek + 1); // km for next week
    }
}
