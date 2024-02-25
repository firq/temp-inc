package io.kontakt.apps.temerature.analytics.api.controller;

import io.kontak.apps.event.Anomaly;
import io.kontakt.apps.temerature.analytics.api.AnomalyService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AnomalyAnalyticsController {

    private final AnomalyService anomalyService;

    @Autowired
    public AnomalyAnalyticsController(AnomalyService anomalyService) {
        this.anomalyService = anomalyService;
    }

    @GetMapping("/public/v1/anomalies/thermometer/{thermometerId}")
    public List<Anomaly> anomaliesByThermometer(@PathVariable @NotBlank String thermometerId) {
        return anomalyService.findAnomaliesByThermometer(thermometerId);
    }

    @GetMapping("/public/v1/anomalies/room/{roomId}")
    public List<Anomaly> anomaliesByRoom(@PathVariable @NotBlank String roomId) {
        return anomalyService.findAnomaliesByRoom(roomId);
    }

    @GetMapping("/public/v1/thermometers/anomalies/above-threshold/{threshold}")
    public List<String> thermometersWithAnomaliesAboveThreshold(@PathVariable @Positive int threshold) {
        return anomalyService.findThermometersWithAnomaliesAboveThreshold(threshold);
    }

}
