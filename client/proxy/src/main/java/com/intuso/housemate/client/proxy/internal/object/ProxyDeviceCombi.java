package com.intuso.housemate.client.proxy.internal.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.housemate.client.proxy.internal.ProxyFailable;
import com.intuso.housemate.client.proxy.internal.ProxyRemoveable;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * @param <COMMAND> the type of the commands
 * @param <VALUE> the type of the values
 * @param <DEVICE> the type of the device
 */
public abstract class ProxyDeviceCombi<
        COMMAND extends ProxyCommand<?, ?, COMMAND>,
        COMMANDS extends ProxyList<? extends ProxyCommand<?, ?, ?>, ?>,
        VALUE extends ProxyValue<?, VALUE>,
        VALUES extends ProxyList<? extends ProxyValue<?, ?>, ?>,
        DEVICES extends ProxyList<? extends ProxyProperty<?, ?, ?>, ?>,
        DEVICE extends ProxyDeviceCombi<COMMAND, COMMANDS, VALUE, VALUES, DEVICES, DEVICE>>
        extends ProxyDevice<Device.Combi.Data, Device.Combi.Listener<? super DEVICE>, COMMAND, COMMANDS, VALUES, DEVICE>
        implements Device.Combi<COMMAND, COMMAND, COMMAND, VALUE, COMMANDS, VALUES, DEVICES, DEVICE>,
        ProxyFailable<VALUE>,
        ProxyRemoveable<COMMAND> {

    private final COMMAND removeCommand;
    private final VALUE errorValue;
    private final DEVICES playbackDevices;
    private final COMMAND addPlaybackDeviceCommand;
    private final DEVICES powerDevices;
    private final COMMAND addPowerDeviceCommand;
    private final DEVICES runDevices;
    private final COMMAND addRunDeviceCommand;
    private final DEVICES temperatureSensorDevices;
    private final COMMAND addTemperatureSensorDeviceCommand;
    private final DEVICES volumeDevices;
    private final COMMAND addVolumeDeviceCommand;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyDeviceCombi(Logger logger,
                            ManagedCollectionFactory managedCollectionFactory,
                            Receiver.Factory receiverFactory,
                            Factory<COMMAND> commandFactory,
                            Factory<COMMANDS> commandsFactory,
                            Factory<VALUE> valueFactory,
                            Factory<VALUES> valuesFactory,
                            Factory<DEVICES> devicesFactory) {
        super(logger, Device.Combi.Data.class, managedCollectionFactory, receiverFactory, commandFactory, commandsFactory, valuesFactory);
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
    protected void initChildren(String name) {
        super.initChildren(name);
        removeCommand.init(ChildUtil.name(name, REMOVE_ID));
        errorValue.init(ChildUtil.name(name, ERROR_ID));
        playbackDevices.init(ChildUtil.name(name, PLAYBACK));
        addPlaybackDeviceCommand.init(ChildUtil.name(name, ADD_PLAYBACK));
        powerDevices.init(ChildUtil.name(name, POWER));
        addPowerDeviceCommand.init(ChildUtil.name(name, ADD_POWER));
        runDevices.init(ChildUtil.name(name, RUN));
        addRunDeviceCommand.init(ChildUtil.name(name, ADD_RUN));
        temperatureSensorDevices.init(ChildUtil.name(name, TEMPERATURE_SENSOR));
        addTemperatureSensorDeviceCommand.init(ChildUtil.name(name, ADD_TEMPERATURE_SENSOR));
        volumeDevices.init(ChildUtil.name(name, VOLUME));
        addVolumeDeviceCommand.init(ChildUtil.name(name, ADD_VOLUME));
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
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
    public COMMAND getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public final String getError() {
        return errorValue.getValue() != null ? errorValue.getValue().getFirstValue() : null;
    }

    @Override
    public VALUE getErrorValue() {
        return errorValue;
    }

    @Override
    public DEVICES getPlaybackDevices() {
        return playbackDevices;
    }

    @Override
    public COMMAND getAddPlaybackDeviceCommand() {
        return addPlaybackDeviceCommand;
    }

    @Override
    public DEVICES getPowerDevices() {
        return powerDevices;
    }

    @Override
    public COMMAND getAddPowerDeviceCommand() {
        return addPowerDeviceCommand;
    }

    @Override
    public DEVICES getRunDevices() {
        return runDevices;
    }

    @Override
    public COMMAND getAddRunDeviceCommand() {
        return addRunDeviceCommand;
    }

    @Override
    public DEVICES getTemperatureSensorDevices() {
        return temperatureSensorDevices;
    }

    @Override
    public COMMAND getAddTemperatureSensorDeviceCommand() {
        return addTemperatureSensorDeviceCommand;
    }

    @Override
    public DEVICES getVolumeDevices() {
        return volumeDevices;
    }

    @Override
    public COMMAND getAddVolumeDeviceCommand() {
        return addVolumeDeviceCommand;
    }

    @Override
    public ProxyObject<?, ?> getChild(String id) {
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

    /**
     * Created with IntelliJ IDEA.
     * User: tomc
     * Date: 14/01/14
     * Time: 13:16
     * To change this template use File | Settings | File Templates.
     */
    public static final class Simple extends ProxyDeviceCombi<
            ProxyCommand.Simple,
            ProxyList.Simple<ProxyCommand.Simple>,
            ProxyValue.Simple,
            ProxyList.Simple<ProxyValue.Simple>,
            ProxyList.Simple<ProxyProperty.Simple>,
            Simple> {

        @Inject
        public Simple(@Assisted Logger logger,
                      ManagedCollectionFactory managedCollectionFactory,
                      Receiver.Factory receiverFactory,
                      Factory<ProxyCommand.Simple> commandFactory,
                      Factory<ProxyList.Simple<ProxyCommand.Simple>> commandsFactory,
                      Factory<ProxyValue.Simple> valueFactory,
                      Factory<ProxyList.Simple<ProxyValue.Simple>> valuesFactory,
                      Factory<ProxyList.Simple<ProxyProperty.Simple>> devicesFactory) {
            super(logger, managedCollectionFactory, receiverFactory, commandFactory, commandsFactory, valueFactory, valuesFactory, devicesFactory);
        }
    }
}
