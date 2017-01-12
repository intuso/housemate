package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.api.internal.type.serialiser.BooleanSerialiser;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.ioc.Type;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Type for a boolean
 */
public class BooleanType extends RealPrimitiveType<Boolean> {

    @Inject
    public BooleanType(@Type Logger logger, ListenersFactory listenersFactory) {
        super(ChildUtil.logger(logger, Boolean.class.getName()),
                new PrimitiveData(Boolean.class.getName(), "Boolean", "True or false"),
                new TypeSpec(Boolean.class),
                BooleanSerialiser.INSTANCE,
                listenersFactory);
    }
}