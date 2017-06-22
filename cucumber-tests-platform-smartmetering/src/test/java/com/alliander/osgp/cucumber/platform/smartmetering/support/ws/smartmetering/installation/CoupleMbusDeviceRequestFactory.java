/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.cucumber.platform.smartmetering.support.ws.smartmetering.installation;

import com.alliander.osgp.adapter.ws.schema.smartmetering.installation.CoupleMbusDeviceAsyncRequest;
import com.alliander.osgp.adapter.ws.schema.smartmetering.installation.CoupleMbusDeviceRequest;
import com.alliander.osgp.cucumber.platform.smartmetering.support.ws.smartmetering.RequestFactoryHelper;

public class CoupleMbusDeviceRequestFactory {

    private CoupleMbusDeviceRequestFactory() {
        // Private constructor for utility class.
    }

    public static CoupleMbusDeviceRequest forMbusDevice(final String mbusDeviceIdentification) {
        return forGatewayMbusDevice(RequestFactoryHelper.getDeviceIdentificationFromScenarioContext(),
                mbusDeviceIdentification);
    }

    public static CoupleMbusDeviceRequest forGatewayMbusDevice(final String gatewayDeviceIdentification,
            final String mbusDeviceIdentification) {
        final CoupleMbusDeviceRequest coupleMbusDeviceRequest = new CoupleMbusDeviceRequest();
        coupleMbusDeviceRequest.setDeviceIdentification(gatewayDeviceIdentification);
        coupleMbusDeviceRequest.setMbusDeviceIdentification(mbusDeviceIdentification);
        return coupleMbusDeviceRequest;
    }

    public static CoupleMbusDeviceAsyncRequest fromScenarioContext() {
        final String correlationUid = RequestFactoryHelper.getCorrelationUidFromScenarioContext();
        final String deviceIdentification = RequestFactoryHelper.getDeviceIdentificationFromScenarioContext();
        final CoupleMbusDeviceAsyncRequest coupleMbusDeviceAsyncRequest = new CoupleMbusDeviceAsyncRequest();
        coupleMbusDeviceAsyncRequest.setCorrelationUid(correlationUid);
        coupleMbusDeviceAsyncRequest.setDeviceIdentification(deviceIdentification);
        return coupleMbusDeviceAsyncRequest;
    }
}
