package io.kontakt.apps.anomaly.detector.rulebased;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import io.kontakt.apps.anomaly.detector.AnomalyDetector;

import java.util.List;
import java.util.Optional;

class RuleBasedAnomalyDetector implements AnomalyDetector {

    private final List<AnomalyRule> anomalyDetectionRules;

    RuleBasedAnomalyDetector(List<AnomalyRule> anomalyDetectionRules) {
        this.anomalyDetectionRules = anomalyDetectionRules;
    }

    @Override
    public Optional<Anomaly> apply(TemperatureReading temperatureReading) {
        return checkedInAllRulesDetectedAsAnomalyInAtLeastOne(temperatureReading)
                ? Optional.of(temperatureReading.toAnomaly())
                : Optional.empty();
    }

    private boolean checkedInAllRulesDetectedAsAnomalyInAtLeastOne(TemperatureReading temperatureReading) {
        return anomalyDetectionRules
                .stream()
                .map(rule -> rule.test(temperatureReading))
                .toList()
                .contains(Boolean.TRUE);
    }
}
