package com.intuso.housemate.client.proxy.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.SystemMapper;
import com.intuso.housemate.client.api.internal.object.System;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Created by tomc on 28/11/16.
 */
public class ProxySystemBridge
        extends ProxyObjectBridge<com.intuso.housemate.client.v1_0.api.object.System.Data, System.Data, System.Listener<? super ProxySystemBridge>>
        implements System<
                ProxyValueBridge,
                ProxyCommandBridge,
                ProxyListBridge<ProxyPropertyBridge>,
        ProxySystemBridge> {

    private final ProxyCommandBridge renameCommand;
    private final ProxyCommandBridge removeCommand;
    private final ProxyValueBridge errorValue;
    private final ProxyListBridge<ProxyPropertyBridge> playbackDevices;
    private final ProxyCommandBridge addPlaybackDeviceCommand;
    private final ProxyListBridge<ProxyPropertyBridge> powerDevices;
    private final ProxyCommandBridge addPowerDeviceCommand;
    private final ProxyListBridge<ProxyPropertyBridge> runDevices;
    private final ProxyCommandBridge addRunDeviceCommand;
    private final ProxyListBridge<ProxyPropertyBridge> temperatureSensorDevices;
    private final ProxyCommandBridge addTemperatureSensorDeviceCommand;
    private final ProxyListBridge<ProxyPropertyBridge> volumeDevices;
    private final ProxyCommandBridge addVolumeDeviceCommand;

    @Inject
    protected ProxySystemBridge(@Assisted Logger logger,
                                SystemMapper systemMapper,
                                Factory<ProxyCommandBridge> commandFactory,
                                Factory<ProxyValueBridge> valueFactory,
                                Factory<ProxyListBridge<ProxyPropertyBridge>> propertiesFactory,
                                ManagedCollectionFactory managedCollectionFactory) {
        super(logger, System.Data.class, systemMapper, managedCollectionFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID));
        removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID));
        errorValue = valueFactory.create(ChildUtil.logger(logger, ERROR_ID));
        playbackDevices = propertiesFactory.create(ChildUtil.logger(logger, PLAYBACK));
        addPlaybackDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_PLAYBACK));
        powerDevices = propertiesFactory.create(ChildUtil.logger(logger, POWER));
        addPowerDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_POWER));
        runDevices = propertiesFactory.create(ChildUtil.logger(logger, RUN));
        addRunDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_RUN));
        temperatureSensorDevices = propertiesFactory.create(ChildUtil.logger(logger, TEMPERATURE_SENSOR));
        addTemperatureSensorDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_TEMPERATURE_SENSOR));
        volumeDevices = propertiesFactory.create(ChildUtil.logger(logger, VOLUME));
        addVolumeDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_VOLUME));
    }

    @Override
    protected void initChildren(String versionName, String internalName, Connection connection) throws JMSException {
        super.initChildren(versionName, internalName, connection);
        renameCommand.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Renameable.RENAME_ID),
                ChildUtil.name(internalName, RENAME_ID),
                connection);
        removeCommand.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Removeable.REMOVE_ID),
                ChildUtil.name(internalName, REMOVE_ID),
                connection);
        errorValue.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Failable.ERROR_ID),
                ChildUtil.name(internalName, ERROR_ID),
                connection);
        playbackDevices.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.System.PLAYBACK),
                ChildUtil.name(internalName, PLAYBACK),
                connection);
        addPlaybackDeviceCommand.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.System.ADD_PLAYBACK),
                ChildUtil.name(internalName, ADD_PLAYBACK),
                connection);
        powerDevices.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.System.POWER),
                ChildUtil.name(internalName, POWER),
                connection);
        addPowerDeviceCommand.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.System.ADD_POWER),
                ChildUtil.name(internalName, ADD_POWER),
                connection);
        runDevices.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.System.RUN),
                ChildUtil.name(internalName, RUN),
                connection);
        addRunDeviceCommand.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.System.ADD_RUN),
                ChildUtil.name(internalName, ADD_RUN),
                connection);
        temperatureSensorDevices.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.System.TEMPERATURE_SENSOR),
                ChildUtil.name(internalName, TEMPERATURE_SENSOR),
                connection);
        addTemperatureSensorDeviceCommand.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.System.ADD_TEMPERATURE_SENSOR),
                ChildUtil.name(internalName, ADD_TEMPERATURE_SENSOR),
                connection);
        volumeDevices.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.System.VOLUME),
                ChildUtil.name(internalName, VOLUME),
                connection);
        addVolumeDeviceCommand.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.System.ADD_VOLUME),
                ChildUtil.name(internalName, ADD_VOLUME),
                connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        removeCommand.uninit();
        errorValue.uninit();
        playbackDevices.uninit();
        addPlaybackDeviceCommand.uninit();
        powerDevices.uninit();
        addPowerDeviceCommand.uninit();
        runDevices.uninit();
        addRunDeviceCommand.uninit();
        temperatureSensorDevices.uninit();
        addTemperatureSensorDeviceCommand.uninit();
        volumeDevices.uninit();
        addVolumeDeviceCommand.uninit();
    }

    @Override
    public ProxyCommandBridge getRenameCommand() {
        return renameCommand;
    }

    @Override
    public ProxyCommandBridge getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public ProxyValueBridge getErrorValue() {
        return errorValue;
    }

    @Override
    public ProxyListBridge<ProxyPropertyBridge> getPlaybackDevices() {
        return playbackDevices;
    }

    @Override
    public ProxyCommandBridge getAddPlaybackDeviceCommand() {
        return addPlaybackDeviceCommand;
    }

    @Override
    public ProxyListBridge<ProxyPropertyBridge> getPowerDevices() {
        return powerDevices;
    }

    @Override
    public ProxyCommandBridge getAddPowerDeviceCommand() {
        return addPowerDeviceCommand;
    }

    @Override
    public ProxyListBridge<ProxyPropertyBridge> getRunDevices() {
        return runDevices;
    }

    @Override
    public ProxyCommandBridge getAddRunDeviceCommand() {
        return addRunDeviceCommand;
    }

    @Override
    public ProxyListBridge<ProxyPropertyBridge> getTemperatureSensorDevices() {
        return temperatureSensorDevices;
    }

    @Override
    public ProxyCommandBridge getAddTemperatureSensorDeviceCommand() {
        return addTemperatureSensorDeviceCommand;
    }

    @Override
    public ProxyListBridge<ProxyPropertyBridge> getVolumeDevices() {
        return volumeDevices;
    }

    @Override
    public ProxyCommandBridge getAddVolumeDeviceCommand() {
        return addVolumeDeviceCommand;
    }
}
