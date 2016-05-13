/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.osgp.adapter.protocol.dlms.domain.commands.stub;

import org.osgp.adapter.protocol.dlms.domain.commands.CommandExecutorMap;

import com.alliander.osgp.dto.valueobjects.smartmetering.ActivityCalendarDataDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.ActualMeterReadsDataDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.ActualMeterReadsDataGasDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.AdministrativeStatusTypeDataDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.FindEventsRequestDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.GMeterInfoDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.GetAdministrativeStatusDataDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.GetAssociationLnObjectsRequestDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.GetConfigurationRequestDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.GetFirmwareVersionRequestDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.PeriodicMeterReadsGasRequestDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.PeriodicMeterReadsRequestDataDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.ReadAlarmRegisterDataDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.SetAlarmNotificationsRequestDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.SetConfigurationObjectRequestDataDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.SetPushSetupAlarmRequestDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.SetPushSetupSmsRequestDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.SpecialDaysRequestDataDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.SynchronizeTimeRequestDto;

public class CommandExecutorMapStub extends CommandExecutorMap {

    public CommandExecutorMapStub() {

        this.addCommandExecutor(FindEventsRequestDto.class, new RetrieveEventsBundleCommandExecutorStub());
        this.addCommandExecutor(ActualMeterReadsDataDto.class, new GetActualMeterReadsBundleCommandExecutorStub());
        this.addCommandExecutor(ActualMeterReadsDataGasDto.class, new GetActualMeterReadsBundleGasCommandExecutorStub());
        this.addCommandExecutor(SpecialDaysRequestDataDto.class, new SetSpecialDaysBundleCommandExecutorStub());
        this.addCommandExecutor(ReadAlarmRegisterDataDto.class, new ReadAlarmRegisterBundleCommandExecutorStub());
        this.addCommandExecutor(GetAdministrativeStatusDataDto.class,
                new GetAdministrativeStatusBundleCommandExecutorStub());
        this.addCommandExecutor(PeriodicMeterReadsRequestDataDto.class,
                new GetPeriodicMeterReadsBundleCommandExecutorStub());
        this.addCommandExecutor(PeriodicMeterReadsGasRequestDto.class,
                new GetPeriodicMeterReadsGasBundleCommandExecutorStub());
        this.addCommandExecutor(AdministrativeStatusTypeDataDto.class,
                new SetAdministrativeStatusBundleCommandExecutorStub());
        this.addCommandExecutor(ActivityCalendarDataDto.class, new SetActivityCalendarBundleCommandExecutorStub());
        this.addCommandExecutor(GMeterInfoDto.class, new SetEncryptionKeyExchangeOnGMeterBundleCommandExecutorStub());
        this.addCommandExecutor(SetAlarmNotificationsRequestDto.class,
                new SetAlarmNotificationsBundleCommandExecutorStub());
        this.addCommandExecutor(SetConfigurationObjectRequestDataDto.class,
                new SetConfigurationObjectBundleCommandExecutorStub());
        this.addCommandExecutor(SetPushSetupAlarmRequestDto.class, new SetPushSetupAlarmBundleCommandExecutorStub());
        this.addCommandExecutor(SetPushSetupSmsRequestDto.class, new SetPushSetupSmsBundleCommandExecutorStub());
        this.addCommandExecutor(SynchronizeTimeRequestDto.class, new SynchronizeTimeBundleCommandExecutorStub());
        this.addCommandExecutor(GetConfigurationRequestDto.class,
                new RetrieveConfigurationObjectsBundleCommandExecutorStub());
        this.addCommandExecutor(GetFirmwareVersionRequestDto.class,
                new GetFirmwareVersionsBundleCommandExecutorStub());
        this.addCommandExecutor(GetAssociationLnObjectsRequestDto.class,
                new GetAssociationLnObjectsBundleCommandExecutorStub());
    }
}
