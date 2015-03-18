package com.intuso.housemate.realclient.factory;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.real.*;
import com.intuso.housemate.object.real.annotations.AnnotationProcessor;
import com.intuso.housemate.object.real.impl.type.RealChoiceType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.housemate.persistence.api.Persistence;
import com.intuso.housemate.plugin.api.TypeInfo;
import com.intuso.housemate.plugin.host.PluginListener;
import com.intuso.housemate.plugin.host.PluginManager;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 */
public final class DeviceFactory implements PluginListener {

    public final static String TYPE_ID = "device-factory";
    public final static String TYPE_NAME = "Device Factory";
    public final static String TYPE_DESCRIPTION = "Available types for new devices";

    public final static String NAME_PARAMETER_ID = "name";
    public final static String NAME_PARAMETER_NAME = "Name";
    public final static String NAME_PARAMETER_DESCRIPTION = "The name of the new device";
    public final static String DESCRIPTION_PARAMETER_ID = "description";
    public final static String DESCRIPTION_PARAMETER_NAME = "Description";
    public final static String DESCRIPTION_PARAMETER_DESCRIPTION = "Description for the new device";
    public final static String TYPE_PARAMETER_ID = "type";
    public final static String TYPE_PARAMETER_NAME = "Type";
    public final static String TYPE_PARAMETER_DESCRIPTION = "The type of the new device";

    private final Log log;
    private final ListenersFactory listenersFactory;
    private final Persistence persistence;
    private final AnnotationProcessor annotationProcessor;

    private final Map<String, Factory.Entry<com.intuso.housemate.api.object.device.DeviceFactory<? extends RealDevice>>> factoryEntries = Maps.newHashMap();
    private final DeviceFactoryType type;

    @Inject
    public DeviceFactory(Log log, ListenersFactory listenersFactory, Persistence persistence,
                         AnnotationProcessor annotationProcessor, PluginManager pluginManager) {
        this.log = log;
        this.listenersFactory = listenersFactory;
        this.persistence = persistence;
        this.annotationProcessor = annotationProcessor;
        type = new DeviceFactoryType(log);
        pluginManager.addPluginListener(this, true);
    }

    public DeviceFactoryType getType() {
        return type;
    }

    public RealCommand createAddDeviceCommand(String commandId, String commandName, String commandDescription,
                                              final RealList<TypeData<?>, RealType<?, ?, ?>> types,
                                              final RealList<DeviceData, RealDevice> list) {
        return new RealCommand(log, listenersFactory, commandId, commandName, commandDescription, Arrays.asList(
                new RealParameter<String>(log, listenersFactory, NAME_PARAMETER_ID, NAME_PARAMETER_NAME, NAME_PARAMETER_DESCRIPTION, new StringType(log, listenersFactory)),
                new RealParameter<String>(log, listenersFactory, DESCRIPTION_PARAMETER_ID, DESCRIPTION_PARAMETER_NAME, DESCRIPTION_PARAMETER_DESCRIPTION, new StringType(log, listenersFactory)),
                new RealParameter<Factory.Entry<com.intuso.housemate.api.object.device.DeviceFactory<? extends RealDevice>>>(log, listenersFactory, TYPE_PARAMETER_ID, TYPE_PARAMETER_NAME, TYPE_PARAMETER_DESCRIPTION, type)
        )) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                TypeInstances deviceType = values.getChildren().get(TYPE_PARAMETER_ID);
                if(deviceType == null || deviceType.getFirstValue() == null)
                    throw new HousemateException("No device type specified");
                TypeInstances name = values.getChildren().get(NAME_PARAMETER_ID);
                if(name == null || name.getFirstValue() == null)
                    throw new HousemateException("No device name specified");
                TypeInstances description = values.getChildren().get(DESCRIPTION_PARAMETER_ID);
                if(description == null || description.getFirstValue() == null)
                    throw new HousemateException("No device description specified");
                Factory.Entry<com.intuso.housemate.api.object.device.DeviceFactory<? extends RealDevice>> deviceFactoryEntry = type.deserialise(deviceType.getElements().get(0));
                if(deviceFactoryEntry == null)
                    throw new HousemateException("No factory known for device type " + deviceType);
                RealDevice device = deviceFactoryEntry.getInjector().getInstance(deviceFactoryEntry.getFactoryKey())
                        .create(new DeviceData(name.getFirstValue(), name.getFirstValue(), description.getFirstValue()));
                annotationProcessor.process(types, device);
                list.add(device);
                String[] path = new String[list.getPath().length + 1];
                System.arraycopy(list.getPath(), 0, path, 0, list.getPath().length);
                path[path.length - 1] = device.getId();
                persistence.saveValues(path, values);
            }
        };
    }

    @Override
    public void pluginAdded(Injector pluginInjector) {
        Set<Factory.Entry<com.intuso.housemate.api.object.device.DeviceFactory<? extends RealDevice>>> factoryEntries = Factory.getEntries(log, pluginInjector, com.intuso.housemate.api.object.device.DeviceFactory.class);
        for(Factory.Entry<com.intuso.housemate.api.object.device.DeviceFactory<? extends RealDevice>> factoryEntry : factoryEntries) {
            log.d("Adding new device factory for type " + factoryEntry.getTypeInfo().id());
            this.factoryEntries.put(factoryEntry.getTypeInfo().id(), factoryEntry);
            type.getOptions().add(new RealOption(log, listenersFactory, factoryEntry.getTypeInfo().id(),
                    factoryEntry.getTypeInfo().name(), factoryEntry.getTypeInfo().description()));
        }
    }

    @Override
    public void pluginRemoved(Injector pluginInjector) {
        for(com.intuso.housemate.api.object.device.DeviceFactory<? extends RealDevice> factory : pluginInjector.getInstance(new Key<Set<com.intuso.housemate.api.object.device.DeviceFactory<? extends RealDevice>>>() {})) {
            TypeInfo factoryInformation = factory.getClass().getAnnotation(TypeInfo.class);
            if(factoryInformation != null) {
                factoryEntries.remove(factoryInformation.id());
                type.getOptions().remove(factoryInformation.id());
            }
        }
    }

    private class DeviceFactoryType extends RealChoiceType<Factory.Entry<com.intuso.housemate.api.object.device.DeviceFactory<? extends RealDevice>>> {
        protected DeviceFactoryType(Log log) {
            super(log, listenersFactory, TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION, 1, 1, Arrays.<RealOption>asList());
        }

        @Override
        public TypeInstance serialise(Factory.Entry<com.intuso.housemate.api.object.device.DeviceFactory<? extends RealDevice>> entry) {
            return new TypeInstance(entry.getTypeInfo().id());
        }

        @Override
        public Factory.Entry<com.intuso.housemate.api.object.device.DeviceFactory<? extends RealDevice>> deserialise(TypeInstance value) {
            return value != null && value.getValue() != null ? factoryEntries.get(value.getValue()) : null;
        }
    }
}
