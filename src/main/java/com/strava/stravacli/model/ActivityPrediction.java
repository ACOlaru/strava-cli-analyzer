package com.strava.stravacli.model;

public record ActivityPrediction(
        double distanceGrowthPerWeek,
        double speedTrendPerWeek,
        double predictedNextWeekDistance
) {
    @Override
    public String toString() {
        return """
               📈 Predictions
               ------------------------
               Distance growth per week   : %.2f km/week
               Speed trend per week       : %.2f km/h/week
               Predicted next week dist.  : %.2f km
               """.formatted(
                distanceGrowthPerWeek,
                speedTrendPerWeek,
                predictedNextWeekDistance
        );
    }
}
