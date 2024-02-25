package io.kontakt.apps.anomaly.detector.rulebased;

import io.kontak.apps.event.TemperatureReading;

import java.util.ArrayDeque;
import java.util.OptionalDouble;
import java.util.Queue;

import static io.kontakt.apps.anomaly.detector.rulebased.TemperatureDiffersCalculator.temperatureExceedsAverage;

class DeviationFromConsecutiveRule implements AnomalyRule {

    static final int NUMBER_OF_CONSECUTIVE_ITEMS = 9;
    private final Queue<TemperatureReading> storedReadings = new ArrayDeque<>(NUMBER_OF_CONSECUTIVE_ITEMS);

    @Override
    public boolean test(TemperatureReading temperatureReading) {
        removeOldestReading();
        // assumption on not clear requirements: if there is less than NUMBER_OF_ITEMS we are still calculating avg
        OptionalDouble avgTemperatureInLastRecords = avgTemperatureInLastRecords();
        storedReadings.add(temperatureReading);
        return temperatureExceedsAverage(temperatureReading, avgTemperatureInLastRecords);
    }

    private void removeOldestReading() {
        if (storedReadings.size() > NUMBER_OF_CONSECUTIVE_ITEMS) {
            storedReadings.poll();
        }
    }

    private OptionalDouble avgTemperatureInLastRecords() {
        return storedReadings.stream()
                .mapToDouble(TemperatureReading::temperature)
                .average();
    }
}
