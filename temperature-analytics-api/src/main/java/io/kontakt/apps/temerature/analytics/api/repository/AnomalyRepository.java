package io.kontakt.apps.temerature.analytics.api.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnomalyRepository extends CrudRepository<AnomalyEntity, Long> {

    List<AnomalyEntity> findAlaByThermometerId(String thermometerId);
    List<AnomalyEntity> findAlaByRoomId(String roomId);

    @Query(value = """
            select thermometer_id
            from anomalies
            group by thermometer_id
            having count(thermometer_id) > (?1);
            """, nativeQuery = true)
    List<String> findAllThermometersWithAnomaliesAboveThreshold(int threshold);

}
