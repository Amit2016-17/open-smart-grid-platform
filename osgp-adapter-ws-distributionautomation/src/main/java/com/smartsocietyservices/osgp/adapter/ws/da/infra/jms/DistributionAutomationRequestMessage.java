/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.smartsocietyservices.osgp.adapter.ws.da.infra.jms;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.alliander.osgp.shared.infra.jms.RequestMessage;

public class DistributionAutomationRequestMessage extends RequestMessage {

    private static final long serialVersionUID = -1212784820197545944L;

    private final DistributionAutomationRequestMessageType messageType;

    public DistributionAutomationRequestMessage(final DistributionAutomationRequestMessageType messageType, final String correlationUid,
            final String organisationIdentification, final String deviceIdentification, final Serializable request,
            final DateTime scheduleTime) {
        super(correlationUid, organisationIdentification, deviceIdentification, request);
        this.messageType = messageType;
    }

    public DistributionAutomationRequestMessageType getMessageType() {
        return this.messageType;
    }
}
