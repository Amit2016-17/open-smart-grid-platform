/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.cucumber.platform.glue.steps.ws.basicosgpfunctions;

import static com.alliander.osgp.cucumber.platform.core.Helpers.getEnum;
import static com.alliander.osgp.cucumber.platform.core.Helpers.getString;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

import javax.naming.OperationNotSupportedException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.soap.client.SoapFaultClientException;

import com.alliander.osgp.adapter.ws.schema.admin.devicemanagement.DeviceAuthorisation;
import com.alliander.osgp.adapter.ws.schema.admin.devicemanagement.DeviceFunctionGroup;
import com.alliander.osgp.adapter.ws.schema.admin.devicemanagement.FindDeviceAuthorisationsRequest;
import com.alliander.osgp.adapter.ws.schema.admin.devicemanagement.FindDeviceAuthorisationsResponse;
import com.alliander.osgp.adapter.ws.schema.admin.devicemanagement.RemoveDeviceRequest;
import com.alliander.osgp.adapter.ws.schema.admin.devicemanagement.UpdateDeviceAuthorisationsRequest;
import com.alliander.osgp.adapter.ws.schema.core.adhocmanagement.SetRebootRequest;
import com.alliander.osgp.adapter.ws.schema.core.configurationmanagement.Configuration;
import com.alliander.osgp.adapter.ws.schema.core.configurationmanagement.GetConfigurationRequest;
import com.alliander.osgp.adapter.ws.schema.core.configurationmanagement.SetConfigurationRequest;
import com.alliander.osgp.adapter.ws.schema.core.deviceinstallation.StartDeviceTestRequest;
import com.alliander.osgp.adapter.ws.schema.core.deviceinstallation.StopDeviceTestRequest;
import com.alliander.osgp.adapter.ws.schema.core.devicemanagement.FindEventsRequest;
import com.alliander.osgp.adapter.ws.schema.core.devicemanagement.SetEventNotificationsRequest;
import com.alliander.osgp.adapter.ws.schema.core.firmwaremanagement.GetFirmwareVersionRequest;
import com.alliander.osgp.adapter.ws.schema.core.firmwaremanagement.UpdateFirmwareRequest;
import com.alliander.osgp.adapter.ws.schema.publiclighting.adhocmanagement.GetStatusRequest;
import com.alliander.osgp.adapter.ws.schema.publiclighting.adhocmanagement.ResumeScheduleRequest;
import com.alliander.osgp.adapter.ws.schema.publiclighting.adhocmanagement.SetLightRequest;
import com.alliander.osgp.adapter.ws.schema.publiclighting.adhocmanagement.SetTransitionRequest;
import com.alliander.osgp.adapter.ws.schema.publiclighting.devicemonitoring.GetActualPowerUsageRequest;
import com.alliander.osgp.adapter.ws.schema.publiclighting.devicemonitoring.GetPowerUsageHistoryRequest;
import com.alliander.osgp.cucumber.platform.Defaults;
import com.alliander.osgp.cucumber.platform.Keys;
import com.alliander.osgp.cucumber.platform.core.ScenarioContext;
import com.alliander.osgp.cucumber.platform.support.ws.admin.AdminDeviceManagementClient;
import com.alliander.osgp.cucumber.platform.support.ws.core.CoreAdHocManagementClient;
import com.alliander.osgp.cucumber.platform.support.ws.core.CoreConfigurationManagementClient;
import com.alliander.osgp.cucumber.platform.support.ws.core.CoreDeviceInstallationClient;
import com.alliander.osgp.cucumber.platform.support.ws.core.CoreDeviceManagementClient;
import com.alliander.osgp.cucumber.platform.support.ws.core.CoreFirmwareManagementClient;
import com.alliander.osgp.cucumber.platform.support.ws.publiclighting.PublicLightingAdHocManagementClient;
import com.alliander.osgp.cucumber.platform.support.ws.publiclighting.PublicLightingDeviceMonitoringClient;
import com.alliander.osgp.cucumber.platform.support.ws.publiclighting.PublicLightingScheduleManagementClient;
import com.alliander.osgp.cucumber.platform.support.ws.tariffswitching.TariffSwitchingScheduleManagementClient;
import com.alliander.osgp.domain.core.valueobjects.DeviceFunction;
import com.alliander.osgp.shared.exceptionhandling.WebServiceSecurityException;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Class with all the AuthorizeDeviceFunctions steps
 */
public class AuthorizeDeviceFunctions {

    @Autowired
    private AdminDeviceManagementClient adminDeviceManagementClient;

    @Autowired
    private CoreDeviceInstallationClient coreDeviceInstallationClient;

    @Autowired
    private CoreDeviceManagementClient coreDeviceManagementClient;

    @Autowired
    private CoreConfigurationManagementClient coreConfigurationManagementClient;

    @Autowired
    private CoreAdHocManagementClient coreAdHocManagementClient;

    @Autowired
    private PublicLightingDeviceMonitoringClient publicLightingDeviceMonitoringClient;

    @Autowired
    private PublicLightingAdHocManagementClient publicLightingAdHocManagementClient;

    @Autowired
    private CoreFirmwareManagementClient coreFirmwareManagementClient;

    @Autowired
    private PublicLightingScheduleManagementClient publicLightingScheduleManagementClient;

    @Autowired
    private TariffSwitchingScheduleManagementClient tariffSwitchingScheduleManagementClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizeDeviceFunctions.class);

    private DeviceFunction deviceFunction;
    private Throwable throwable;

    @When("receiving a device function request")
    public void receivingADeviceFunctionRequest(final Map<String, String> requestParameters)
            throws OperationNotSupportedException, WebServiceSecurityException, GeneralSecurityException, IOException {
        this.deviceFunction = getEnum(requestParameters, Keys.DEVICE_FUNCTION, DeviceFunction.class);

        try {
            if (requestParameters.containsKey(Keys.DELEGATE_FUNCTION_GROUP)) {
                this.findDeviceAuthorisations(requestParameters);
            } else {
                switch (this.deviceFunction) {
                case START_SELF_TEST:
                    this.startSelfTest(requestParameters);
                    break;
                case STOP_SELF_TEST:
                    this.stopSelfTest(requestParameters);
                    break;
                case SET_LIGHT:
                    this.setLight(requestParameters);
                    break;
                case GET_STATUS:
                    this.getStatus(requestParameters);
                    break;
                case GET_DEVICE_AUTHORIZATION:
                    this.getDeviceAuthorization(requestParameters);
                    break;
                case SET_EVENT_NOTIFICATIONS:
                    this.setEventNotifications(requestParameters);
                    break;
                case GET_EVENT_NOTIFICATIONS:
                    this.getEventNotifications(requestParameters);
                    break;
                case UPDATE_FIRMWARE:
                    this.updateFirmware(requestParameters);
                    break;
                case GET_FIRMWARE_VERSION:
                    this.getFirmwareVersion(requestParameters);
                    break;
                case SET_LIGHT_SCHEDULE:
                    this.setLightSchedule(requestParameters);
                    break;
                case SET_TARIFF_SCHEDULE:
                    this.setTariffSchedule(requestParameters);
                    break;
                case SET_CONFIGURATION:
                    this.setConfiguration(requestParameters);
                    break;
                case GET_CONFIGURATION:
                    this.getConfiguration(requestParameters);
                    break;
                case REMOVE_DEVICE:
                    this.removeDevice(requestParameters);
                    break;
                case GET_ACTUAL_POWER_USAGE:
                    this.getActualPowerUsage(requestParameters);
                    break;
                case GET_POWER_USAGE_HISTORY:
                    this.getPowerUsageHistory(requestParameters);
                    break;
                case RESUME_SCHEDULE:
                    this.resumeSchedule(requestParameters);
                    break;
                case SET_REBOOT:
                    this.setReboot(requestParameters);
                    break;
                case SET_TRANSITION:
                    this.setTransition(requestParameters);
                    break;
                default:
                    throw new OperationNotSupportedException(
                            "DeviceFunction " + this.deviceFunction + " does not exist.");
                }
            }
        } catch (final Throwable t) {
            LOGGER.info("Exception: {}", t.getClass().getSimpleName());
            this.throwable = t;
        }
    }

    @Then("the device function response is \"([^\"]*)\"")
    public void theDeviceFunctionResponseIsSuccessful(final Boolean allowed) {
        final Object response = ScenarioContext.Current().get(Keys.RESPONSE);

        if (allowed) {
            Assert.assertTrue(!(response instanceof SoapFaultClientException));
        } else {
            Assert.assertTrue(this.throwable != null);
        }
    }

    private void findDeviceAuthorisations(final Map<String, String> requestParameters)
            throws WebServiceSecurityException, GeneralSecurityException, IOException, OperationNotSupportedException {
        final FindDeviceAuthorisationsRequest findDeviceAuthorisationsRequest = new FindDeviceAuthorisationsRequest();
        findDeviceAuthorisationsRequest.setDeviceIdentification(
                getString(requestParameters, Keys.KEY_DEVICE_IDENTIFICATION, Defaults.DEFAULT_DEVICE_IDENTIFICATION));

        final FindDeviceAuthorisationsResponse response = this.adminDeviceManagementClient
                .findDeviceAuthorisations(findDeviceAuthorisationsRequest);

        final UpdateDeviceAuthorisationsRequest updateDeviceAuthorisationsRequest = new UpdateDeviceAuthorisationsRequest();

        final DeviceAuthorisation deviceAuthorisation = response.getDeviceAuthorisations().get(0);
        deviceAuthorisation
        	.setFunctionGroup(getEnum(requestParameters, Keys.KEY_DEVICE_FUNCTION_GROUP, DeviceFunctionGroup.class));

        updateDeviceAuthorisationsRequest.getDeviceAuthorisations().add(deviceAuthorisation);

        ScenarioContext.Current().put(Keys.RESPONSE,
                this.adminDeviceManagementClient.updateDeviceAuthorisations(updateDeviceAuthorisationsRequest));
    }

    private void startSelfTest(final Map<String, String> requestParameters)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final StartDeviceTestRequest request = new StartDeviceTestRequest();
        request.setDeviceIdentification(
                getString(requestParameters, Keys.KEY_DEVICE_IDENTIFICATION, Defaults.DEFAULT_DEVICE_IDENTIFICATION));

        ScenarioContext.Current().put(Keys.RESPONSE, this.coreDeviceInstallationClient.startDeviceTest(request));
    }

    private void stopSelfTest(final Map<String, String> requestParameters)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final StopDeviceTestRequest request = new StopDeviceTestRequest();
        request.setDeviceIdentification(
                getString(requestParameters, Keys.KEY_DEVICE_IDENTIFICATION, Defaults.DEFAULT_DEVICE_IDENTIFICATION));

        ScenarioContext.Current().put(Keys.RESPONSE, this.coreDeviceInstallationClient.stopDeviceTest(request));
    }

    private void setLight(final Map<String, String> requestParameters)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final SetLightRequest request = new SetLightRequest();
        request.setDeviceIdentification(
                getString(requestParameters, Keys.KEY_DEVICE_IDENTIFICATION, Defaults.DEFAULT_DEVICE_IDENTIFICATION));
        ScenarioContext.Current().put(Keys.RESPONSE, this.publicLightingAdHocManagementClient.setLight(request));
    }

    private void getStatus(final Map<String, String> requestParameters)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {
        // Note: This GetStatusRequest is from PublicLighting AdhocManagement
        final GetStatusRequest request = new GetStatusRequest();
        request.setDeviceIdentification(
                getString(requestParameters, Keys.KEY_DEVICE_IDENTIFICATION, Defaults.DEFAULT_DEVICE_IDENTIFICATION));

        ScenarioContext.Current().put(Keys.RESPONSE, this.publicLightingAdHocManagementClient.getStatus(request));
    }

    private void getDeviceAuthorization(final Map<String, String> requestParameters)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final FindDeviceAuthorisationsRequest request = new FindDeviceAuthorisationsRequest();
        request.setDeviceIdentification(
                getString(requestParameters, Keys.KEY_DEVICE_IDENTIFICATION, Defaults.DEFAULT_DEVICE_IDENTIFICATION));

        ScenarioContext.Current().put(Keys.RESPONSE,
                this.adminDeviceManagementClient.findDeviceAuthorisations(request));
    }

    private void setEventNotifications(final Map<String, String> requestParameters)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final SetEventNotificationsRequest request = new SetEventNotificationsRequest();
        request.setDeviceIdentification(
                getString(requestParameters, Keys.KEY_DEVICE_IDENTIFICATION, Defaults.DEFAULT_DEVICE_IDENTIFICATION));

        ScenarioContext.Current().put(Keys.RESPONSE, this.coreDeviceManagementClient.setEventNotifications(request));
    }

    private void getEventNotifications(final Map<String, String> requestParameters)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final FindEventsRequest request = new FindEventsRequest();
        request.setDeviceIdentification(
                getString(requestParameters, Keys.KEY_DEVICE_IDENTIFICATION, Defaults.DEFAULT_DEVICE_IDENTIFICATION));

        ScenarioContext.Current().put(Keys.RESPONSE, this.coreDeviceManagementClient.findEventsResponse(request));
    }

    private void updateFirmware(final Map<String, String> requestParameters)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final UpdateFirmwareRequest request = new UpdateFirmwareRequest();
        request.setDeviceIdentification(
                getString(requestParameters, Keys.KEY_DEVICE_IDENTIFICATION, Defaults.DEFAULT_DEVICE_IDENTIFICATION));
        request.setFirmwareIdentification(
                getString(requestParameters, Keys.KEY_FIRMWARE_IDENTIFICATION, Defaults.DEFAULT_FIRMWARE_IDENTIFICATION));

        ScenarioContext.Current().put(Keys.RESPONSE, this.coreFirmwareManagementClient.updateFirmware(request));
    }

    private void getFirmwareVersion(final Map<String, String> requestParameters)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final GetFirmwareVersionRequest request = new GetFirmwareVersionRequest();
        request.setDeviceIdentification(
                getString(requestParameters, Keys.KEY_DEVICE_IDENTIFICATION, Defaults.DEFAULT_DEVICE_IDENTIFICATION));

        ScenarioContext.Current().put(Keys.RESPONSE, this.coreFirmwareManagementClient.getFirmwareVersion(request));
    }

    private void setLightSchedule(final Map<String, String> requestParameters)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final com.alliander.osgp.adapter.ws.schema.publiclighting.schedulemanagement.SetScheduleRequest request = new com.alliander.osgp.adapter.ws.schema.publiclighting.schedulemanagement.SetScheduleRequest();
        request.setDeviceIdentification(
                getString(requestParameters, Keys.KEY_DEVICE_IDENTIFICATION, Defaults.DEFAULT_DEVICE_IDENTIFICATION));

        ScenarioContext.Current().put(Keys.RESPONSE, this.publicLightingScheduleManagementClient.setSchedule(request));
    }

    private void setTariffSchedule(final Map<String, String> requestParameters)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final com.alliander.osgp.adapter.ws.schema.tariffswitching.schedulemanagement.SetScheduleRequest request = new com.alliander.osgp.adapter.ws.schema.tariffswitching.schedulemanagement.SetScheduleRequest();
        request.setDeviceIdentification(
                getString(requestParameters, Keys.KEY_DEVICE_IDENTIFICATION, Defaults.DEFAULT_DEVICE_IDENTIFICATION));

        ScenarioContext.Current().put(Keys.RESPONSE, this.tariffSwitchingScheduleManagementClient.setSchedule(request));
    }

    private void setConfiguration(final Map<String, String> requestParameters)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final SetConfigurationRequest request = new SetConfigurationRequest();
        request.setDeviceIdentification(
                getString(requestParameters, Keys.KEY_DEVICE_IDENTIFICATION, Defaults.DEFAULT_DEVICE_IDENTIFICATION));

        final Configuration config = new Configuration();

        config.setLightType(Defaults.DEFAULT_CONFIGURATION_LIGHTTYPE);
        config.setPreferredLinkType(Defaults.DEFAULT_CONFIGURATION_PREFERRED_LINKTYPE);
        config.setMeterType(Defaults.DEFAULT_CONFIGURATION_METER_TYPE);
        config.setShortTermHistoryIntervalMinutes(Defaults.DEFAULT_SHORT_INTERVAL);
        config.setLongTermHistoryInterval(Defaults.DEFAULT_LONG_INTERVAL);
        config.setLongTermHistoryIntervalType(Defaults.DEFAULT_INTERVAL_TYPE);

        request.setConfiguration(config);

        ScenarioContext.Current().put(Keys.RESPONSE, this.coreConfigurationManagementClient.setConfiguration(request));
    }

    private void getConfiguration(final Map<String, String> requestParameters)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final GetConfigurationRequest request = new GetConfigurationRequest();
        request.setDeviceIdentification(
                getString(requestParameters, Keys.KEY_DEVICE_IDENTIFICATION, Defaults.DEFAULT_DEVICE_IDENTIFICATION));

        ScenarioContext.Current().put(Keys.RESPONSE, this.coreConfigurationManagementClient.getConfiguration(request));
    }

    private void removeDevice(final Map<String, String> requestParameters)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final RemoveDeviceRequest request = new RemoveDeviceRequest();
        request.setDeviceIdentification(
                getString(requestParameters, Keys.KEY_DEVICE_IDENTIFICATION, Defaults.DEFAULT_DEVICE_IDENTIFICATION));

        ScenarioContext.Current().put(Keys.RESPONSE, this.adminDeviceManagementClient.removeDevice(request));
    }

    private void getActualPowerUsage(final Map<String, String> requestParameters)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final GetActualPowerUsageRequest request = new GetActualPowerUsageRequest();
        request.setDeviceIdentification(
                getString(requestParameters, Keys.KEY_DEVICE_IDENTIFICATION, Defaults.DEFAULT_DEVICE_IDENTIFICATION));

        ScenarioContext.Current().put(Keys.RESPONSE,
                this.publicLightingDeviceMonitoringClient.getActualPowerUsage(request));
    }

    private void getPowerUsageHistory(final Map<String, String> requestParameters)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final GetPowerUsageHistoryRequest request = new GetPowerUsageHistoryRequest();
        request.setDeviceIdentification(
                getString(requestParameters, Keys.KEY_DEVICE_IDENTIFICATION, Defaults.DEFAULT_DEVICE_IDENTIFICATION));

        ScenarioContext.Current().put(Keys.RESPONSE,
                this.publicLightingDeviceMonitoringClient.getPowerUsageHistory(request));
    }

    private void resumeSchedule(final Map<String, String> requestParameters)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final ResumeScheduleRequest request = new ResumeScheduleRequest();
        request.setDeviceIdentification(
                getString(requestParameters, Keys.KEY_DEVICE_IDENTIFICATION, Defaults.DEFAULT_DEVICE_IDENTIFICATION));

        ScenarioContext.Current().put(Keys.RESPONSE, this.publicLightingAdHocManagementClient.resumeSchedule(request));
    }

    private void setReboot(final Map<String, String> requestParameters)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final SetRebootRequest request = new SetRebootRequest();
        request.setDeviceIdentification(
                getString(requestParameters, Keys.KEY_DEVICE_IDENTIFICATION, Defaults.DEFAULT_DEVICE_IDENTIFICATION));

        ScenarioContext.Current().put(Keys.RESPONSE, this.coreAdHocManagementClient.setReboot(request));
    }

    private void setTransition(final Map<String, String> requestParameters)
            throws WebServiceSecurityException, GeneralSecurityException, IOException, DatatypeConfigurationException {
        final SetTransitionRequest request = new SetTransitionRequest();
        request.setDeviceIdentification(
                getString(requestParameters, Keys.KEY_DEVICE_IDENTIFICATION, Defaults.DEFAULT_DEVICE_IDENTIFICATION));

        ScenarioContext.Current().put(Keys.RESPONSE, this.publicLightingAdHocManagementClient.setTransition(request));
    }
}