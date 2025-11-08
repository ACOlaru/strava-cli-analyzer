package com.strava.stravacli.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Activity {
    private String id;
    private String name;
    private double distance;

    @JsonProperty("moving_time")
    private long movingTime; // in seconds

    @JsonProperty("elapsed_time")
    private long elapsedTime; // in seconds

    @JsonProperty("total_elevation_gain")
    private double elevationGain; // in meters

    private String type;

    @JsonProperty("kudos_count")
    private int kudosCount;

    @Override
    public String toString() {
        return String.format("%s (%s): %.2f km, %d min, ↑ %.1f m",
                name,
                type,
                distance / 1000.0,
                movingTime / 60,
                elevationGain);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getMovingTime() {
        return movingTime;
    }

    public void setMovingTime(long movingTime) {
        this.movingTime = movingTime;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public double getElevationGain() {
        return elevationGain;
    }

    public void setElevationGain(double elevationGain) {
        this.elevationGain = elevationGain;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getKudosCount() {
        return kudosCount;
    }

    public void setKudosCount(int kudosCount) {
        this.kudosCount = kudosCount;
    }
}
