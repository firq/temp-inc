package io.kontakt.apps.anomaly.detector.rulebased;

import io.kontak.apps.event.TemperatureReading;

import java.util.function.Predicate;

@FunctionalInterface
interface AnomalyRule extends Predicate<TemperatureReading> {

}
