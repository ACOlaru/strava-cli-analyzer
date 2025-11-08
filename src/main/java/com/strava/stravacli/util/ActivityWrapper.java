package com.strava.stravacli.util;

import com.strava.stravacli.model.Activity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;

public class ActivityWrapper {
    private final Activity activity;
    private final LocalDateTime startDateTime;
    private final LocalDate startDate;
    private final int weekNumber;

    public ActivityWrapper(Activity activity) {
        this.activity = activity;
        this.startDateTime = LocalDateTime.parse(activity.getStartDateLocal(), DateTimeFormatter.ISO_DATE_TIME);
        this.startDate = startDateTime.toLocalDate();
        LocalDate date = LocalDateTime.parse(activity.getStartDateLocal(), DateTimeFormatter.ISO_DATE_TIME)
                .toLocalDate();
        this.weekNumber = date.get(WeekFields.ISO.weekOfWeekBasedYear());

    }

    public Activity getActivity() { return activity; }
    public LocalDateTime getStartDateTime() { return startDateTime; }
    public LocalDate getStartDate() { return startDate; }
    public int getWeekNumber() { return weekNumber; }
}

