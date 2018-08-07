package com.intuso.housemate.client.proxy.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.DeviceGroupMapper;
import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.api.internal.object.view.DeviceGroupView;
import com.intuso.housemate.client.api.internal.object.view.DeviceView;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class ProxyDeviceGroupBridge
        extends ProxyDeviceBridge<com.intuso.housemate.client.v1_0.api.object.Device.Group.Data,
        Device.Group.Data,
        Device.Group.Listener<? super ProxyDeviceGroupBridge>,
        DeviceGroupView,
        ProxyDeviceGroupBridge>
        implements Device.Group<
        ProxyCommandBridge,
        ProxyCommandBridge,
        ProxyCommandBridge,
        ProxyValueBridge,
        ProxyListBridge<ProxyDeviceComponentBridge>,
        ProxyListBridge<ProxyReferenceBridge<DeviceView<?>, ProxyDeviceBridge<?, ?, ?, DeviceView<?>, ?>>>,
        ProxyDeviceGroupBridge> {

    private final ProxyCommandBridge renameCommand;
    private final ProxyCommandBridge removeCommand;
    private final ProxyValueBridge errorValue;
    private final ProxyListBridge<ProxyReferenceBridge<DeviceView<?>, ProxyDeviceBridge<?, ?, ?, DeviceView<?>, ?>>> playbackDevices;
    private final ProxyCommandBridge addPlaybackDeviceCommand;
    private final ProxyListBridge<ProxyReferenceBridge<DeviceView<?>, ProxyDeviceBridge<?, ?, ?, DeviceView<?>, ?>>> powerDevices;
    private final ProxyCommandBridge addPowerDeviceCommand;
    private final ProxyListBridge<ProxyReferenceBridge<DeviceView<?>, ProxyDeviceBridge<?, ?, ?, DeviceView<?>, ?>>> runDevices;
    private final ProxyCommandBridge addRunDeviceCommand;
    private final ProxyListBridge<ProxyReferenceBridge<DeviceView<?>, ProxyDeviceBridge<?, ?, ?, DeviceView<?>, ?>>> temperatureSensorDevices;
    private final ProxyCommandBridge addTemperatureSensorDeviceCommand;
    private final ProxyListBridge<ProxyReferenceBridge<DeviceView<?>, ProxyDeviceBridge<?, ?, ?, DeviceView<?>, ?>>> volumeDevices;
    private final ProxyCommandBridge addVolumeDeviceCommand;

    @Inject
    protected ProxyDeviceGroupBridge(@Assisted Logger logger,
                                     DeviceGroupMapper deviceGroupMapper,
                                     ManagedCollectionFactory managedCollectionFactory,
                                     com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory,
                                     Sender.Factory v1_0SenderFactory,
                                     Factory<ProxyCommandBridge> commandFactory,
                                     Factory<ProxyValueBridge> valueFactory,
                                     Factory<ProxyListBridge<ProxyDeviceComponentBridge>> componentsFactory,
                                     Factory<ProxyListBridge<ProxyReferenceBridge<DeviceView<?>, ProxyDeviceBridge<?, ?, ?, DeviceView<?>, ?>>>> devicesFactory) {
        super(logger, Group.Data.class, deviceGroupMapper, managedCollectionFactory, internalReceiverFactory, v1_0SenderFactory, commandFactory, componentsFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID));
        removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID));
        errorValue = valueFactory.create(ChildUtil.logger(logger, ERROR_ID));
        playbackDevices = devicesFactory.create(ChildUtil.logger(logger, PLAYBACK));
        addPlaybackDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_PLAYBACK));
        powerDevices = devicesFactory.create(ChildUtil.logger(logger, POWER));
        addPowerDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_POWER));
        runDevices = devicesFactory.create(ChildUtil.logger(logger, RUN));
        addRunDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_RUN));
        temperatureSensorDevices = devicesFactory.create(ChildUtil.logger(logger, TEMPERATURE_SENSOR));
        addTemperatureSensorDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_TEMPERATURE_SENSOR));
        volumeDevices = devicesFactory.create(ChildUtil.logger(logger, VOLUME));
        addVolumeDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_VOLUME));
    }

    @Override
    protected void initChildren(String versionName, String internalName) {
        super.initChildren(versionName, internalName);
        renameCommand.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Renameable.RENAME_ID),
                ChildUtil.name(internalName, RENAME_ID)
        );
        removeCommand.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Removeable.REMOVE_ID),
                ChildUtil.name(internalName, REMOVE_ID)
        );
        errorValue.init(
                ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Failable.ERROR_ID),
                ChildUtil.name(internalName, ERROR_ID)
        );
        playbackDevices.init(
                ChildUtil.name(versionName, Group.PLAYBACK),
                ChildUtil.name(internalName, PLAYBACK)
        );
        addPlaybackDeviceCommand.init(
                ChildUtil.name(versionName, Group.ADD_PLAYBACK),
                ChildUtil.name(internalName, ADD_PLAYBACK)
        );
        powerDevices.init(
                ChildUtil.name(versionName, Group.POWER),
                ChildUtil.name(internalName, POWER)
        );
        addPowerDeviceCommand.init(
                ChildUtil.name(versionName, Group.ADD_POWER),
                ChildUtil.name(internalName, ADD_POWER)
        );
        runDevices.init(
                ChildUtil.name(versionName, Group.RUN),
                ChildUtil.name(internalName, RUN)
        );
        addRunDeviceCommand.init(
                ChildUtil.name(versionName, Group.ADD_RUN),
                ChildUtil.name(internalName, ADD_RUN)
        );
        temperatureSensorDevices.init(
                ChildUtil.name(versionName, Group.TEMPERATURE_SENSOR),
                ChildUtil.name(internalName, TEMPERATURE_SENSOR)
        );
        addTemperatureSensorDeviceCommand.init(
                ChildUtil.name(versionName, Group.ADD_TEMPERATURE_SENSOR),
                ChildUtil.name(internalName, ADD_TEMPERATURE_SENSOR)
        );
        volumeDevices.init(
                ChildUtil.name(versionName, Group.VOLUME),
                ChildUtil.name(internalName, VOLUME)
        );
        addVolumeDeviceCommand.init(
                ChildUtil.name(versionName, Group.ADD_VOLUME),
                ChildUtil.name(internalName, ADD_VOLUME)
        );
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
    public ProxyListBridge<ProxyReferenceBridge<DeviceView<?>, ProxyDeviceBridge<?, ?, ?, DeviceView<?>, ?>>> getPlaybackDevices() {
        return playbackDevices;
    }

    @Override
    public ProxyCommandBridge getAddPlaybackDeviceCommand() {
        return addPlaybackDeviceCommand;
    }

    @Override
    public ProxyListBridge<ProxyReferenceBridge<DeviceView<?>, ProxyDeviceBridge<?, ?, ?, DeviceView<?>, ?>>> getPowerDevices() {
        return powerDevices;
    }

    @Override
    public ProxyCommandBridge getAddPowerDeviceCommand() {
        return addPowerDeviceCommand;
    }

    @Override
    public ProxyListBridge<ProxyReferenceBridge<DeviceView<?>, ProxyDeviceBridge<?, ?, ?, DeviceView<?>, ?>>> getRunDevices() {
        return runDevices;
    }

    @Override
    public ProxyCommandBridge getAddRunDeviceCommand() {
        return addRunDeviceCommand;
    }

    @Override
    public ProxyListBridge<ProxyReferenceBridge<DeviceView<?>, ProxyDeviceBridge<?, ?, ?, DeviceView<?>, ?>>> getTemperatureSensorDevices() {
        return temperatureSensorDevices;
    }

    @Override
    public ProxyCommandBridge getAddTemperatureSensorDeviceCommand() {
        return addTemperatureSensorDeviceCommand;
    }

    @Override
    public ProxyListBridge<ProxyReferenceBridge<DeviceView<?>, ProxyDeviceBridge<?, ?, ?, DeviceView<?>, ?>>> getVolumeDevices() {
        return volumeDevices;
    }

    @Override
    public ProxyCommandBridge getAddVolumeDeviceCommand() {
        return addVolumeDeviceCommand;
    }

    @Override
    public ProxyObjectBridge<?, ?, ?, ?> getChild(String id) {
        if(REMOVE_ID.equals(id))
            return removeCommand;
        else if(ERROR_ID.equals(id))
            return errorValue;
        else if(PLAYBACK.equals(id))
            return playbackDevices;
        else if(ADD_PLAYBACK.equals(id))
            return addPlaybackDeviceCommand;
        else if(POWER.equals(id))
            return powerDevices;
        else if(ADD_POWER.equals(id))
            return addPowerDeviceCommand;
        else if(RUN.equals(id))
            return runDevices;
        else if(ADD_RUN.equals(id))
            return addRunDeviceCommand;
        else if(TEMPERATURE_SENSOR.equals(id))
            return temperatureSensorDevices;
        else if(ADD_TEMPERATURE_SENSOR.equals(id))
            return addTemperatureSensorDeviceCommand;
        else if(VOLUME.equals(id))
            return volumeDevices;
        else if(ADD_VOLUME.equals(id))
            return addVolumeDeviceCommand;
        return super.getChild(id);
    }
}
