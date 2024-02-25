package io.kontakt.apps.anomaly.detector.rulebased;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.kontakt.apps.anomaly.detector.TemperatureReadingTestCreator.ofTemperature;
import static io.kontakt.apps.anomaly.detector.rulebased.DeviationFromConsecutiveRule.NUMBER_OF_CONSECUTIVE_ITEMS;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeviationFromConsecutiveRuleTest extends AnomalyRuleTest {

    DeviationFromConsecutiveRuleTest() {
        super(new DeviationFromConsecutiveRule());
    }

    @ParameterizedTest
    @ValueSource(doubles = {17, 27})
    void allOfPrevious9ReadingsAreUsedForAvgCalculationAndReportAnomalies(double tenthValue) {
        nineConsecutiveChecks();
        assertTrue(rule.test(ofTemperature(tenthValue)));
    }

    @ParameterizedTest
    @ValueSource(doubles = {17.01, 22, 22.75, 23, 26.99})
    void allOfPrevious9ReadingsAreUsedForAvgCalculationAndReportNoAnomalies(double tenthValue) {
        nineConsecutiveChecks();
        assertFalse(rule.test(ofTemperature(tenthValue)));
    }

    @ParameterizedTest
    @ValueSource(doubles = {17.01, 22, 22.75, 23, 26.99})
    void firstTemperatureIsNotTakenIntoConsiderationOn11thCheck(double eleventhValue) {
        rule.test(ofTemperature(-60));
        nineConsecutiveChecks();
        assertFalse(rule.test(ofTemperature(eleventhValue)));
    }


    // 22 avg for 9 items
    private void nineConsecutiveChecks() {
        addNTimesIncrementTemperatureByHalf(NUMBER_OF_CONSECUTIVE_ITEMS);
    }

}