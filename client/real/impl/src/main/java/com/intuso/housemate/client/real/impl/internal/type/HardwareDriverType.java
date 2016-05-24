package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.RealOptionImpl;
import com.intuso.housemate.client.real.impl.internal.ioc.Types;
import com.intuso.housemate.plugin.api.internal.driver.HardwareDriver;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 19/03/15.
 */
public class HardwareDriverType extends FactoryType<HardwareDriver.Factory<?>> {

    public final static String TYPE_ID = "hardware-factory";
    public final static String TYPE_NAME = "Hardware Factory";
    public final static String TYPE_DESCRIPTION = "Available types for new hardware";

    @Inject
    protected HardwareDriverType(@Types Logger logger, ListenersFactory listenersFactory, RealOptionImpl.Factory optionFactory) {
        super(ChildUtil.logger(logger, TYPE_ID), listenersFactory, TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION, optionFactory);
    }
}
