package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.type.serialiser.BooleanPrimitiveSerialiser;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.ioc.Type;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Type for a boolean
 */
public class BooleanPrimitiveType extends RealPrimitiveType<Boolean> {

    @Inject
    public BooleanPrimitiveType(@Type Logger logger,
                                ManagedCollectionFactory managedCollectionFactory) {
        super(ChildUtil.logger(logger, boolean.class.getName()),
                new PrimitiveData(boolean.class.getName(), "Boolean", "True or false"),
                BooleanPrimitiveSerialiser.INSTANCE,
                managedCollectionFactory);
    }
}