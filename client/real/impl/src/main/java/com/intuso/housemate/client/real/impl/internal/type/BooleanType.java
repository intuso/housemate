package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.type.serialiser.BooleanSerialiser;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.ioc.Type;
import com.intuso.utilities.listener.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Type for a boolean
 */
public class BooleanType extends RealPrimitiveType<Boolean> {

    @Inject
    public BooleanType(@Type Logger logger, ManagedCollectionFactory managedCollectionFactory) {
        super(ChildUtil.logger(logger, Boolean.class.getName()),
                new PrimitiveData("boolean", "Boolean", "True or false"),
                BooleanSerialiser.INSTANCE,
                managedCollectionFactory);
    }
}