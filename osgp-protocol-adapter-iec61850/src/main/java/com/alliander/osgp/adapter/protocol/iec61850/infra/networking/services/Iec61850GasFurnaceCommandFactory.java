/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.protocol.iec61850.infra.networking.services;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alliander.osgp.adapter.protocol.iec61850.device.rtu.RtuReadCommand;
import com.alliander.osgp.adapter.protocol.iec61850.device.rtu.RtuReadCommandFactory;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.helper.DataAttribute;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.services.commands.Iec61850AlarmCommand;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.services.commands.Iec61850AlarmOtherCommand;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.services.commands.Iec61850BehaviourCommand;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.services.commands.Iec61850HealthCommand;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.services.commands.Iec61850MaterialFlowCommand;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.services.commands.Iec61850MaterialStatusCommand;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.services.commands.Iec61850MaterialTypeCommand;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.services.commands.Iec61850ModeCommand;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.services.commands.Iec61850TemperatureCommand;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.services.commands.Iec61850WarningCommand;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.services.commands.Iec61850WarningOtherCommand;
import com.alliander.osgp.dto.valueobjects.microgrids.MeasurementDto;
import com.alliander.osgp.dto.valueobjects.microgrids.MeasurementFilterDto;

public class Iec61850GasFurnaceCommandFactory implements RtuReadCommandFactory<MeasurementDto, MeasurementFilterDto> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Iec61850GasFurnaceCommandFactory.class);

    private static Iec61850GasFurnaceCommandFactory instance;

    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;

    private static final Map<DataAttribute, RtuReadCommand<MeasurementDto>> RTU_COMMAND_MAP = new HashMap<>();

    static {
        initializeRtuCommandMap();
    }

    private Iec61850GasFurnaceCommandFactory() {
        // Default constructor
    }

    public static synchronized Iec61850GasFurnaceCommandFactory getInstance() {
        if (instance == null) {
            instance = new Iec61850GasFurnaceCommandFactory();
        }
        return instance;
    }

    @Override
    public RtuReadCommand<MeasurementDto> getCommand(final MeasurementFilterDto filter) {
        return this.getCommand(DataAttribute.fromString(filter.getNode()));
    }

    @Override
    public RtuReadCommand<MeasurementDto> getCommand(final String node) {
        return this.getCommand(DataAttribute.fromString(node));
    }

    private static void initializeRtuCommandMap() {
        RTU_COMMAND_MAP.put(DataAttribute.BEHAVIOR, new Iec61850BehaviourCommand());
        RTU_COMMAND_MAP.put(DataAttribute.HEALTH, new Iec61850HealthCommand());
        RTU_COMMAND_MAP.put(DataAttribute.MODE, new Iec61850ModeCommand());

        RTU_COMMAND_MAP.put(DataAttribute.ALARM_ONE, new Iec61850AlarmCommand(ONE));
        RTU_COMMAND_MAP.put(DataAttribute.ALARM_TWO, new Iec61850AlarmCommand(TWO));
        RTU_COMMAND_MAP.put(DataAttribute.ALARM_THREE, new Iec61850AlarmCommand(THREE));
        RTU_COMMAND_MAP.put(DataAttribute.ALARM_FOUR, new Iec61850AlarmCommand(FOUR));
        RTU_COMMAND_MAP.put(DataAttribute.ALARM_OTHER, new Iec61850AlarmOtherCommand());

        RTU_COMMAND_MAP.put(DataAttribute.WARNING_ONE, new Iec61850WarningCommand(ONE));
        RTU_COMMAND_MAP.put(DataAttribute.WARNING_TWO, new Iec61850WarningCommand(TWO));
        RTU_COMMAND_MAP.put(DataAttribute.WARNING_THREE, new Iec61850WarningCommand(THREE));
        RTU_COMMAND_MAP.put(DataAttribute.WARNING_FOUR, new Iec61850WarningCommand(FOUR));
        RTU_COMMAND_MAP.put(DataAttribute.WARNING_OTHER, new Iec61850WarningOtherCommand());

        for (int i = ONE; i <= TWO; i++) {
            RTU_COMMAND_MAP.put(DataAttribute.TEMPERATURE, new Iec61850TemperatureCommand(i));
            RTU_COMMAND_MAP.put(DataAttribute.MATERIAL_STATUS, new Iec61850MaterialStatusCommand(i));
            RTU_COMMAND_MAP.put(DataAttribute.MATERIAL_TYPE, new Iec61850MaterialTypeCommand(i));
            RTU_COMMAND_MAP.put(DataAttribute.MATERIAL_FLOW, new Iec61850MaterialFlowCommand(i));
        }
    }

    private RtuReadCommand<MeasurementDto> getCommand(final DataAttribute dataAttribute) {

        final RtuReadCommand<MeasurementDto> command = RTU_COMMAND_MAP.get(dataAttribute);

        if (command == null) {
            LOGGER.warn("No command found for data attribute {}", dataAttribute);
        }
        return command;
    }
}
