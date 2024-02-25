package io.kontakt.apps.temerature.analytics.api;

import io.kontak.apps.event.Anomaly;
import org.apache.kafka.streams.kstream.KStream;

import java.util.function.Consumer;

public class AnomaliesSaver implements Consumer<KStream<String, Anomaly>> {

    private final AnomalyService anomalyService;

    public AnomaliesSaver(AnomalyService anomalyService) {
        this.anomalyService = anomalyService;
    }

    @Override
    public void accept(KStream<String, Anomaly> events) {
        events.foreach((s, anomaly) -> anomalyService.save(anomaly));
    }
}
