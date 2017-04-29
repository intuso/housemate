package com.intuso.housemate.client.proxy.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.DeviceCombiMapper;
import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class ProxyDeviceCombiBridge
        extends ProxyDeviceBridge<com.intuso.housemate.client.v1_0.api.object.Device.Combi.Data,
        Device.Combi.Data,
        Device.Combi.Listener<? super ProxyDeviceCombiBridge>,
        ProxyDeviceCombiBridge>
        implements Device.Combi<
        ProxyCommandBridge,
        ProxyCommandBridge,
        ProxyCommandBridge,
        ProxyValueBridge,
        ProxyListBridge<ProxyCommandBridge>,
        ProxyListBridge<ProxyValueBridge>,
        ProxyListBridge<ProxyPropertyBridge>,
        ProxyDeviceCombiBridge> {

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
    protected ProxyDeviceCombiBridge(@Assisted Logger logger,
                                     DeviceCombiMapper deviceCombiMapper,
                                     ManagedCollectionFactory managedCollectionFactory,
                                     com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory,
                                     Sender.Factory v1_0SenderFactory,
                                     Factory<ProxyCommandBridge> commandFactory,
                                     Factory<ProxyValueBridge> valueFactory,
                                     Factory<ProxyListBridge<ProxyCommandBridge>> commandsFactory,
                                     Factory<ProxyListBridge<ProxyValueBridge>> valuesFactory,
                                     Factory<ProxyListBridge<ProxyPropertyBridge>> devicesFactory) {
        super(logger, Device.Combi.Data.class, deviceCombiMapper, managedCollectionFactory, internalReceiverFactory, v1_0SenderFactory, commandFactory, commandsFactory, valuesFactory);
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
                ChildUtil.name(versionName, Device.Combi.PLAYBACK),
                ChildUtil.name(internalName, PLAYBACK)
        );
        addPlaybackDeviceCommand.init(
                ChildUtil.name(versionName, Device.Combi.ADD_PLAYBACK),
                ChildUtil.name(internalName, ADD_PLAYBACK)
        );
        powerDevices.init(
                ChildUtil.name(versionName, Device.Combi.POWER),
                ChildUtil.name(internalName, POWER)
        );
        addPowerDeviceCommand.init(
                ChildUtil.name(versionName, Device.Combi.ADD_POWER),
                ChildUtil.name(internalName, ADD_POWER)
        );
        runDevices.init(
                ChildUtil.name(versionName, Device.Combi.RUN),
                ChildUtil.name(internalName, RUN)
        );
        addRunDeviceCommand.init(
                ChildUtil.name(versionName, Device.Combi.ADD_RUN),
                ChildUtil.name(internalName, ADD_RUN)
        );
        temperatureSensorDevices.init(
                ChildUtil.name(versionName, Device.Combi.TEMPERATURE_SENSOR),
                ChildUtil.name(internalName, TEMPERATURE_SENSOR)
        );
        addTemperatureSensorDeviceCommand.init(
                ChildUtil.name(versionName, Device.Combi.ADD_TEMPERATURE_SENSOR),
                ChildUtil.name(internalName, ADD_TEMPERATURE_SENSOR)
        );
        volumeDevices.init(
                ChildUtil.name(versionName, Device.Combi.VOLUME),
                ChildUtil.name(internalName, VOLUME)
        );
        addVolumeDeviceCommand.init(
                ChildUtil.name(versionName, Device.Combi.ADD_VOLUME),
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
