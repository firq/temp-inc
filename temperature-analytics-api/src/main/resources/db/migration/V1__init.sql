CREATE TABLE IF NOT EXISTS anomalies
(
    id              UUID              PRIMARY KEY,
    temperature     DOUBLE PRECISION  NOT NULL,
    room_id         VARCHAR(255)      NOT NULL,
    thermometer_id  VARCHAR(255)      NOT NULL,
    timestamp       TIMESTAMP         NOT NULL
);

CREATE index anomalies_room_id on anomalies(room_id);
CREATE index anomalies_thermometer_id on anomalies(thermometer_id);