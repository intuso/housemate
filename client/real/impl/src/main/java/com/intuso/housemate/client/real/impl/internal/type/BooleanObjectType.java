package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.type.serialiser.BooleanObjectSerialiser;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.ioc.Type;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Type for a boolean
 */
public class BooleanObjectType extends RealPrimitiveType<Boolean> {

    @Inject
    public BooleanObjectType(@Type Logger logger,
                             ManagedCollectionFactory managedCollectionFactory) {
        super(ChildUtil.logger(logger, Boolean.class.getName()),
                new PrimitiveData(Boolean.class.getName(), "Boolean", "True or false"),
                BooleanObjectSerialiser.INSTANCE,
                managedCollectionFactory);
    }
}