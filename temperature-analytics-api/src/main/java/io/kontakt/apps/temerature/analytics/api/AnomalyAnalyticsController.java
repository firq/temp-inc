package io.kontakt.apps.temerature.analytics.api;

import io.kontak.apps.event.Anomaly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
public class AnomalyAnalyticsController {

    private final AnomalyService anomalyService;

    @Autowired
    public AnomalyAnalyticsController(AnomalyService anomalyService) {
        this.anomalyService = anomalyService;
    }

    @GetMapping("/anomalies/thermometer/{thermometerId}")
    public List<Anomaly> anomaliesByThermometer(@PathVariable @NotBlank String thermometerId) {
        return anomalyService.findAnomaliesByThermometer(thermometerId);
    }

    @GetMapping("/anomalies/room/{roomId}")
    public List<Anomaly> anomaliesByRoom(@PathVariable @NotBlank String roomId) {
        return anomalyService.findAnomaliesByRoom(roomId);
    }

    @GetMapping("/thermometers/anomalies/above-threshold/{threshold}")
    @Min(value = 0L, message = "The value must be positive")
    public List<String> thermometersWithAnomaliesAboveThreshold(@PathVariable int threshold) {
        return anomalyService.findThermometersWithAnomaliesAboveThreshold(threshold);
    }

}
