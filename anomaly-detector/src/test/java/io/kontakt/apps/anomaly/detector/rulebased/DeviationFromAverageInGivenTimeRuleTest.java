package io.kontakt.apps.anomaly.detector.rulebased;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static io.kontakt.apps.anomaly.detector.TemperatureReadingTestCreator.ofTemperature;
import static io.kontakt.apps.anomaly.detector.TemperatureReadingTestCreator.ofTemperatureAtTime;
import static io.kontakt.apps.anomaly.detector.rulebased.DeviationFromAverageInGivenTimeRule.PERIOD_TO_BE_CHECKED_IN_SECONDS;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeviationFromAverageInGivenTimeRuleTest extends AnomalyRuleTest {

    DeviationFromAverageInGivenTimeRuleTest() {
        super(new DeviationFromAverageInGivenTimeRule());
    }

    @ParameterizedTest
    @ValueSource(doubles = {19.75, 29.75})
    void allOfValuesWithinTimeLimitAreUsedForAvgCalculationAndReportAnomalies(double tenthValue) {
        checksWithinTimeLimitOfAvg24_75();
        assertTrue(rule.test(ofTemperature(tenthValue)));
    }

    @ParameterizedTest
    @ValueSource(doubles = {19.76, 29.74})
    void allOfValuesWithinTimeLimitAreUsedForAvgCalculationAndReportNoAnomalies(double tenthValue) {
        checksWithinTimeLimitOfAvg24_75();
        assertFalse(rule.test(ofTemperature(tenthValue)));
    }

    @ParameterizedTest
    @ValueSource(doubles = {19.76, 29.74})
    void temperatureBeforeSelectedIntervalIsNotParticipatingInAvgCheck(double eleventhValue) {
        checksWithinTimeLimitOfAvg24_75();
        rule.test(ofTemperatureAtTime(-100, Instant.now().minus(PERIOD_TO_BE_CHECKED_IN_SECONDS, ChronoUnit.SECONDS)));
        assertFalse(rule.test(ofTemperature(eleventhValue)));
    }

    // 24.75 avg for 20 items
    private void checksWithinTimeLimitOfAvg24_75() {
        addNTimesIncrementTemperatureByHalf(20);
    }


}