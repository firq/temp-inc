package io.kontak.apps.event;

import java.time.Instant;

public record TemperatureReading(double temperature, String roomId, String thermometerId, Instant timestamp) {

    public Anomaly toAnomaly(){
        return new Anomaly(
                temperature(),
                roomId(),
                thermometerId(),
                timestamp());
    }

}
