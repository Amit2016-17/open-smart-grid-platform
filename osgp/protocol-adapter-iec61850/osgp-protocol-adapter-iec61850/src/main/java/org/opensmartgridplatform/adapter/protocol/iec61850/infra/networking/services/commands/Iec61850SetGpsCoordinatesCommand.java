/**
 * Copyright 2014-2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.protocol.iec61850.infra.networking.services.commands;

import com.beanit.openiec61850.Fc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.opensmartgridplatform.adapter.protocol.iec61850.exceptions.NodeException;
import org.opensmartgridplatform.adapter.protocol.iec61850.infra.networking.helper.DataAttribute;
import org.opensmartgridplatform.adapter.protocol.iec61850.infra.networking.helper.DeviceConnection;
import org.opensmartgridplatform.adapter.protocol.iec61850.infra.networking.helper.LogicalDevice;
import org.opensmartgridplatform.adapter.protocol.iec61850.infra.networking.helper.LogicalNode;
import org.opensmartgridplatform.adapter.protocol.iec61850.infra.networking.helper.NodeContainer;
import org.opensmartgridplatform.adapter.protocol.iec61850.infra.networking.helper.SubDataAttribute;

public class Iec61850SetGpsCoordinatesCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(Iec61850SetGpsCoordinatesCommand.class);

    public void setGpsCoordinates(final DeviceConnection deviceConnection, final Float longitude, final Float latitude)
            throws NodeException {
        final NodeContainer astronomical = deviceConnection.getFcModelNode(LogicalDevice.LIGHTING,
                LogicalNode.STREET_LIGHT_CONFIGURATION, DataAttribute.ASTRONOMICAL, Fc.CF);
        astronomical.writeFloat(SubDataAttribute.GPS_LONGITUDE, longitude);
        astronomical.writeFloat(SubDataAttribute.GPS_LATITUDE, latitude);
        LOGGER.info("longitude: {}, latitude: {} written for device: {}", longitude, latitude,
                deviceConnection.getDeviceIdentification());
    }
}
