package io.kontakt.apps.temerature.analytics.api.repository;

import io.kontak.apps.event.Anomaly;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "anomalies")
public class AnomalyEntity {

    @Id
    @GeneratedValue
    @Column( columnDefinition = "uuid", updatable = false )
    private UUID id;
    private double temperature;
    private String roomId;
    private String thermometerId;
    private Instant timestamp;

    private AnomalyEntity(double temperature, String roomId, String thermometerId, Instant timestamp) {
        this.temperature = temperature;
        this.roomId = roomId;
        this.thermometerId = thermometerId;
        this.timestamp = timestamp;
    }

    public AnomalyEntity() {
    }

    public AnomalyEntity(UUID id, double temperature, String roomId, String thermometerId, Instant timestamp) {
        this.id = id;
        this.temperature = temperature;
        this.roomId = roomId;
        this.thermometerId = thermometerId;
        this.timestamp = timestamp;
    }

    public static AnomalyEntity from(Anomaly anomaly) {
        return new AnomalyEntity(anomaly.temperature(), anomaly.roomId(), anomaly.thermometerId(), anomaly.timestamp());
    }

    public Anomaly toAnomaly() {
        return new Anomaly(temperature, roomId, thermometerId, timestamp);
    }
}
