package com.intuso.housemate.object.real.factory.device;

import com.google.inject.Inject;
import com.intuso.housemate.object.real.factory.FactoryType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Created by tomc on 19/03/15.
 */
public class DeviceFactoryType extends FactoryType<RealDeviceFactory<?>> {

    public final static String TYPE_ID = "device-factory";
    public final static String TYPE_NAME = "Device Factory";
    public final static String TYPE_DESCRIPTION = "Available types for new devices";

    @Inject
    protected DeviceFactoryType(Log log, ListenersFactory listenersFactory) {
        super(log, listenersFactory, TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION);
    }
}
