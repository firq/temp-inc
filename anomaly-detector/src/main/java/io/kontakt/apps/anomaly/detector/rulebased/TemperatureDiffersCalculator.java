package io.kontakt.apps.anomaly.detector.rulebased;

import io.kontak.apps.event.TemperatureReading;

import java.util.OptionalDouble;

enum TemperatureDiffersCalculator {
    ;

    static final int TEMPERATURE_DIFFERENCE_LIMIT = 5;

    static boolean temperatureExceedsAverage(TemperatureReading temperatureReading,
                                             OptionalDouble avgFromLast10Minutes) {
        return avgFromLast10Minutes
                .stream()
                .anyMatch(avg -> Math.abs(avg - temperatureReading.temperature()) >= TEMPERATURE_DIFFERENCE_LIMIT);
    }

}
