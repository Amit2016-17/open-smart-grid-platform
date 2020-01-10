package org.opensmartgridplatform.simulator.protocol.iec61850.domain.services;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.openmuc.openiec61850.ServerSap;
import org.opensmartgridplatform.simulator.protocol.iec61850.domain.valueobjects.FloatMeasurement;
import org.opensmartgridplatform.simulator.protocol.iec61850.infra.files.CsvFloatMeasurementValueProvider;
import org.opensmartgridplatform.simulator.protocol.iec61850.server.RtuSimulator;
import org.opensmartgridplatform.simulator.protocol.iec61850.server.logicaldevices.Transformer;
import org.opensmartgridplatform.simulator.protocol.iec61850.server.tasks.UpdateTransformerPowerValuesTask;
import org.opensmartgridplatform.simulator.protocol.iec61850.server.tasks.UpdateTransformerTemperatureValuesTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@Component
public class TransformerSimulationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransformerSimulationService.class);

    @Value("${rtu.transformer.simulation.power.update.interval:10000}")
    private long transformerSimulationPowerUpdateInterval;

    @Value("${rtu.transformer.simulation.temperature.update.interval:60000}")
    private long transformerSimulationTemperatureUpdateInterval;

    @Value("${rtu.transformer.simulation.speedup.factor:1}")
    private long transformerSimulationSpeedupFactor;

    @Value("${rtu.transformer.1.power.file:p_clean.csv}")
    private String transformer1PowerValuesFileName;

    @Value("${rtu.transformer.1.temperature.file:internTemperature_clean.csv}")
    private String transformer1TemperatureValuesFileName;

    @Value("${rtu.transformer.2.power.file:p_defective.csv}")
    private String transformer2PowerValuesFileName;

    @Value("${rtu.transformer.2.temperature.file:internTemperature_defective.csv}")
    private String transformer2TemperatureValuesFileName;

    private ThreadPoolTaskScheduler taskScheduler;

    public void initialize(final RtuSimulator simulator) {
        LOGGER.info("Initializing transformer simulation");
        final ServerSap serverSap = simulator.getServer();

        final List<Transformer> transformers = simulator.getLogicalDevices()
                .stream()
                .filter(ld -> (ld instanceof Transformer))
                .map(ld -> (Transformer) ld)
                .collect(Collectors.toList());

        this.initializeScheduler(serverSap, transformers);
    }

    public void stop() {
        if (this.taskScheduler != null) {
            this.taskScheduler.destroy();
        }
    }

    private ValueProvider<FloatMeasurement> transformer1PowerValueProvider() {
        LOGGER.info("Initializing transformer1PowerValueProvider bean with file: {}.",
                this.transformer1PowerValuesFileName);
        return new CsvFloatMeasurementValueProvider(this.transformer1PowerValuesFileName);
    }

    private ValueProvider<FloatMeasurement> transformer1TemperatureValueProvider() {
        LOGGER.info("Initializing transformer1TemperatureValueProvider bean with file: {}.",
                this.transformer1TemperatureValuesFileName);
        return new CsvFloatMeasurementValueProvider(this.transformer1TemperatureValuesFileName);
    }

    private ValueProvider<FloatMeasurement> transformer2PowerValueProvider() {
        LOGGER.info("Initializing transformer1PowerValueProvider bean with file: {}.",
                this.transformer1PowerValuesFileName);
        return new CsvFloatMeasurementValueProvider(this.transformer2PowerValuesFileName);
    }

    private ValueProvider<FloatMeasurement> transformer2TemperatureValueProvider() {
        LOGGER.info("Initializing transformer1TemperatureValueProvider bean with file: {}.",
                this.transformer1TemperatureValuesFileName);
        return new CsvFloatMeasurementValueProvider(this.transformer2TemperatureValuesFileName);
    }

    private void initializeScheduler(final ServerSap serverSap, final List<Transformer> transformers) {
        final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();
        scheduler.setPoolSize(10);
        scheduler.setThreadNamePrefix("transformerSubstationTaskScheduler");

        final Duration powerInterval = this.periodInMillis(this.transformerSimulationPowerUpdateInterval);
        final Duration temperatureInterval = this.periodInMillis(this.transformerSimulationTemperatureUpdateInterval);

        final Runnable updateTransformer1PowerValuesTask = new UpdateTransformerPowerValuesTask(serverSap,
                transformers.get(0), this.transformer1PowerValueProvider());
        scheduler.scheduleAtFixedRate(updateTransformer1PowerValuesTask, powerInterval);

        final Runnable updateTransformer1TemperatureValuesTask = new UpdateTransformerTemperatureValuesTask(serverSap,
                transformers.get(0), this.transformer1TemperatureValueProvider());
        scheduler.scheduleAtFixedRate(updateTransformer1TemperatureValuesTask, temperatureInterval);

        final Runnable updateTransformer2PowerValuesTask = new UpdateTransformerPowerValuesTask(serverSap,
                transformers.get(1), this.transformer2PowerValueProvider());
        scheduler.scheduleAtFixedRate(updateTransformer2PowerValuesTask, powerInterval);

        final Runnable updateTemperatureValuesTask = new UpdateTransformerTemperatureValuesTask(serverSap,
                transformers.get(1), this.transformer2TemperatureValueProvider());
        scheduler.scheduleAtFixedRate(updateTemperatureValuesTask, temperatureInterval);

        this.taskScheduler = scheduler;
    }

    private Duration periodInMillis(final long interval) {
        long millis;
        if (this.transformerSimulationSpeedupFactor > 0) {
            millis = interval / this.transformerSimulationSpeedupFactor;
        } else {
            millis = interval;
        }
        return Duration.ofMillis(millis);
    }
}
