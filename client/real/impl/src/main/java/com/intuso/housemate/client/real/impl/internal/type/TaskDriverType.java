package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.RealListGeneratedImpl;
import com.intuso.housemate.client.real.impl.internal.RealOptionImpl;
import com.intuso.housemate.client.real.impl.internal.ioc.Types;
import com.intuso.housemate.plugin.api.internal.driver.TaskDriver;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 19/03/15.
 */
public class TaskDriverType extends FactoryType<TaskDriver.Factory<?>> {

    public final static String TYPE_ID = "task-factory";
    public final static String TYPE_NAME = "Task Factory";
    public final static String TYPE_DESCRIPTION = "Available types for new task";

    @Inject
    protected TaskDriverType(@Types Logger logger, ListenersFactory listenersFactory, RealOptionImpl.Factory optionFactory, RealListGeneratedImpl.Factory<RealOptionImpl> optionsFactory) {
        super(ChildUtil.logger(logger, TYPE_ID), TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION, listenersFactory, optionFactory, optionsFactory);
    }
}
