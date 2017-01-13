package com.intuso.housemate.client.real.impl.internal.factory;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.real.api.internal.RealDevice;
import com.intuso.housemate.client.real.api.internal.RealProperty;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.RealCommandImpl;
import com.intuso.housemate.client.real.impl.internal.RealDeviceImpl;
import com.intuso.housemate.client.real.impl.internal.RealParameterImpl;
import com.intuso.housemate.client.real.impl.internal.factory.device.DeviceFactoryType;
import com.intuso.housemate.client.real.impl.internal.type.StringType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
* Created by tomc on 19/03/15.
*/
public class AddDeviceCommand {
    
    public final static String NAME_PARAMETER_ID = "name";
    public final static String NAME_PARAMETER_NAME = "Name";
    public final static String NAME_PARAMETER_DESCRIPTION = "The name of the new device";
    public final static String DESCRIPTION_PARAMETER_ID = "description";
    public final static String DESCRIPTION_PARAMETER_NAME = "Description";
    public final static String DESCRIPTION_PARAMETER_DESCRIPTION = "A description of the new device";
    public final static String TYPE_PARAMETER_ID = "type";
    public final static String TYPE_PARAMETER_NAME = "Type";
    public final static String TYPE_PARAMETER_DESCRIPTION = "The type of the new device";

    public interface Callback {
        void addDevice(RealDeviceImpl device);
    }

    public static class Factory {

        private final ListenersFactory listenersFactory;
        private final StringType stringType;
        private final DeviceFactoryType deviceFactoryType;
        private final RealCommandImpl.Factory commandFactory;
        private final RealDeviceImpl.Factory deviceFactory;

        @Inject
        public Factory(ListenersFactory listenersFactory, StringType stringType, DeviceFactoryType deviceFactoryType, RealCommandImpl.Factory commandFactory, RealDeviceImpl.Factory DeviceFactory) {
            this.listenersFactory = listenersFactory;
            this.stringType = stringType;
            this.deviceFactoryType = deviceFactoryType;
            this.commandFactory = commandFactory;
            this.deviceFactory = DeviceFactory;
        }

        public RealCommandImpl create(Logger baseLogger,
                                      Logger logger,
                                      String id,
                                      String name,
                                      String description,
                                      Callback callback,
                                      RealDevice.RemoveCallback<RealDeviceImpl> removeCallback) {
            return commandFactory.create(logger, id, name, description, new Performer(baseLogger, deviceFactoryType, deviceFactory, callback, removeCallback),
                    Lists.newArrayList(new RealParameterImpl<>(ChildUtil.logger(logger, NAME_PARAMETER_ID),
                                    NAME_PARAMETER_ID,
                                    NAME_PARAMETER_NAME,
                                    NAME_PARAMETER_DESCRIPTION,
                                    listenersFactory,
                                    stringType),
                            new RealParameterImpl<>(ChildUtil.logger(logger, DESCRIPTION_PARAMETER_ID),
                                    DESCRIPTION_PARAMETER_ID,
                                    DESCRIPTION_PARAMETER_NAME,
                                    DESCRIPTION_PARAMETER_DESCRIPTION,
                                    listenersFactory,
                                    stringType),
                            new RealParameterImpl<>(ChildUtil.logger(logger, TYPE_PARAMETER_ID),
                                    TYPE_PARAMETER_ID,
                                    TYPE_PARAMETER_NAME,
                                    TYPE_PARAMETER_DESCRIPTION,
                                    listenersFactory,
                                    deviceFactoryType)));
        }
    }

    private static class Performer implements RealCommandImpl.Performer {

        private final Logger logger;
        private final DeviceFactoryType deviceFactoryType;
        private final RealDeviceImpl.Factory deviceFactory;
        private final Callback callback;
        private final RealDevice.RemoveCallback<RealDeviceImpl> removeCallback;

        private Performer(Logger logger, DeviceFactoryType deviceFactoryType, RealDeviceImpl.Factory deviceFactory, Callback callback, RealDevice.RemoveCallback<RealDeviceImpl> removeCallback) {
            this.logger = logger;
            this.deviceFactoryType = deviceFactoryType;
            this.callback = callback;
            this.deviceFactory = deviceFactory;
            this.removeCallback = removeCallback;
        }

        @Override
        public void perform(Type.InstanceMap values) {
            Type.Instances name = values.getChildren().get(NAME_PARAMETER_ID);
            if(name == null || name.getFirstValue() == null)
                throw new HousemateException("No name specified");
            Type.Instances description = values.getChildren().get(DESCRIPTION_PARAMETER_ID);
            if(description == null || description.getFirstValue() == null)
                throw new HousemateException("No description specified");
            RealDeviceImpl device = deviceFactory.create(ChildUtil.logger(logger, name.getFirstValue()), name.getFirstValue(), name.getFirstValue(), description.getFirstValue(), removeCallback);
            callback.addDevice(device);
            Type.Instances deviceType = values.getChildren().get(TYPE_PARAMETER_ID);
            if(deviceType != null && deviceType.getFirstValue() != null)
                ((RealProperty)device.getDriverProperty()).setValue(deviceFactoryType.deserialise(deviceType.getElements().get(0)));
        }
    }
}
