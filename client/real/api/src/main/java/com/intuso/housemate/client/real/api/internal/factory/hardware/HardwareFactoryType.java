package com.intuso.housemate.client.real.api.internal.factory.hardware;

import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.factory.FactoryType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Created by tomc on 19/03/15.
 */
public class HardwareFactoryType extends FactoryType<RealHardwareFactory<?>> {

    public final static String TYPE_ID = "hardware-factory";
    public final static String TYPE_NAME = "Hardware Factory";
    public final static String TYPE_DESCRIPTION = "Available types for new hardware";

    @Inject
    protected HardwareFactoryType(Log log, ListenersFactory listenersFactory) {
        super(log, listenersFactory, TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION);
    }
}
