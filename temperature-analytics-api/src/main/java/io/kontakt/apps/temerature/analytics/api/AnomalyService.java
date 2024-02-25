package io.kontakt.apps.temerature.analytics.api;

import io.kontak.apps.event.Anomaly;
import io.kontakt.apps.temerature.analytics.api.repository.AnomalyEntity;
import io.kontakt.apps.temerature.analytics.api.repository.AnomalyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnomalyService {

    private final AnomalyRepository anomalyRepository;

    @Autowired
    public AnomalyService(AnomalyRepository anomalyRepository) {
        this.anomalyRepository = anomalyRepository;
    }

    public List<Anomaly> findAnomaliesByThermometer(String thermometerId) {
        return asAnomalies(anomalyRepository.findAlaByThermometerId(thermometerId));
    }

    public List<Anomaly> findAnomaliesByRoom(String roomId) {
        return asAnomalies(anomalyRepository.findAlaByRoomId(roomId));
    }

    private List<Anomaly> asAnomalies(List<AnomalyEntity> entityList){
        return entityList
                .stream()
                .map(AnomalyEntity::toAnomaly)
                .toList();
    }

    public void save(Anomaly anomaly) {
        anomalyRepository.save(AnomalyEntity.from(anomaly));
    }

    public List<String> findThermometersWithAnomaliesAboveThreshold(int threshold) {
        return anomalyRepository.findAllThermometersWithAnomaliesAboveThreshold(threshold);
    }
}
