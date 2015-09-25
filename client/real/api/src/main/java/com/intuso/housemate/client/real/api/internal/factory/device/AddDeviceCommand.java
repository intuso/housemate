package com.intuso.housemate.client.real.api.internal.factory.device;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.*;
import com.intuso.housemate.client.real.api.internal.annotations.AnnotationProcessor;
import com.intuso.housemate.client.real.api.internal.impl.type.StringType;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.comms.api.internal.payload.DeviceData;
import com.intuso.housemate.comms.api.internal.payload.TypeData;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
* Created by tomc on 19/03/15.
*/
public class AddDeviceCommand extends RealCommand {

    public final static String NAME_PARAMETER_ID = "name";
    public final static String NAME_PARAMETER_NAME = "Name";
    public final static String NAME_PARAMETER_DESCRIPTION = "The name of the new device";
    public final static String DESCRIPTION_PARAMETER_ID = "description";
    public final static String DESCRIPTION_PARAMETER_NAME = "Description";
    public final static String DESCRIPTION_PARAMETER_DESCRIPTION = "A description of the new device";
    public final static String TYPE_PARAMETER_ID = "type";
    public final static String TYPE_PARAMETER_NAME = "Type";
    public final static String TYPE_PARAMETER_DESCRIPTION = "The type of the new device";
    
    private final DeviceFactoryType deviceFactoryType;
    private final AnnotationProcessor annotationProcessor;
    private final RealList<TypeData<?>, RealType<?, ?, ?>> types;
    private final RealDeviceOwner owner;

    @Inject
    protected AddDeviceCommand(Log log, ListenersFactory listenersFactory, StringType stringType,
                               DeviceFactoryType deviceFactoryType, AnnotationProcessor annotationProcessor,
                               RealList<TypeData<?>, RealType<?, ?, ?>> types,
                               @Assisted RealDeviceOwner owner) {
        super(log, listenersFactory,
                owner.getAddDeviceCommandDetails().getId(), owner.getAddDeviceCommandDetails().getName(), owner.getAddDeviceCommandDetails().getDescription(),
                new RealParameter<>(log, listenersFactory, NAME_PARAMETER_ID, NAME_PARAMETER_NAME, NAME_PARAMETER_DESCRIPTION, stringType),
                new RealParameter<>(log, listenersFactory, DESCRIPTION_PARAMETER_ID, DESCRIPTION_PARAMETER_NAME, DESCRIPTION_PARAMETER_DESCRIPTION, stringType),
                new RealParameter<>(log, listenersFactory, TYPE_PARAMETER_ID, TYPE_PARAMETER_NAME, TYPE_PARAMETER_DESCRIPTION, deviceFactoryType));
        this.deviceFactoryType = deviceFactoryType;
        this.annotationProcessor = annotationProcessor;
        this.types = types;
        this.owner = owner;
    }

    @Override
    public void perform(TypeInstanceMap values) {
        TypeInstances deviceType = values.getChildren().get(TYPE_PARAMETER_ID);
        if(deviceType == null || deviceType.getFirstValue() == null)
            throw new HousemateCommsException("No type specified");
        RealDeviceFactory<?> deviceFactory = deviceFactoryType.deserialise(deviceType.getElements().get(0));
        if(deviceFactory == null)
            throw new HousemateCommsException("No factory known for device type " + deviceType);
        TypeInstances name = values.getChildren().get(NAME_PARAMETER_ID);
        if(name == null || name.getFirstValue() == null)
            throw new HousemateCommsException("No name specified");
        TypeInstances description = values.getChildren().get(DESCRIPTION_PARAMETER_ID);
        if(description == null || description.getFirstValue() == null)
            throw new HousemateCommsException("No description specified");
        RealDevice device = deviceFactory.create(new DeviceData(name.getFirstValue(), name.getFirstValue(), description.getFirstValue()), owner);
        annotationProcessor.process(types, device);
        owner.addDevice(device);
    }

    public interface Factory {
        public AddDeviceCommand create(RealDeviceOwner owner);
    }
}
