package com.intuso.housemate.client.real.impl.internal.factory.device;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.RealDevice;
import com.intuso.housemate.client.real.api.internal.RealProperty;
import com.intuso.housemate.client.real.impl.internal.RealCommandImpl;
import com.intuso.housemate.client.real.impl.internal.RealParameterImpl;
import com.intuso.housemate.client.real.impl.internal.type.StringType;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.comms.api.internal.payload.DeviceData;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
* Created by tomc on 19/03/15.
*/
public class AddDeviceCommand extends RealCommandImpl {

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
    private final Callback callback;
    private final RealDevice.Factory deviceFactory;
    private final RealDevice.RemoveCallback removeCallback;

    @Inject
    protected AddDeviceCommand(Log log,
                               ListenersFactory listenersFactory,
                               StringType stringType,
                               DeviceFactoryType deviceFactoryType,
                               RealDevice.Factory deviceFactory,
                               @Assisted("id") String id,
                               @Assisted("name") String name,
                               @Assisted("description") String description,
                               @Assisted Callback callback,
                               @Assisted RealDevice.RemoveCallback removeCallback) {
        super(log, listenersFactory, id, name, description,
                new RealParameterImpl<>(log, listenersFactory, NAME_PARAMETER_ID, NAME_PARAMETER_NAME, NAME_PARAMETER_DESCRIPTION, stringType),
                new RealParameterImpl<>(log, listenersFactory, DESCRIPTION_PARAMETER_ID, DESCRIPTION_PARAMETER_NAME, DESCRIPTION_PARAMETER_DESCRIPTION, stringType),
                new RealParameterImpl<>(log, listenersFactory, TYPE_PARAMETER_ID, TYPE_PARAMETER_NAME, TYPE_PARAMETER_DESCRIPTION, deviceFactoryType));
        this.deviceFactoryType = deviceFactoryType;
        this.callback = callback;
        this.deviceFactory = deviceFactory;
        this.removeCallback = removeCallback;
    }

    @Override
    public void perform(TypeInstanceMap values) {
        TypeInstances name = values.getChildren().get(NAME_PARAMETER_ID);
        if(name == null || name.getFirstValue() == null)
            throw new HousemateCommsException("No name specified");
        TypeInstances description = values.getChildren().get(DESCRIPTION_PARAMETER_ID);
        if(description == null || description.getFirstValue() == null)
            throw new HousemateCommsException("No description specified");
        RealDevice<?> device = deviceFactory.create(new DeviceData(name.getFirstValue(), name.getFirstValue(), description.getFirstValue()), removeCallback);
        callback.addDevice(device);
        TypeInstances deviceType = values.getChildren().get(TYPE_PARAMETER_ID);
        if(deviceType != null && deviceType.getFirstValue() != null)
            ((RealProperty)device.getDriverProperty()).setTypedValues(deviceFactoryType.deserialise(deviceType.getElements().get(0)));
    }

    public interface Callback {
        void addDevice(RealDevice device);
    }

    public interface Factory {
        AddDeviceCommand create(@Assisted("id") String id,
                                @Assisted("name") String name,
                                @Assisted("description") String description,
                                Callback callback,
                                RealDevice.RemoveCallback removeCallback);
    }
}
