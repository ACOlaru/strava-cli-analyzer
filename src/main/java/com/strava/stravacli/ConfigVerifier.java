package com.strava.stravacli;

import com.strava.stravacli.config.StravaConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConfigVerifier implements CommandLineRunner {

    private final StravaConfig stravaConfig;

    // Constructor injection (recommended)
    public ConfigVerifier(StravaConfig stravaConfig) {
        this.stravaConfig = stravaConfig;
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
