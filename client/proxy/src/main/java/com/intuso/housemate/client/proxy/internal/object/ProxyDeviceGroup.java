package com.intuso.housemate.client.proxy.internal.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.Tree;
import com.intuso.housemate.client.api.internal.object.view.*;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.housemate.client.proxy.internal.ProxyFailable;
import com.intuso.housemate.client.proxy.internal.ProxyRemoveable;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;

/**
 * @param <COMMAND> the type of the commands
 * @param <VALUE> the type of the values
 */
public abstract class ProxyDeviceGroup<
        COMMAND extends ProxyCommand<?, ?, ?>,
        VALUE extends ProxyValue<?, ?>,
        DEVICE_COMPONENTS extends ProxyList<? extends ProxyDeviceComponent<?, ?, ?>, ?>,
        DEVICES extends ProxyList<? extends ProxyReference<DeviceView<?>, ? extends ProxyDevice<?, ?, ?, ?, ?, ?>, ?>, ?>,
        DEVICE_GROUP extends ProxyDeviceGroup<COMMAND, VALUE, DEVICE_COMPONENTS, DEVICES, DEVICE_GROUP>>
        extends ProxyDevice<Device.Group.Data, Device.Group.Listener<? super DEVICE_GROUP>, DeviceGroupView, COMMAND, DEVICE_COMPONENTS, DEVICE_GROUP>
        implements Device.Group<COMMAND, COMMAND, COMMAND, VALUE, DEVICE_COMPONENTS, DEVICES, DEVICE_GROUP>,
        ProxyFailable<VALUE>,
        ProxyRemoveable<COMMAND> {

    private final Factory<COMMAND> commandFactory;
    private final Factory<VALUE> valueFactory;
    private final Factory<DEVICES> devicesFactory;

    private COMMAND removeCommand;
    private VALUE errorValue;
    private DEVICES playbackDevices;
    private COMMAND addPlaybackDeviceCommand;
    private DEVICES powerDevices;
    private COMMAND addPowerDeviceCommand;
    private DEVICES runDevices;
    private COMMAND addRunDeviceCommand;
    private DEVICES temperatureSensorDevices;
    private COMMAND addTemperatureSensorDeviceCommand;
    private DEVICES volumeDevices;
    private COMMAND addVolumeDeviceCommand;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyDeviceGroup(Logger logger,
                            String path,
                            String name,
                            ManagedCollectionFactory managedCollectionFactory,
                            Receiver.Factory receiverFactory,
                            Factory<COMMAND> commandFactory,
                            Factory<VALUE> valueFactory,
                            Factory<DEVICE_COMPONENTS> componentsFactory,
                            Factory<DEVICES> devicesFactory) {
        super(logger, path, name, Group.Data.class, managedCollectionFactory, receiverFactory, commandFactory, componentsFactory);
        this.commandFactory = commandFactory;
        this.valueFactory = valueFactory;
        this.devicesFactory = devicesFactory;
    }

    @Override
    public DeviceGroupView createView(View.Mode mode) {
        return new DeviceGroupView(mode);
    }

    @Override
    public Tree getTree(DeviceGroupView view, Tree.ReferenceHandler referenceHandler, Tree.Listener listener, List<ManagedCollection.Registration> listenerRegistrations) {

        // make sure what they want is loaded
        load(view);

        // create a result even for a null view
        Tree result = super.getTree(view, referenceHandler, listener, listenerRegistrations);

        // get anything else the view wants
        if(view != null && view.getMode() != null) {
            switch (view.getMode()) {

                // get recursively
                case ANCESTORS:
                    result.getChildren().put(REMOVE_ID, removeCommand.getTree(new CommandView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(ERROR_ID, errorValue.getTree(new ValueView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(PLAYBACK, playbackDevices.getTree(new ListView<DeviceView<?>>(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(ADD_PLAYBACK, addPlaybackDeviceCommand.getTree(new CommandView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(POWER, powerDevices.getTree(new ListView<DeviceView<?>>(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(ADD_POWER, addPowerDeviceCommand.getTree(new CommandView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(RUN, runDevices.getTree(new ListView<DeviceView<?>>(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(ADD_RUN, addRunDeviceCommand.getTree(new CommandView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(TEMPERATURE_SENSOR, temperatureSensorDevices.getTree(new ListView<DeviceView<?>>(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(ADD_TEMPERATURE_SENSOR, addTemperatureSensorDeviceCommand.getTree(new CommandView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(VOLUME, volumeDevices.getTree(new ListView<DeviceView<?>>(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(ADD_VOLUME, addVolumeDeviceCommand.getTree(new CommandView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    break;

                // get all children using inner view. NB all children non-null because of load(). Can give children null views
                case CHILDREN:
                    result.getChildren().put(REMOVE_ID, removeCommand.getTree(view.getRemoveCommand(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(ERROR_ID, errorValue.getTree(view.getErrorValue(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(PLAYBACK, playbackDevices.getTree(view.getPlaybackDevices(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(ADD_PLAYBACK, addPlaybackDeviceCommand.getTree(view.getAddPlaybackDevice(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(POWER, powerDevices.getTree(view.getPowerDevices(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(ADD_POWER, addPowerDeviceCommand.getTree(view.getAddPowerDevice(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(RUN, runDevices.getTree(view.getRunDevices(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(ADD_RUN, addRunDeviceCommand.getTree(view.getAddRunDevice(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(TEMPERATURE_SENSOR, temperatureSensorDevices.getTree(view.getTemperatureSensorDevices(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(ADD_TEMPERATURE_SENSOR, addTemperatureSensorDeviceCommand.getTree(view.getAddTemperatureSensorDevice(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(VOLUME, volumeDevices.getTree(view.getVolumeDevices(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(ADD_VOLUME, addVolumeDeviceCommand.getTree(view.getAddVolumeDevice(), referenceHandler, listener, listenerRegistrations));
                    break;

                case SELECTION:
                    if(view.getRemoveCommand() != null)
                        result.getChildren().put(REMOVE_ID, removeCommand.getTree(view.getRemoveCommand(), referenceHandler, listener, listenerRegistrations));
                    if(view.getErrorValue() != null)
                        result.getChildren().put(ERROR_ID, errorValue.getTree(view.getErrorValue(), referenceHandler, listener, listenerRegistrations));
                    if(view.getPlaybackDevices() != null)
                        result.getChildren().put(PLAYBACK, playbackDevices.getTree(view.getPlaybackDevices(), referenceHandler, listener, listenerRegistrations));
                    if(view.getAddPlaybackDevice() != null)
                        result.getChildren().put(ADD_PLAYBACK, addPlaybackDeviceCommand.getTree(view.getAddPlaybackDevice(), referenceHandler, listener, listenerRegistrations));
                    if(view.getPowerDevices() != null)
                        result.getChildren().put(POWER, powerDevices.getTree(view.getPowerDevices(), referenceHandler, listener, listenerRegistrations));
                    if(view.getAddPowerDevice() != null)
                        result.getChildren().put(ADD_POWER, addPowerDeviceCommand.getTree(view.getAddPowerDevice(), referenceHandler, listener, listenerRegistrations));
                    if(view.getRunDevices() != null)
                        result.getChildren().put(RUN, runDevices.getTree(view.getRunDevices(), referenceHandler, listener, listenerRegistrations));
                    if(view.getAddRunDevice() != null)
                        result.getChildren().put(ADD_RUN, addRunDeviceCommand.getTree(view.getAddRunDevice(), referenceHandler, listener, listenerRegistrations));
                    if(view.getTemperatureSensorDevices() != null)
                        result.getChildren().put(TEMPERATURE_SENSOR, temperatureSensorDevices.getTree(view.getTemperatureSensorDevices(), referenceHandler, listener, listenerRegistrations));
                    if(view.getAddTemperatureSensorDevice() != null)
                        result.getChildren().put(ADD_TEMPERATURE_SENSOR, addTemperatureSensorDeviceCommand.getTree(view.getAddTemperatureSensorDevice(), referenceHandler, listener, listenerRegistrations));
                    if(view.getVolumeDevices() != null)
                        result.getChildren().put(VOLUME, volumeDevices.getTree(view.getVolumeDevices(), referenceHandler, listener, listenerRegistrations));
                    if(view.getAddVolumeDevice() != null)
                        result.getChildren().put(ADD_VOLUME, addVolumeDeviceCommand.getTree(view.getAddVolumeDevice(), referenceHandler, listener, listenerRegistrations));
                    break;
            }
        }

        return result;
    }

    @Override
    public void load(DeviceGroupView view) {

        super.load(view);

        if(view == null || view.getMode() == null)
            return;

        // create things according to the view's mode, sub-views, and what's already created
        switch (view.getMode()) {
            case ANCESTORS:
            case CHILDREN:
                if(removeCommand == null)
                    removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID), ChildUtil.path(path, REMOVE_ID), ChildUtil.name(name, REMOVE_ID));
                if (errorValue == null)
                    errorValue = valueFactory.create(ChildUtil.logger(logger, ERROR_ID), ChildUtil.path(path, ERROR_ID), ChildUtil.name(name, ERROR_ID));
                if(playbackDevices == null)
                    playbackDevices = devicesFactory.create(ChildUtil.logger(logger, PLAYBACK), ChildUtil.path(path, PLAYBACK), ChildUtil.name(name, PLAYBACK));
                if(addPlaybackDeviceCommand == null)
                    addPlaybackDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_PLAYBACK), ChildUtil.path(path, ADD_PLAYBACK), ChildUtil.name(name, ADD_PLAYBACK));
                if(powerDevices == null)
                    powerDevices = devicesFactory.create(ChildUtil.logger(logger, POWER), ChildUtil.path(path, POWER), ChildUtil.name(name, POWER));
                if(addPowerDeviceCommand == null)
                    addPowerDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_POWER), ChildUtil.path(path, ADD_POWER), ChildUtil.name(name, ADD_POWER));
                if(runDevices == null)
                    runDevices = devicesFactory.create(ChildUtil.logger(logger, RUN), ChildUtil.path(path, RUN), ChildUtil.name(name, RUN));
                if(addRunDeviceCommand == null)
                    addRunDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_RUN), ChildUtil.path(path, ADD_RUN), ChildUtil.name(name, ADD_RUN));
                if(temperatureSensorDevices == null)
                    temperatureSensorDevices = devicesFactory.create(ChildUtil.logger(logger, TEMPERATURE_SENSOR), ChildUtil.path(path, TEMPERATURE_SENSOR), ChildUtil.name(name, TEMPERATURE_SENSOR));
                if(addTemperatureSensorDeviceCommand == null)
                    addTemperatureSensorDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_TEMPERATURE_SENSOR), ChildUtil.path(path, ADD_TEMPERATURE_SENSOR), ChildUtil.name(name, ADD_TEMPERATURE_SENSOR));
                if(volumeDevices == null)
                    volumeDevices = devicesFactory.create(ChildUtil.logger(logger, VOLUME), ChildUtil.path(path, VOLUME), ChildUtil.name(name, VOLUME));
                if(addVolumeDeviceCommand == null)
                    addVolumeDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_VOLUME), ChildUtil.path(path, ADD_VOLUME), ChildUtil.name(name, ADD_VOLUME));
                break;
            case SELECTION:
                if(removeCommand == null && view.getRemoveCommand() != null)
                    removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID), ChildUtil.path(path, REMOVE_ID), ChildUtil.name(name, REMOVE_ID));
                if (errorValue == null && view.getErrorValue() != null)
                    errorValue = valueFactory.create(ChildUtil.logger(logger, ERROR_ID), ChildUtil.path(path, ERROR_ID), ChildUtil.name(name, ERROR_ID));
                if(playbackDevices == null && view.getPlaybackDevices() == null)
                    playbackDevices = devicesFactory.create(ChildUtil.logger(logger, PLAYBACK), ChildUtil.path(path, PLAYBACK), ChildUtil.name(name, PLAYBACK));
                if(addPlaybackDeviceCommand == null && view.getAddPlaybackDevice() == null)
                    addPlaybackDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_PLAYBACK), ChildUtil.path(path, ADD_PLAYBACK), ChildUtil.name(name, ADD_PLAYBACK));
                if(powerDevices == null && view.getPowerDevices() == null)
                    powerDevices = devicesFactory.create(ChildUtil.logger(logger, POWER), ChildUtil.path(path, POWER), ChildUtil.name(name, POWER));
                if(addPowerDeviceCommand == null && view.getAddPowerDevice() == null)
                    addPowerDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_POWER), ChildUtil.path(path, ADD_POWER), ChildUtil.name(name, ADD_POWER));
                if(runDevices == null && view.getRunDevices() == null)
                    runDevices = devicesFactory.create(ChildUtil.logger(logger, RUN), ChildUtil.path(path, RUN), ChildUtil.name(name, RUN));
                if(addRunDeviceCommand == null && view.getAddRunDevice() == null)
                    addRunDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_RUN), ChildUtil.path(path, ADD_RUN), ChildUtil.name(name, ADD_RUN));
                if(temperatureSensorDevices == null && view.getTemperatureSensorDevices() == null)
                    temperatureSensorDevices = devicesFactory.create(ChildUtil.logger(logger, TEMPERATURE_SENSOR), ChildUtil.path(path, TEMPERATURE_SENSOR), ChildUtil.name(name, TEMPERATURE_SENSOR));
                if(addTemperatureSensorDeviceCommand == null && view.getAddTemperatureSensorDevice() == null)
                    addTemperatureSensorDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_TEMPERATURE_SENSOR), ChildUtil.path(path, ADD_TEMPERATURE_SENSOR), ChildUtil.name(name, ADD_TEMPERATURE_SENSOR));
                if(volumeDevices == null && view.getVolumeDevices() == null)
                    volumeDevices = devicesFactory.create(ChildUtil.logger(logger, VOLUME), ChildUtil.path(path, VOLUME), ChildUtil.name(name, VOLUME));
                if(addVolumeDeviceCommand == null && view.getAddVolumeDevice() == null)
                    addVolumeDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_VOLUME), ChildUtil.path(path, ADD_VOLUME), ChildUtil.name(name, ADD_VOLUME));
                break;
        }

        // view things according to the view's mode and sub-views
        switch (view.getMode()) {
            case ANCESTORS:
                removeCommand.load(new CommandView(View.Mode.ANCESTORS));
                errorValue.load(new ValueView(View.Mode.ANCESTORS));
                playbackDevices.load(new ListView<DeviceView<?>>(View.Mode.ANCESTORS));
                addPlaybackDeviceCommand.load(new CommandView(View.Mode.ANCESTORS));
                powerDevices.load(new ListView<DeviceView<?>>(View.Mode.ANCESTORS));
                addPowerDeviceCommand.load(new CommandView(View.Mode.ANCESTORS));
                runDevices.load(new ListView<DeviceView<?>>(View.Mode.ANCESTORS));
                addRunDeviceCommand.load(new CommandView(View.Mode.ANCESTORS));
                temperatureSensorDevices.load(new ListView<DeviceView<?>>(View.Mode.ANCESTORS));
                addTemperatureSensorDeviceCommand.load(new CommandView(View.Mode.ANCESTORS));
                volumeDevices.load(new ListView<DeviceView<?>>(View.Mode.ANCESTORS));
                addVolumeDeviceCommand.load(new CommandView(View.Mode.ANCESTORS));
                break;
            case CHILDREN:
            case SELECTION:
                if (view.getRemoveCommand() != null)
                    removeCommand.load(view.getRemoveCommand());
                if (view.getErrorValue() != null)
                    errorValue.load(view.getErrorValue());
                if(view.getPlaybackDevices() != null)
                    playbackDevices.load(view.getPlaybackDevices());
                if(view.getAddPlaybackDevice() != null)
                    addPlaybackDeviceCommand.load(view.getAddPlaybackDevice());
                if(view.getPowerDevices() != null)
                    powerDevices.load(view.getPowerDevices());
                if(view.getAddPowerDevice() != null)
                    addPowerDeviceCommand.load(view.getAddPowerDevice());
                if(view.getRunDevices() != null)
                    runDevices.load(view.getRunDevices());
                if(view.getAddRunDevice() != null)
                    addRunDeviceCommand.load(view.getAddRunDevice());
                if(view.getTemperatureSensorDevices() != null)
                    temperatureSensorDevices.load(view.getTemperatureSensorDevices());
                if(view.getAddTemperatureSensorDevice() != null)
                    addTemperatureSensorDeviceCommand.load(view.getAddTemperatureSensorDevice());
                if(view.getVolumeDevices() != null)
                    volumeDevices.load(view.getVolumeDevices());
                if(view.getAddVolumeDevice() != null)
                    addVolumeDeviceCommand.load(view.getAddVolumeDevice());
                break;
        }
    }

    @Override
    public void loadRemoveCommand(CommandView commandView) {
        if(removeCommand == null)
            removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID), ChildUtil.path(path, REMOVE_ID), ChildUtil.name(name, REMOVE_ID));
        removeCommand.load(commandView);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        if(removeCommand != null)
            removeCommand.uninit();
        if(errorValue != null)
            errorValue.uninit();
        if(playbackDevices != null)
            playbackDevices.uninit();
        if(addPlaybackDeviceCommand != null)
            addPlaybackDeviceCommand.uninit();
        if(powerDevices != null)
            powerDevices.uninit();
        if(addPowerDeviceCommand != null)
            addPowerDeviceCommand.uninit();
        if(runDevices != null)
            runDevices.uninit();
        if(addRunDeviceCommand != null)
            addRunDeviceCommand.uninit();
        if(temperatureSensorDevices != null)
            temperatureSensorDevices.uninit();
        if(addTemperatureSensorDeviceCommand != null)
            addTemperatureSensorDeviceCommand.uninit();
        if(volumeDevices != null)
            volumeDevices.uninit();
        if(addVolumeDeviceCommand != null)
            addVolumeDeviceCommand.uninit();
    }

    @Override
    public COMMAND getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public final String getError() {
        return errorValue.getValues() != null ? errorValue.getValues().getFirstValue() : null;
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
    public Object<?, ?, ?> getChild(String id) {
        if(REMOVE_ID.equals(id)) {
            if(removeCommand == null)
                removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID), ChildUtil.path(path, REMOVE_ID), ChildUtil.name(name, REMOVE_ID));
            return removeCommand;
        }
        else if(ERROR_ID.equals(id)) {
            if (errorValue == null)
                errorValue = valueFactory.create(ChildUtil.logger(logger, ERROR_ID), ChildUtil.path(path, ERROR_ID), ChildUtil.name(name, ERROR_ID));
            return errorValue;
        }
        else if(PLAYBACK.equals(id)) {
            if(playbackDevices == null)
                playbackDevices = devicesFactory.create(ChildUtil.logger(logger, PLAYBACK), ChildUtil.path(path, PLAYBACK), ChildUtil.name(name, PLAYBACK));
            return playbackDevices;
        } else if(ADD_PLAYBACK.equals(id)) {
            if(addPlaybackDeviceCommand == null)
                addPlaybackDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_PLAYBACK), ChildUtil.path(path, ADD_PLAYBACK), ChildUtil.name(name, ADD_PLAYBACK));
            return addPlaybackDeviceCommand;
        } else if(POWER.equals(id)) {
            if(powerDevices == null)
                powerDevices = devicesFactory.create(ChildUtil.logger(logger, POWER), ChildUtil.path(path, POWER), ChildUtil.name(name, POWER));
            return powerDevices;
        } else if(ADD_POWER.equals(id)) {
            if(addPowerDeviceCommand == null)
                addPowerDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_POWER), ChildUtil.path(path, ADD_POWER), ChildUtil.name(name, ADD_POWER));
            return addPowerDeviceCommand;
        } else if(RUN.equals(id)) {
            if(runDevices == null)
                runDevices = devicesFactory.create(ChildUtil.logger(logger, RUN), ChildUtil.path(path, RUN), ChildUtil.name(name, RUN));
            return runDevices;
        } else if(ADD_RUN.equals(id)) {
            if(addRunDeviceCommand == null)
                addRunDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_RUN), ChildUtil.path(path, ADD_RUN), ChildUtil.name(name, ADD_RUN));
            return addRunDeviceCommand;
        } else if(TEMPERATURE_SENSOR.equals(id)) {
            if(temperatureSensorDevices == null)
                temperatureSensorDevices = devicesFactory.create(ChildUtil.logger(logger, TEMPERATURE_SENSOR), ChildUtil.path(path, TEMPERATURE_SENSOR), ChildUtil.name(name, TEMPERATURE_SENSOR));
            return temperatureSensorDevices;
        } else if(ADD_TEMPERATURE_SENSOR.equals(id)) {
            if(addTemperatureSensorDeviceCommand == null)
                addTemperatureSensorDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_TEMPERATURE_SENSOR), ChildUtil.path(path, ADD_TEMPERATURE_SENSOR), ChildUtil.name(name, ADD_TEMPERATURE_SENSOR));
            return addTemperatureSensorDeviceCommand;
        } else if(VOLUME.equals(id)) {
            if(volumeDevices == null)
                volumeDevices = devicesFactory.create(ChildUtil.logger(logger, VOLUME), ChildUtil.path(path, VOLUME), ChildUtil.name(name, VOLUME));
            return volumeDevices;
        } else if(ADD_VOLUME.equals(id)) {
            if(addVolumeDeviceCommand == null)
                addVolumeDeviceCommand = commandFactory.create(ChildUtil.logger(logger, ADD_VOLUME), ChildUtil.path(path, ADD_VOLUME), ChildUtil.name(name, ADD_VOLUME));
            return addVolumeDeviceCommand;
        }
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
            ProxyValue.Simple,
            ProxyList.Simple<ProxyDeviceComponent.Simple>,
            ProxyList.Simple<ProxyReference.Simple<DeviceView<?>, ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?>>>,
            Simple> {

        @Inject
        public Simple(@Assisted Logger logger,
                      @Assisted("path") String path,
                      @Assisted("name") String name,
                      ManagedCollectionFactory managedCollectionFactory,
                      Receiver.Factory receiverFactory,
                      Factory<ProxyCommand.Simple> commandFactory,
                      Factory<ProxyValue.Simple> valueFactory,
                      Factory<ProxyList.Simple<ProxyDeviceComponent.Simple>> componentsFactory,
                      Factory<ProxyList.Simple<ProxyReference.Simple<DeviceView<?>, ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?>>>> devicesFactory) {
            super(logger, path, name, managedCollectionFactory, receiverFactory, commandFactory, valueFactory, componentsFactory, devicesFactory);
        }
    }
}
