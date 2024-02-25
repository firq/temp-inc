package io.kontakt.apps.anomaly.detector.rulebased;

import io.kontakt.apps.anomaly.detector.AnomalyDetector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
class AnomalyDetectionConfig {

    @Bean
    AnomalyDetector anomalyDetector() {
        return new RuleBasedAnomalyDetector(List.of(
                new DeviationFromConsecutiveRule(),
                new DeviationFromAverageInGivenTimeRule()
        ));
    }

}
