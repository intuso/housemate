package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.Failable;
import com.intuso.housemate.client.api.internal.Removeable;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.api.internal.object.Property;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.type.ObjectReference;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.proxy.internal.object.ProxyDevice;
import com.intuso.housemate.client.real.api.internal.RealDeviceCombi;
import com.intuso.housemate.client.real.impl.internal.annotation.AnnotationParser;
import com.intuso.housemate.client.real.impl.internal.type.TypeRepository;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Base class for all device
 */
public final class RealDeviceCombiImpl
        extends RealDeviceImpl<Device.Combi.Data, Device.Combi.Listener<? super RealDeviceCombiImpl>, RealDeviceCombiImpl>
        implements RealDeviceCombi<RealCommandImpl, RealCommandImpl, RealCommandImpl, RealValueImpl<String>,
        RealListGeneratedImpl<RealCommandImpl>,
        RealListGeneratedImpl<RealValueImpl<?>>,
        RealListPersistedImpl<Property.Data, RealPropertyImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?>>>>, RealDeviceCombiImpl> {

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
    private final RealListPersistedImpl<Property.Data, RealPropertyImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?>>>> playbackDevices;
    private final RealCommandImpl addPlaybackDeviceCommand;
    private final RealListPersistedImpl<Property.Data, RealPropertyImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?>>>> powerDevices;
    private final RealCommandImpl addPowerDeviceCommand;
    private final RealListPersistedImpl<Property.Data, RealPropertyImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?>>>> runDevices;
    private final RealCommandImpl addRunDeviceCommand;
    private final RealListPersistedImpl<Property.Data, RealPropertyImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?>>>> temperatureSensorDevices;
    private final RealCommandImpl addTemperatureSensorDeviceCommand;
    private final RealListPersistedImpl<Property.Data, RealPropertyImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?>>>> volumeDevices;
    private final RealCommandImpl addVolumeDeviceCommand;

    private final RealListPersistedImpl.RemoveCallback<RealDeviceCombiImpl> removeCallback;

    /**
     * @param logger {@inheritDoc}
     * @param managedCollectionFactory
     */
    @Inject
    public RealDeviceCombiImpl(@Assisted final Logger logger,
                               @Assisted("id") String id,
                               @Assisted("name") String name,
                               @Assisted("description") String description,
                               @Assisted RealListPersistedImpl.RemoveCallback<RealDeviceCombiImpl> removeCallback,
                               ManagedCollectionFactory managedCollectionFactory,
                               Sender.Factory senderFactory,
                               AnnotationParser annotationParser,
                               RealCommandImpl.Factory commandFactory,
                               RealParameterImpl.Factory parameterFactory,
                               RealListGeneratedImpl.Factory<RealCommandImpl> commandsFactory,
                               RealListGeneratedImpl.Factory<RealValueImpl<?>> valuesFactory,
                               RealValueImpl.Factory valueFactory,
                               RealListPersistedImpl.Factory<Property.Data, RealPropertyImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?>>>> devicesFactory,
                               final TypeRepository typeRepository) {
        super(logger, new Device.Combi.Data(id, name, description), managedCollectionFactory, senderFactory,
                annotationParser, commandFactory, parameterFactory, commandsFactory, valuesFactory, typeRepository);
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
                            if (newName != null && !RealDeviceCombiImpl.this.getName().equals(newName))
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
        RealDeviceCombiImpl.this.getData().setName(newName);
        for(Device.Combi.Listener<? super RealDeviceCombiImpl> listener : listeners)
            listener.renamed(RealDeviceCombiImpl.this, RealDeviceCombiImpl.this.getName(), newName);
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
    public RealListPersistedImpl<Property.Data, RealPropertyImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?>>>> getPlaybackDevices() {
        return playbackDevices;
    }

    @Override
    public RealCommandImpl getAddPlaybackDeviceCommand() {
        return addPlaybackDeviceCommand;
    }

    @Override
    public RealListPersistedImpl<Property.Data, RealPropertyImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?>>>> getPowerDevices() {
        return powerDevices;
    }

    @Override
    public RealCommandImpl getAddPowerDeviceCommand() {
        return addPowerDeviceCommand;
    }

    @Override
    public RealListPersistedImpl<Property.Data, RealPropertyImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?>>>> getRunDevices() {
        return runDevices;
    }

    @Override
    public RealCommandImpl getAddRunDeviceCommand() {
        return addRunDeviceCommand;
    }

    @Override
    public RealListPersistedImpl<Property.Data, RealPropertyImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?>>>> getTemperatureSensorDevices() {
        return temperatureSensorDevices;
    }

    @Override
    public RealCommandImpl getAddTemperatureSensorDeviceCommand() {
        return addTemperatureSensorDeviceCommand;
    }

    @Override
    public RealListPersistedImpl<Property.Data, RealPropertyImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?>>>> getVolumeDevices() {
        return volumeDevices;
    }

    @Override
    public RealCommandImpl getAddVolumeDeviceCommand() {
        return addVolumeDeviceCommand;
    }

    public interface Factory {
        RealDeviceCombiImpl create(Logger logger,
                                   @Assisted("id") String id,
                                   @Assisted("name") String name,
                                   @Assisted("description") String description,
                                   RealListPersistedImpl.RemoveCallback<RealDeviceCombiImpl> removeCallback);
    }

    public static class LoadPersisted implements RealListPersistedImpl.ElementFactory<Device.Combi.Data, RealDeviceCombiImpl> {

        private final RealDeviceCombiImpl.Factory factory;

        @Inject
        public LoadPersisted(Factory factory) {
            this.factory = factory;
        }

        @Override
        public RealDeviceCombiImpl create(Logger logger, Device.Combi.Data data, RealListPersistedImpl.RemoveCallback<RealDeviceCombiImpl> removeCallback) {
            return factory.create(logger, data.getId(), data.getName(), data.getDescription(), removeCallback);
        }
    }
}
