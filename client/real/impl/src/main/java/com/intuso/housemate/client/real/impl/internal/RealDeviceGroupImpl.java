package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.Failable;
import com.intuso.housemate.client.api.internal.Removeable;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.object.*;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.view.CommandView;
import com.intuso.housemate.client.api.internal.object.view.DeviceGroupView;
import com.intuso.housemate.client.api.internal.object.view.ValueView;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.housemate.client.api.internal.type.ObjectReference;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.proxy.internal.object.ProxyDevice;
import com.intuso.housemate.client.real.api.internal.RealDeviceGroup;
import com.intuso.housemate.client.real.impl.internal.type.TypeRepository;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.Set;

/**
 * Base class for all device
 */
public final class RealDeviceGroupImpl
        extends RealDeviceImpl<Device.Group.Data, Device.Group.Listener<? super RealDeviceGroupImpl>, DeviceGroupView, RealDeviceGroupImpl>
        implements RealDeviceGroup<RealCommandImpl, RealCommandImpl, RealCommandImpl, RealValueImpl<String>,
                RealListGeneratedImpl<RealCommandImpl>,
                RealListGeneratedImpl<RealValueImpl<?>>,
                RealListPersistedImpl<Value.Data, RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>>,
                RealDeviceGroupImpl> {

    private final static String PLAYBACK_NAME = "Playback devices";
    private final static String PLAYBACK_DESCRIPTION = "The device's playback devices";
    private final static String POWER_NAME = "Power devices";
    private final static String POWER_DESCRIPTION = "The device's power devices";
    private final static String RUN_NAME = "Run devices";
    private final static String RUN_DESCRIPTION = "The device's run devices";
    private final static String TEMPERATURE_SENSOR_NAME = "Temperature sensor devices";
    private final static String TEMPERATURE_SENSOR_DESCRIPTION = "The device's temperature sensor devices";
    private final static String VOLUME_NAME = "Volume devices";
    private final static String VOLUME_DESCRIPTION = "The device's volume devices";

    private final RealCommandImpl renameCommand;
    private final RealCommandImpl removeCommand;
    private final RealValueImpl<String> errorValue;
    private final RealListPersistedImpl<Value.Data, RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>> playbackDeviceReferences;
    private final ConvertingList<RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>, ProxyDevice<?, ?, ?, ?, ?, ?, ?>> playbackDevices;
    private final RealCommandImpl addPlaybackDeviceCommand;
    private final RealListPersistedImpl<Value.Data, RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>> powerDeviceReferences;
    private final ConvertingList<RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>, ProxyDevice<?, ?, ?, ?, ?, ?, ?>> powerDevices;
    private final RealCommandImpl addPowerDeviceCommand;
    private final RealListPersistedImpl<Value.Data, RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>> runDeviceReferences;
    private final ConvertingList<RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>, ProxyDevice<?, ?, ?, ?, ?, ?, ?>> runDevices;
    private final RealCommandImpl addRunDeviceCommand;
    private final RealListPersistedImpl<Value.Data, RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>> temperatureSensorDeviceReferences;
    private final ConvertingList<RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>, ProxyDevice<?, ?, ?, ?, ?, ?, ?>> temperatureSensorDevices;
    private final RealCommandImpl addTemperatureSensorDeviceCommand;
    private final RealListPersistedImpl<Value.Data, RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>> volumeDeviceReferences;
    private final ConvertingList<RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>, ProxyDevice<?, ?, ?, ?, ?, ?, ?>> volumeDevices;
    private final RealCommandImpl addVolumeDeviceCommand;

    private final RealListPersistedImpl.RemoveCallback<RealDeviceGroupImpl> removeCallback;

    /**
     * @param logger {@inheritDoc}
     * @param managedCollectionFactory
     */
    @Inject
    public RealDeviceGroupImpl(@Assisted final Logger logger,
                               @Assisted("id") String id,
                               @Assisted("name") String name,
                               @Assisted("description") String description,
                               @Assisted RealListPersistedImpl.RemoveCallback<RealDeviceGroupImpl> removeCallback,
                               ManagedCollectionFactory managedCollectionFactory,
                               Sender.Factory senderFactory,
                               RealCommandImpl.Factory commandFactory,
                               RealParameterImpl.Factory parameterFactory,
                               RealListGeneratedImpl.Factory<RealCommandImpl> commandsFactory,
                               RealListGeneratedImpl.Factory<RealValueImpl<?>> valuesFactory,
                               RealValueImpl.Factory valueFactory,
                               RealListPersistedImpl.Factory<Value.Data, RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>> devicesFactory,
                               final TypeRepository typeRepository) {
        super(logger, new Group.Data(id, name, description), managedCollectionFactory, senderFactory, commandFactory,
                parameterFactory, commandsFactory, valuesFactory, typeRepository);
        this.removeCallback = removeCallback;
        this.renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID),
                Renameable.RENAME_ID,
                Renameable.RENAME_ID,
                "Rename the device",
                new RealCommandImpl.Performer() {
                    @Override
                    public void perform(Type.InstanceMap values) {
                        if(values != null && values.getChildren().containsKey(Renameable.NAME_ID)) {
                            String newName = values.getChildren().get(Renameable.NAME_ID).getFirstValue();
                            if (newName != null && !RealDeviceGroupImpl.this.getName().equals(newName))
                                setName(newName);
                        }
                    }
                },
                Lists.newArrayList(parameterFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID, Renameable.NAME_ID),
                        Renameable.NAME_ID,
                        Renameable.NAME_ID,
                        "The new name",
                        typeRepository.getType(new TypeSpec(String.class)),
                        1,
                        1)));
        this.removeCommand = commandFactory.create(ChildUtil.logger(logger, Removeable.REMOVE_ID),
                Removeable.REMOVE_ID,
                Removeable.REMOVE_ID,
                "Remove the device",
                new RealCommandImpl.Performer() {
                    @Override
                    public void perform(Type.InstanceMap values) {
                        remove();
                    }
                },
                Lists.<RealParameterImpl<?>>newArrayList());
        this.errorValue = (RealValueImpl<String>) valueFactory.create(ChildUtil.logger(logger, Failable.ERROR_ID),
                Failable.ERROR_ID,
                Failable.ERROR_ID,
                "Current error for the device",
                typeRepository.getType(new TypeSpec(String.class)),
                1,
                1,
                Lists.<String>newArrayList());
        this.playbackDeviceReferences = devicesFactory.create(ChildUtil.logger(logger, PLAYBACK),
                PLAYBACK,
                PLAYBACK_NAME,
                PLAYBACK_DESCRIPTION);
        playbackDevices = new ConvertingList<>(playbackDeviceReferences, value -> value.getValue().getObject());
        this.addPlaybackDeviceCommand = /* todo */ null;
        this.powerDeviceReferences = devicesFactory.create(ChildUtil.logger(logger, POWER),
                POWER,
                POWER_NAME,
                POWER_DESCRIPTION);
        powerDevices = new ConvertingList<>(powerDeviceReferences, value -> value.getValue().getObject());
        this.addPowerDeviceCommand = /* todo */ null;
        this.runDeviceReferences = devicesFactory.create(ChildUtil.logger(logger, RUN),
                RUN,
                RUN_NAME,
                RUN_DESCRIPTION);
        runDevices = new ConvertingList<>(runDeviceReferences, value -> value.getValue().getObject());
        this.addRunDeviceCommand = /* todo */ null;
        this.temperatureSensorDeviceReferences = devicesFactory.create(ChildUtil.logger(logger, TEMPERATURE_SENSOR),
                TEMPERATURE_SENSOR,
                TEMPERATURE_SENSOR_NAME,
                TEMPERATURE_SENSOR_DESCRIPTION);
        temperatureSensorDevices = new ConvertingList<>(temperatureSensorDeviceReferences, value -> value.getValue().getObject());
        this.addTemperatureSensorDeviceCommand = /* todo */ null;
        this.volumeDeviceReferences = devicesFactory.create(ChildUtil.logger(logger, VOLUME),
                VOLUME,
                VOLUME_NAME,
                VOLUME_DESCRIPTION);
        volumeDevices = new ConvertingList<>(volumeDeviceReferences, value -> value.getValue().getObject());
        this.addVolumeDeviceCommand = /* todo */ null;
    }

    @Override
    public DeviceGroupView createView(View.Mode mode) {
        return new DeviceGroupView(mode);
    }

    @Override
    public Tree getTree(DeviceGroupView view, ValueBase.Listener listener) {

        // create a result even for a null view
        Tree result = super.getTree(view, listener);

        // get anything else the view wants
        if(view != null && view.getMode() != null) {
            switch (view.getMode()) {

                // get recursively
                case ANCESTORS:
                    result.getChildren().put(REMOVE_ID, removeCommand.getTree(new CommandView(View.Mode.ANCESTORS), listener));
                    result.getChildren().put(ERROR_ID, errorValue.getTree(new ValueView(View.Mode.ANCESTORS), listener));
                    break;

                    // get all children using inner view. NB all children non-null because of load(). Can give children null views
                case CHILDREN:
                    result.getChildren().put(REMOVE_ID, removeCommand.getTree(view.getRemoveCommand(), listener));
                    result.getChildren().put(ERROR_ID, errorValue.getTree(view.getErrorValue(), listener));
                    break;

                case SELECTION:
                    if(view.getRemoveCommand() != null)
                        result.getChildren().put(REMOVE_ID, removeCommand.getTree(view.getRemoveCommand(), listener));
                    if(view.getErrorValue() != null)
                        result.getChildren().put(ERROR_ID, errorValue.getTree(view.getErrorValue(), listener));
                    break;
            }
        }

        return result;
    }

    @Override
    protected void initChildren(String name) {
        super.initChildren(name);
        renameCommand.init(ChildUtil.name(name, RENAME_ID));
        removeCommand.init(ChildUtil.name(name, REMOVE_ID));
        errorValue.init(ChildUtil.name(name, ERROR_ID));
        playbackDeviceReferences.init(ChildUtil.name(name, PLAYBACK));
        addPlaybackDeviceCommand.init(ChildUtil.name(name, ADD_PLAYBACK));
        powerDeviceReferences.init(ChildUtil.name(name, POWER_DESCRIPTION));
        addPowerDeviceCommand.init(ChildUtil.name(name, ADD_POWER));
        runDeviceReferences.init(ChildUtil.name(name, RUN_DESCRIPTION));
        addRunDeviceCommand.init(ChildUtil.name(name, ADD_RUN));
        temperatureSensorDeviceReferences.init(ChildUtil.name(name, TEMPERATURE_SENSOR));
        addTemperatureSensorDeviceCommand.init(ChildUtil.name(name, ADD_TEMPERATURE_SENSOR));
        volumeDeviceReferences.init(ChildUtil.name(name, VOLUME));
        addVolumeDeviceCommand.init(ChildUtil.name(name, ADD_VOLUME));
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
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

    private void setName(String newName) {
        RealDeviceGroupImpl.this.getData().setName(newName);
        for(Group.Listener<? super RealDeviceGroupImpl> listener : listeners)
            listener.renamed(RealDeviceGroupImpl.this, RealDeviceGroupImpl.this.getName(), newName);
        data.setName(newName);
        sendData();
    }

    @Override
    public Set<String> getClasses() {
        return getData().getClasses();
    }

    @Override
    public Set<String> getAbilities() {
        return getData().getAbilities();
    }

    @Override
    public RealCommandImpl getRenameCommand() {
        return renameCommand;
    }

    @Override
    public RealCommandImpl getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public RealValueImpl<String> getErrorValue() {
        return errorValue;
    }

    protected final void remove() {
        removeCallback.remove(this);
    }

    public RealListPersistedImpl<Value.Data, RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>> getPlaybackDeviceReferences() {
        return playbackDeviceReferences;
    }

    @Override
    public ConvertingList<RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>, ProxyDevice<?, ?, ?, ?, ?, ?, ?>> getPlaybackDevices() {
        return playbackDevices;
    }

    @Override
    public RealCommandImpl getAddPlaybackDeviceCommand() {
        return addPlaybackDeviceCommand;
    }

    public RealListPersistedImpl<Value.Data, RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>> getPowerDeviceReferences() {
        return powerDeviceReferences;
    }

    @Override
    public ConvertingList<RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>, ProxyDevice<?, ?, ?, ?, ?, ?, ?>> getPowerDevices() {
        return powerDevices;
    }

    @Override
    public RealCommandImpl getAddPowerDeviceCommand() {
        return addPowerDeviceCommand;
    }

    public RealListPersistedImpl<Value.Data, RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>> getRunDeviceReferences() {
        return runDeviceReferences;
    }

    @Override
    public ConvertingList<RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>, ProxyDevice<?, ?, ?, ?, ?, ?, ?>> getRunDevices() {
        return runDevices;
    }

    @Override
    public RealCommandImpl getAddRunDeviceCommand() {
        return addRunDeviceCommand;
    }

    public RealListPersistedImpl<Value.Data, RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>> getTemperatureSensorDeviceReferences() {
        return temperatureSensorDeviceReferences;
    }

    @Override
    public ConvertingList<RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>, ProxyDevice<?, ?, ?, ?, ?, ?, ?>> getTemperatureSensorDevices() {
        return temperatureSensorDevices;
    }

    @Override
    public RealCommandImpl getAddTemperatureSensorDeviceCommand() {
        return addTemperatureSensorDeviceCommand;
    }

    public RealListPersistedImpl<Value.Data, RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>> getVolumeDeviceReferences() {
        return volumeDeviceReferences;
    }

    @Override
    public ConvertingList<RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>, ProxyDevice<?, ?, ?, ?, ?, ?, ?>> getVolumeDevices() {
        return volumeDevices;
    }

    @Override
    public RealCommandImpl getAddVolumeDeviceCommand() {
        return addVolumeDeviceCommand;
    }

    @Override
    public Object<?, ?, ?> getChild(String id) {
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

    public interface Factory {
        RealDeviceGroupImpl create(Logger logger,
                                   @Assisted("id") String id,
                                   @Assisted("name") String name,
                                   @Assisted("description") String description,
                                   RealListPersistedImpl.RemoveCallback<RealDeviceGroupImpl> removeCallback);
    }

    public static class LoadPersisted implements RealListPersistedImpl.ElementFactory<Group.Data, RealDeviceGroupImpl> {

        private final RealDeviceGroupImpl.Factory factory;

        @Inject
        public LoadPersisted(Factory factory) {
            this.factory = factory;
        }

        @Override
        public RealDeviceGroupImpl create(Logger logger, Group.Data data, RealListPersistedImpl.RemoveCallback<RealDeviceGroupImpl> removeCallback) {
            return factory.create(logger, data.getId(), data.getName(), data.getDescription(), removeCallback);
        }
    }
}
