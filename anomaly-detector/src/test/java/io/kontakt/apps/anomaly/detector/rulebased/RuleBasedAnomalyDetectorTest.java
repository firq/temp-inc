package io.kontakt.apps.anomaly.detector.rulebased;

import io.kontak.apps.event.TemperatureReading;
import io.kontakt.apps.anomaly.detector.AnomalyDetector;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static io.kontakt.apps.anomaly.detector.TemperatureReadingTestCreator.ofTemperature;
import static io.kontakt.apps.anomaly.detector.TemperatureReadingTestCreator.ofTemperatureAtTime;
import static io.kontakt.apps.anomaly.detector.rulebased.DeviationFromConsecutiveRule.NUMBER_OF_CONSECUTIVE_ITEMS;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RuleBasedAnomalyDetectorTest {

    AnomalyDetector anomalyDetector = new AnomalyDetectionConfig().anomalyDetector();

    @Test
    void consecutiveAnomaliesAreDetected() {
        // set of checks resulting ing avg of 20 is performed and results in no anomalies
        checkTemperatureNTimesAndNotExpectAnomaly(20, 3);
        // anomaly appears and is detected
        assertTrue(anomalyDetector.apply(ofTemperature(30)).isPresent());
        // checks up to limit of consecutive temperatures are performed with valid temperatures
        checkTemperatureNTimesAndNotExpectAnomaly(20, NUMBER_OF_CONSECUTIVE_ITEMS);
        // anomaly appears and is detected
        assertTrue(anomalyDetector.apply(ofTemperature(25)).isPresent());
    }

    @Test
    void olderThenLimitReadingIsStillAffectingConsecutiveRuleAvg() {
        anomalyDetector.apply(temperatureInputBeforeCheckedTime(24));
        // set of checks
        checkTemperatureNTimesAndNotExpectAnomaly(20, NUMBER_OF_CONSECUTIVE_ITEMS - 1);
        // first check still influences consecutive rule but in time rule got violated resulting in anomaly
        assertTrue(anomalyDetector.apply(ofTemperature(25)).isPresent());
    }

    @Test
    void timeBasedRuleReportViolationEvenAfterReachingMaxNumberOfConsecutiveChecks() {
        // initial check with slightly higher temperature
        anomalyDetector.apply(ofTemperature(24));
        // set of checks up to limit of consecutive with fixed temperature
        checkTemperatureNTimesAndNotExpectAnomaly(20, NUMBER_OF_CONSECUTIVE_ITEMS);
        // check afterward resulting in anomaly from time based rule
        assertTrue(anomalyDetector.apply(ofTemperature(15.1)).isPresent());
    }

    private static TemperatureReading temperatureInputBeforeCheckedTime(int temperature) {
        return ofTemperatureAtTime(temperature, Instant.now().minus(1, ChronoUnit.MINUTES));
    }

    void checkTemperatureNTimesAndNotExpectAnomaly(int temperature, int executionNumber) {
        for (int i = 0; i < executionNumber; i++) {
            assertTrue(anomalyDetector.apply(ofTemperature(temperature)).isEmpty());
        }
    }

}