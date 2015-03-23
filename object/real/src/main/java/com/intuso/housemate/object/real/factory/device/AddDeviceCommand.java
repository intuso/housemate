package com.intuso.housemate.object.real.factory.device;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.real.*;
import com.intuso.housemate.object.real.annotations.AnnotationProcessor;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
* Created by tomc on 19/03/15.
*/
public class AddDeviceCommand extends RealCommand {

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
                new RealParameter<>(log, listenersFactory, RealRoot.NAME_PARAMETER_ID, RealRoot.NAME_PARAMETER_NAME, RealRoot.NAME_PARAMETER_DESCRIPTION, stringType),
                new RealParameter<>(log, listenersFactory, RealRoot.DESCRIPTION_PARAMETER_ID, RealRoot.DESCRIPTION_PARAMETER_NAME, RealRoot.DESCRIPTION_PARAMETER_DESCRIPTION, stringType),
                new RealParameter<>(log, listenersFactory, RealRoot.TYPE_PARAMETER_ID, RealRoot.TYPE_PARAMETER_NAME, RealRoot.TYPE_PARAMETER_DESCRIPTION, deviceFactoryType));
        this.deviceFactoryType = deviceFactoryType;
        this.annotationProcessor = annotationProcessor;
        this.types = types;
        this.owner = owner;
    }

    @Override
    public void perform(TypeInstanceMap values) throws HousemateException {
        TypeInstances deviceType = values.getChildren().get(RealRoot.TYPE_PARAMETER_ID);
        if(deviceType == null || deviceType.getFirstValue() == null)
            throw new HousemateException("No type specified");
        RealDeviceFactory<?> deviceFactory = deviceFactoryType.deserialise(deviceType.getElements().get(0));
        if(deviceFactory == null)
            throw new HousemateException("No factory known for device type " + deviceType);
        TypeInstances name = values.getChildren().get(RealRoot.NAME_PARAMETER_ID);
        if(name == null || name.getFirstValue() == null)
            throw new HousemateException("No name specified");
        TypeInstances description = values.getChildren().get(RealRoot.DESCRIPTION_PARAMETER_ID);
        if(description == null || description.getFirstValue() == null)
            throw new HousemateException("No description specified");
        RealDevice device = deviceFactory.create(new DeviceData(name.getFirstValue(), name.getFirstValue(), description.getFirstValue()), owner);
        annotationProcessor.process(types, device);
        owner.addDevice(device);
    }

    public interface Factory {
        public AddDeviceCommand create(RealDeviceOwner owner);
    }
}
