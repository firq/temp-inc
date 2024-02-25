package io.kontakt.apps.anomaly.detector.rulebased;

import io.kontak.apps.event.TemperatureReading;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

import static io.kontakt.apps.anomaly.detector.rulebased.TemperatureDiffersCalculator.temperatureExceedsAverage;

class DeviationFromAverageInGivenTimeRule implements AnomalyRule {

    private static final int INIT_CAPACITY = 200;
    static final int PERIOD_TO_BE_CHECKED_IN_SECONDS = 10;
    private final List<TemperatureReading> storedReadings = new ArrayList<>(INIT_CAPACITY);

    @Override
    public boolean test(TemperatureReading temperatureReading) {
        cleanUpOldRecords();
        OptionalDouble avgFromLast10Minutes = avgTemperatureInLastPeriod();
        storedReadings.add(temperatureReading);
        return temperatureExceedsAverage(temperatureReading, avgFromLast10Minutes);
    }

    private void cleanUpOldRecords() {
        if (isSizeRightBeforeResizing(storedReadings)) {
            Instant limit = ageLimitForRecords();
            storedReadings.removeIf(temperatureReading -> temperatureReading.timestamp().isBefore(limit));
        }
    }

    private static boolean isSizeRightBeforeResizing(List<TemperatureReading> temperatureReadings) {
        return temperatureReadings.size() % INIT_CAPACITY - 1 == 0;
    }

    private OptionalDouble avgTemperatureInLastPeriod() {
        Instant limit = ageLimitForRecords();
        return storedReadings.stream()
                .filter(temperatureReading -> temperatureReading.timestamp().isAfter(limit))
                .mapToDouble(TemperatureReading::temperature)
                .average();
    }

    private static Instant ageLimitForRecords() {
        return Instant.now().minus(PERIOD_TO_BE_CHECKED_IN_SECONDS, ChronoUnit.SECONDS);
    }
}
