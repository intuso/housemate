package com.intuso.housemate.client.proxy.internal.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.ConvertingList;
import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.api.internal.object.Object;
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
public abstract class ProxyDeviceGroup<
        COMMAND extends ProxyCommand<?, ?, COMMAND>,
        COMMANDS extends ProxyList<? extends ProxyCommand<?, ?, ?>, ?>,
        VALUE extends ProxyValue<?, VALUE>,
        VALUES extends ProxyList<? extends VALUE, ?>,
        DEVICE extends ProxyDevice<?, ?, ?, ?, ?, ?>,
        DEVICE_GROUP extends ProxyDeviceGroup<COMMAND, COMMANDS, VALUE, VALUES, DEVICE, DEVICE_GROUP>>
        extends ProxyDevice<Device.Group.Data, Device.Group.Listener<? super DEVICE_GROUP>, COMMAND, COMMANDS, VALUES, DEVICE_GROUP>
        implements Device.Group<COMMAND, COMMAND, COMMAND, VALUE, COMMANDS, VALUES, ConvertingList<VALUE, DEVICE>, DEVICE_GROUP>,
        ProxyFailable<VALUE>,
        ProxyRemoveable<COMMAND> {

    private final COMMAND removeCommand;
    private final VALUE errorValue;
    private final VALUES playbackDeviceReferences;
    private final ConvertingList<VALUE, DEVICE> playbackDevices;
    private final COMMAND addPlaybackDeviceCommand;
    private final VALUES powerDeviceReferences;
    private final ConvertingList<VALUE, DEVICE> powerDevices;
    private final COMMAND addPowerDeviceCommand;
    private final VALUES runDeviceReferences;
    private final ConvertingList<VALUE, DEVICE> runDevices;
    private final COMMAND addRunDeviceCommand;
    private final VALUES temperatureSensorDeviceReferences;
    private final ConvertingList<VALUE, DEVICE> temperatureSensorDevices;
    private final COMMAND addTemperatureSensorDeviceCommand;
    private final VALUES volumeDeviceReferences;
    private final ConvertingList<VALUE, DEVICE> volumeDevices;
    private final COMMAND addVolumeDeviceCommand;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyDeviceGroup(Logger logger,
                            ManagedCollectionFactory managedCollectionFactory,
                            Receiver.Factory receiverFactory,
                            ProxyServer<?, ?, ?, ?, ?, ?, ?, ?, ?> server,
                            Factory<COMMAND> commandFactory,
                            Factory<COMMANDS> commandsFactory,
                            Factory<VALUE> valueFactory,
                            Factory<VALUES> valuesFactory) {
        super(logger, Group.Data.class, managedCollectionFactory, receiverFactory, commandFactory, commandsFactory, valuesFactory);
        removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID));
        errorValue = valueFactory.create(ChildUtil.logger(logger, ERROR_ID));
        playbackDeviceReferences = valuesFactory.create(ChildUtil.logger(logger, PLAYBACK));
        playbackDevices = new ConvertingList<>(playbackDeviceReferences, server.findConverter());
        addPlaybackDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_PLAYBACK));
        powerDeviceReferences = valuesFactory.create(ChildUtil.logger(logger, POWER));
        powerDevices = new ConvertingList<>(powerDeviceReferences, server.findConverter());
        addPowerDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_POWER));
        runDeviceReferences = valuesFactory.create(ChildUtil.logger(logger, RUN));
        runDevices = new ConvertingList<>(runDeviceReferences, server.findConverter());
        addRunDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_RUN));
        temperatureSensorDeviceReferences = valuesFactory.create(ChildUtil.logger(logger, TEMPERATURE_SENSOR));
        temperatureSensorDevices = new ConvertingList<>(temperatureSensorDeviceReferences, server.findConverter());
        addTemperatureSensorDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_TEMPERATURE_SENSOR));
        volumeDeviceReferences = valuesFactory.create(ChildUtil.logger(logger, VOLUME));
        volumeDevices = new ConvertingList<>(volumeDeviceReferences, server.findConverter());
        addVolumeDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_VOLUME));
    }

    @Override
    protected void initChildren(String name) {
        super.initChildren(name);
        removeCommand.init(ChildUtil.name(name, REMOVE_ID));
        errorValue.init(ChildUtil.name(name, ERROR_ID));
        playbackDeviceReferences.init(ChildUtil.name(name, PLAYBACK));
        addPlaybackDeviceCommand.init(ChildUtil.name(name, ADD_PLAYBACK));
        powerDeviceReferences.init(ChildUtil.name(name, POWER));
        addPowerDeviceCommand.init(ChildUtil.name(name, ADD_POWER));
        runDeviceReferences.init(ChildUtil.name(name, RUN));
        addRunDeviceCommand.init(ChildUtil.name(name, ADD_RUN));
        temperatureSensorDeviceReferences.init(ChildUtil.name(name, TEMPERATURE_SENSOR));
        addTemperatureSensorDeviceCommand.init(ChildUtil.name(name, ADD_TEMPERATURE_SENSOR));
        volumeDeviceReferences.init(ChildUtil.name(name, VOLUME));
        addVolumeDeviceCommand.init(ChildUtil.name(name, ADD_VOLUME));
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        removeCommand.uninit();
        errorValue.uninit();
        playbackDeviceReferences.uninit();
        addPlaybackDeviceCommand.uninit();
        powerDeviceReferences.uninit();
        addPowerDeviceCommand.uninit();
        runDeviceReferences.uninit();
        addRunDeviceCommand.uninit();
        temperatureSensorDeviceReferences.uninit();
        addTemperatureSensorDeviceCommand.uninit();
        volumeDeviceReferences.uninit();
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

    public VALUES getPlaybackDeviceReferences() {
        return playbackDeviceReferences;
    }

    @Override
    public ConvertingList<VALUE, DEVICE> getPlaybackDevices() {
        return playbackDevices;
    }

    @Override
    public COMMAND getAddPlaybackDeviceCommand() {
        return addPlaybackDeviceCommand;
    }

    public VALUES getPowerDeviceReferences() {
        return powerDeviceReferences;
    }

    @Override
    public ConvertingList<VALUE, DEVICE> getPowerDevices() {
        return powerDevices;
    }

    @Override
    public COMMAND getAddPowerDeviceCommand() {
        return addPowerDeviceCommand;
    }

    public VALUES getRunDeviceReferences() {
        return runDeviceReferences;
    }

    @Override
    public ConvertingList<VALUE, DEVICE> getRunDevices() {
        return runDevices;
    }

    @Override
    public COMMAND getAddRunDeviceCommand() {
        return addRunDeviceCommand;
    }

    public VALUES getTemperatureSensorDeviceReferences() {
        return temperatureSensorDeviceReferences;
    }

    @Override
    public ConvertingList<VALUE, DEVICE> getTemperatureSensorDevices() {
        return temperatureSensorDevices;
    }

    @Override
    public COMMAND getAddTemperatureSensorDeviceCommand() {
        return addTemperatureSensorDeviceCommand;
    }

    public VALUES getVolumeDeviceReferences() {
        return volumeDeviceReferences;
    }

    @Override
    public ConvertingList<VALUE, DEVICE> getVolumeDevices() {
        return volumeDevices;
    }

    @Override
    public COMMAND getAddVolumeDeviceCommand() {
        return addVolumeDeviceCommand;
    }

    @Override
    public Object<?> getChild(String id) {
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
    public static final class Simple extends ProxyDeviceGroup<
                ProxyCommand.Simple,
                ProxyList.Simple<ProxyCommand.Simple>,
                ProxyValue.Simple,
                ProxyList.Simple<ProxyValue.Simple>,
                ProxyDevice<?, ?, ?, ?, ?, ?>,
                Simple> {

        @Inject
        public Simple(@Assisted Logger logger,
                      ManagedCollectionFactory managedCollectionFactory,
                      Receiver.Factory receiverFactory,
                      ProxyServer.Simple server,
                      Factory<ProxyCommand.Simple> commandFactory,
                      Factory<ProxyList.Simple<ProxyCommand.Simple>> commandsFactory,
                      Factory<ProxyValue.Simple> valueFactory,
                      Factory<ProxyList.Simple<ProxyValue.Simple>> valuesFactory) {
            super(logger, managedCollectionFactory, receiverFactory, server, commandFactory, commandsFactory, valueFactory, valuesFactory);
        }
    }
}
