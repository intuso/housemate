package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.Failable;
import com.intuso.housemate.client.api.internal.Removeable;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.object.*;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.view.*;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.proxy.internal.object.ProxyDevice;
import com.intuso.housemate.client.real.api.internal.RealDeviceGroup;
import com.intuso.housemate.client.real.impl.internal.type.TypeRepository;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;
import java.util.Set;

/**
 * Base class for all device
 */
public final class RealDeviceGroupImpl
        extends RealDeviceImpl<Device.Group.Data, Device.Group.Listener<? super RealDeviceGroupImpl>, DeviceGroupView, RealDeviceGroupImpl>
        implements RealDeviceGroup<RealCommandImpl, RealCommandImpl, RealCommandImpl, RealValueImpl<String>,
                RealListGeneratedImpl<RealCommandImpl>,
                RealListGeneratedImpl<RealValueImpl<?>>,
                RealListPersistedImpl<Reference.Data, RealReferenceImpl<DeviceView<?>, ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?, ?>>>,
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
    private final RealListPersistedImpl<Reference.Data, RealReferenceImpl<DeviceView<?>, ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?, ?>>> playbackDevices;
    private final RealCommandImpl addPlaybackDeviceCommand;
    private final RealListPersistedImpl<Reference.Data, RealReferenceImpl<DeviceView<?>, ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?, ?>>> powerDevices;
    private final RealCommandImpl addPowerDeviceCommand;
    private final RealListPersistedImpl<Reference.Data, RealReferenceImpl<DeviceView<?>, ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?, ?>>> runDevices;
    private final RealCommandImpl addRunDeviceCommand;
    private final RealListPersistedImpl<Reference.Data, RealReferenceImpl<DeviceView<?>, ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?, ?>>> temperatureSensorDevices;
    private final RealCommandImpl addTemperatureSensorDeviceCommand;
    private final RealListPersistedImpl<Reference.Data, RealReferenceImpl<DeviceView<?>, ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?, ?>>> volumeDevices;
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
                               RealCommandImpl.Factory commandFactory,
                               RealParameterImpl.Factory parameterFactory,
                               RealListGeneratedImpl.Factory<RealCommandImpl> commandsFactory,
                               RealListGeneratedImpl.Factory<RealValueImpl<?>> valuesFactory,
                               RealValueImpl.Factory valueFactory,
                               RealListPersistedImpl.Factory<Reference.Data, RealReferenceImpl<DeviceView<?>, ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?, ?>>> devicesFactory,
                               final TypeRepository typeRepository) {
        super(logger, new Group.Data(id, name, description), managedCollectionFactory, commandFactory,
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
    public DeviceGroupView createView(View.Mode mode) {
        return new DeviceGroupView(mode);
    }

    @Override
    public Tree getTree(DeviceGroupView view, Tree.ReferenceHandler referenceHandler, Tree.Listener listener, List<ManagedCollection.Registration> listenerRegistrations) {

        // create a result even for a null view
        Tree result = super.getTree(view, referenceHandler, listener, listenerRegistrations);

        // get anything else the view wants
        if(view != null && view.getMode() != null) {
            switch (view.getMode()) {

                // get recursively
                case ANCESTORS:
                    result.getChildren().put(REMOVE_ID, removeCommand.getTree(new CommandView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(ERROR_ID, errorValue.getTree(new ValueView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    break;

                    // get all children using inner view. NB all children non-null because of load(). Can give children null views
                case CHILDREN:
                    result.getChildren().put(REMOVE_ID, removeCommand.getTree(view.getRemoveCommand(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(ERROR_ID, errorValue.getTree(view.getErrorValue(), referenceHandler, listener, listenerRegistrations));
                    break;

                case SELECTION:
                    if(view.getRemoveCommand() != null)
                        result.getChildren().put(REMOVE_ID, removeCommand.getTree(view.getRemoveCommand(), referenceHandler, listener, listenerRegistrations));
                    if(view.getErrorValue() != null)
                        result.getChildren().put(ERROR_ID, errorValue.getTree(view.getErrorValue(), referenceHandler, listener, listenerRegistrations));
                    break;
            }
        }

        return result;
    }

    @Override
    protected void initChildren(String name, Sender.Factory senderFactory, Receiver.Factory receiverFactory) {
        super.initChildren(name, senderFactory, receiverFactory);
        renameCommand.init(ChildUtil.name(name, RENAME_ID), senderFactory, receiverFactory);
        removeCommand.init(ChildUtil.name(name, REMOVE_ID), senderFactory, receiverFactory);
        errorValue.init(ChildUtil.name(name, ERROR_ID), senderFactory, receiverFactory);
        playbackDevices.init(ChildUtil.name(name, PLAYBACK), senderFactory, receiverFactory);
        addPlaybackDeviceCommand.init(ChildUtil.name(name, ADD_PLAYBACK), senderFactory, receiverFactory);
        powerDevices.init(ChildUtil.name(name, POWER_DESCRIPTION), senderFactory, receiverFactory);
        addPowerDeviceCommand.init(ChildUtil.name(name, ADD_POWER), senderFactory, receiverFactory);
        runDevices.init(ChildUtil.name(name, RUN_DESCRIPTION), senderFactory, receiverFactory);
        addRunDeviceCommand.init(ChildUtil.name(name, ADD_RUN), senderFactory, receiverFactory);
        temperatureSensorDevices.init(ChildUtil.name(name, TEMPERATURE_SENSOR), senderFactory, receiverFactory);
        addTemperatureSensorDeviceCommand.init(ChildUtil.name(name, ADD_TEMPERATURE_SENSOR), senderFactory, receiverFactory);
        volumeDevices.init(ChildUtil.name(name, VOLUME), senderFactory, receiverFactory);
        addVolumeDeviceCommand.init(ChildUtil.name(name, ADD_VOLUME), senderFactory, receiverFactory);
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
        RealDeviceGroupImpl.this.getData().setName(newName);
        for(Group.Listener<? super RealDeviceGroupImpl> listener : listeners)
            listener.renamed(RealDeviceGroupImpl.this, RealDeviceGroupImpl.this.getName(), newName);
        data.setName(newName);
        dataUpdated();
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

    @Override
    public RealListPersistedImpl<Reference.Data, RealReferenceImpl<DeviceView<?>, ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?, ?>>> getPlaybackDevices() {
        return playbackDevices;
    }

    @Override
    public RealCommandImpl getAddPlaybackDeviceCommand() {
        return addPlaybackDeviceCommand;
    }

    @Override
    public RealListPersistedImpl<Reference.Data, RealReferenceImpl<DeviceView<?>, ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?, ?>>> getPowerDevices() {
        return powerDevices;
    }

    @Override
    public RealCommandImpl getAddPowerDeviceCommand() {
        return addPowerDeviceCommand;
    }

    @Override
    public RealListPersistedImpl<Reference.Data, RealReferenceImpl<DeviceView<?>, ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?, ?>>> getRunDevices() {
        return runDevices;
    }

    @Override
    public RealCommandImpl getAddRunDeviceCommand() {
        return addRunDeviceCommand;
    }

    @Override
    public RealListPersistedImpl<Reference.Data, RealReferenceImpl<DeviceView<?>, ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?, ?>>> getTemperatureSensorDevices() {
        return temperatureSensorDevices;
    }

    @Override
    public RealCommandImpl getAddTemperatureSensorDeviceCommand() {
        return addTemperatureSensorDeviceCommand;
    }

    @Override
    public RealListPersistedImpl<Reference.Data, RealReferenceImpl<DeviceView<?>, ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?, ?>>> getVolumeDevices() {
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
