package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.type.serialiser.IntegerObjectSerialiser;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.ioc.Type;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Type for an integer
 */
public class IntegerObjectType extends RealPrimitiveType<Integer> {

    @Inject
    public IntegerObjectType(@Type Logger logger,
                             ManagedCollectionFactory managedCollectionFactory) {
        super(ChildUtil.logger(logger, Integer.class.getName()),
                new PrimitiveData(Integer.class.getName(), "Integer", "A whole number"),
                IntegerObjectSerialiser.INSTANCE,
                managedCollectionFactory);
    }
}
