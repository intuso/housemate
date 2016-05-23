package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.real.impl.internal.RealOptionImpl;
import com.intuso.housemate.client.real.impl.internal.ioc.Types;
import com.intuso.housemate.plugin.api.internal.driver.DeviceDriver;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 19/03/15.
 */
public class DeviceDriverType extends FactoryType<DeviceDriver.Factory<?>> {

    public final static String TYPE_ID = "device-factory";
    public final static String TYPE_NAME = "Device Factory";
    public final static String TYPE_DESCRIPTION = "Available types for new device";

    @Inject
    protected DeviceDriverType(@Types Logger logger, ListenersFactory listenersFactory, RealOptionImpl.Factory optionFactory) {
        super(logger, listenersFactory, TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION, optionFactory);
    }
}
