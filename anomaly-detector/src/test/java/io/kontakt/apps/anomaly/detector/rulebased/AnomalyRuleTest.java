package io.kontakt.apps.anomaly.detector.rulebased;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.kontakt.apps.anomaly.detector.TemperatureReadingTestCreator.avgTemp20Degree;
import static io.kontakt.apps.anomaly.detector.TemperatureReadingTestCreator.ofTemperature;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class AnomalyRuleTest {

    final AnomalyRule rule;

    AnomalyRuleTest(AnomalyRule rule) {
        this.rule = rule;
    }

    @ParameterizedTest
    @ValueSource(doubles = {-20, -0.5, 0, 0.5, 25.01, 25.1, 26, 40, 100})
    void singleValueIsNotMarkedAsAnomaly(double value) {
        assertFalse(rule.test(ofTemperature(value)));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-20, -0.5, 0, 14, 14.9, 14.99, 15, 25.01, 25.1, 26, 40})
    void consecutiveValuesAboveLimitMarkedAsAnomaly(double secondValue) {
        rule.test(avgTemp20Degree());
        assertTrue(rule.test(ofTemperature(secondValue)));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-10, 0})
    void consecutiveValuesAboveLimitMarkedAsAnomalyForAvgNegativeNumber(double secondValue) {
        rule.test(ofTemperature(-5));
        assertTrue(rule.test(ofTemperature(secondValue)));
    }

    @ParameterizedTest
    @ValueSource(doubles = {15.01, 15.1, 20, 24.9, 24.99})
    void consecutiveValuesWithingLimitAreNotAnomalies(double secondValue) {
        rule.test(avgTemp20Degree());
        assertFalse(rule.test(ofTemperature(secondValue)));
    }

    void addNTimesIncrementTemperatureByHalf(int executionNumber) {
        for (int i = 0; i < executionNumber; i++) {
            rule.test(ofTemperature(20 + i * 0.5));
        }
    }

}