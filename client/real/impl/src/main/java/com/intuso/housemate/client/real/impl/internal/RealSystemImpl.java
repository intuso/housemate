package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.Failable;
import com.intuso.housemate.client.api.internal.Removeable;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.object.Property;
import com.intuso.housemate.client.api.internal.object.System;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.type.ObjectReference;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.proxy.internal.object.ProxyDevice;
import com.intuso.housemate.client.real.api.internal.RealSystem;
import com.intuso.housemate.client.real.impl.internal.type.TypeRepository;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Base class for all device
 */
public final class RealSystemImpl
        extends RealObject<System.Data, System.Listener<? super RealSystemImpl>>
        implements RealSystem<RealValueImpl<String>, RealCommandImpl,
                RealListPersistedImpl<Property.Data, RealPropertyImpl<ObjectReference<ProxyDevice.Simple>>>, RealSystemImpl> {

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
    private final RealListPersistedImpl<Property.Data, RealPropertyImpl<ObjectReference<ProxyDevice.Simple>>> playbackDevices;
    private final RealCommandImpl addPlaybackDeviceCommand;
    private final RealListPersistedImpl<Property.Data, RealPropertyImpl<ObjectReference<ProxyDevice.Simple>>> powerDevices;
    private final RealCommandImpl addPowerDeviceCommand;
    private final RealListPersistedImpl<Property.Data, RealPropertyImpl<ObjectReference<ProxyDevice.Simple>>> runDevices;
    private final RealCommandImpl addRunDeviceCommand;
    private final RealListPersistedImpl<Property.Data, RealPropertyImpl<ObjectReference<ProxyDevice.Simple>>> temperatureSensorDevices;
    private final RealCommandImpl addTemperatureSensorDeviceCommand;
    private final RealListPersistedImpl<Property.Data, RealPropertyImpl<ObjectReference<ProxyDevice.Simple>>> volumeDevices;
    private final RealCommandImpl addVolumeDeviceCommand;

    private final RealListPersistedImpl.RemoveCallback<RealSystemImpl> removeCallback;

    /**
     * @param logger {@inheritDoc}
     * @param managedCollectionFactory
     */
    @Inject
    public RealSystemImpl(@Assisted final Logger logger,
                          @Assisted("id") String id,
                          @Assisted("name") String name,
                          @Assisted("description") String description,
                          @Assisted RealListPersistedImpl.RemoveCallback<RealSystemImpl> removeCallback,
                          ManagedCollectionFactory managedCollectionFactory,
                          Sender.Factory senderFactory,
                          RealCommandImpl.Factory commandFactory,
                          RealParameterImpl.Factory parameterFactory,
                          RealValueImpl.Factory valueFactory,
                          RealListPersistedImpl.Factory<Property.Data, RealPropertyImpl<ObjectReference<ProxyDevice.Simple>>> devicesFactory,
                          final TypeRepository typeRepository) {
        super(logger, new System.Data(id, name, description), managedCollectionFactory, senderFactory);
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
                            if (newName != null && !RealSystemImpl.this.getName().equals(newName))
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
        this.playbackDevices = devicesFactory.create(ChildUtil.logger(logger, PLAYBACK),
                PLAYBACK,
                PLAYBACK_NAME,
                PLAYBACK_DESCRIPTION);
        this.addPlaybackDeviceCommand = /* todo */ null;
        this.powerDevices = devicesFactory.create(ChildUtil.logger(logger, POWER),
                POWER,
                POWER_NAME,
                POWER_DESCRIPTION);
        this.addPowerDeviceCommand = /* todo */ null;
        this.runDevices = devicesFactory.create(ChildUtil.logger(logger, RUN),
                RUN,
                RUN_NAME,
                RUN_DESCRIPTION);
        this.addRunDeviceCommand = /* todo */ null;
        this.temperatureSensorDevices = devicesFactory.create(ChildUtil.logger(logger, TEMPERATURE_SENSOR),
                TEMPERATURE_SENSOR,
                TEMPERATURE_SENSOR_NAME,
                TEMPERATURE_SENSOR_DESCRIPTION);
        this.addTemperatureSensorDeviceCommand = /* todo */ null;
        this.volumeDevices = devicesFactory.create(ChildUtil.logger(logger, VOLUME),
                VOLUME,
                VOLUME_NAME,
                VOLUME_DESCRIPTION);
        this.addVolumeDeviceCommand = /* todo */ null;
    }

    @Override
    protected void initChildren(String name) {
        super.initChildren(name);
        renameCommand.init(ChildUtil.name(name, RENAME_ID));
        removeCommand.init(ChildUtil.name(name, REMOVE_ID));
        errorValue.init(ChildUtil.name(name, ERROR_ID));
        playbackDevices.init(ChildUtil.name(name, PLAYBACK));
        addPlaybackDeviceCommand.init(ChildUtil.name(name, ADD_PLAYBACK));
        powerDevices.init(ChildUtil.name(name, POWER_DESCRIPTION));
        addPowerDeviceCommand.init(ChildUtil.name(name, ADD_POWER));
        runDevices.init(ChildUtil.name(name, RUN_DESCRIPTION));
        addRunDeviceCommand.init(ChildUtil.name(name, ADD_RUN));
        temperatureSensorDevices.init(ChildUtil.name(name, TEMPERATURE_SENSOR));
        addTemperatureSensorDeviceCommand.init(ChildUtil.name(name, ADD_TEMPERATURE_SENSOR));
        volumeDevices.init(ChildUtil.name(name, VOLUME));
        addVolumeDeviceCommand.init(ChildUtil.name(name, ADD_VOLUME));
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

    private void setName(String newName) {
        RealSystemImpl.this.getData().setName(newName);
        for(System.Listener<? super RealSystemImpl> listener : listeners)
            listener.renamed(RealSystemImpl.this, RealSystemImpl.this.getName(), newName);
        data.setName(newName);
        sendData();
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

    @Override
    public RealListPersistedImpl<Property.Data, RealPropertyImpl<ObjectReference<ProxyDevice.Simple>>> getPlaybackDevices() {
        return playbackDevices;
    }

    @Override
    public RealCommandImpl getAddPlaybackDeviceCommand() {
        return addPlaybackDeviceCommand;
    }

    @Override
    public RealListPersistedImpl<Property.Data, RealPropertyImpl<ObjectReference<ProxyDevice.Simple>>> getPowerDevices() {
        return powerDevices;
    }

    @Override
    public RealCommandImpl getAddPowerDeviceCommand() {
        return addPowerDeviceCommand;
    }

    @Override
    public RealListPersistedImpl<Property.Data, RealPropertyImpl<ObjectReference<ProxyDevice.Simple>>> getRunDevices() {
        return runDevices;
    }

    @Override
    public RealCommandImpl getAddRunDeviceCommand() {
        return addRunDeviceCommand;
    }

    @Override
    public RealListPersistedImpl<Property.Data, RealPropertyImpl<ObjectReference<ProxyDevice.Simple>>> getTemperatureSensorDevices() {
        return temperatureSensorDevices;
    }

    @Override
    public RealCommandImpl getAddTemperatureSensorDeviceCommand() {
        return addTemperatureSensorDeviceCommand;
    }

    @Override
    public RealListPersistedImpl<Property.Data, RealPropertyImpl<ObjectReference<ProxyDevice.Simple>>> getVolumeDevices() {
        return volumeDevices;
    }

    @Override
    public RealCommandImpl getAddVolumeDeviceCommand() {
        return addVolumeDeviceCommand;
    }

    public interface Factory {
        RealSystemImpl create(Logger logger,
                              @Assisted("id") String id,
                              @Assisted("name") String name,
                              @Assisted("description") String description,
                              RealListPersistedImpl.RemoveCallback<RealSystemImpl> removeCallback);
    }

    public static class LoadPersisted implements RealListPersistedImpl.ElementFactory<System.Data, RealSystemImpl> {

        private final RealSystemImpl.Factory factory;

        @Inject
        public LoadPersisted(Factory factory) {
            this.factory = factory;
        }

        @Override
        public RealSystemImpl create(Logger logger, System.Data data, RealListPersistedImpl.RemoveCallback<RealSystemImpl> removeCallback) {
            return factory.create(logger, data.getId(), data.getName(), data.getDescription(), removeCallback);
        }
    }
}
