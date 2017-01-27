package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.type.serialiser.DoubleSerialiser;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.ioc.Type;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Type for a double
 */
public class DoubleType extends RealPrimitiveType<Double> {

    @Inject
    public DoubleType(@Type Logger logger, ManagedCollectionFactory managedCollectionFactory) {
        super(ChildUtil.logger(logger, Double.class.getName()),
                new PrimitiveData("double", "Double", "A number with a decimal point"),
                DoubleSerialiser.INSTANCE,
                managedCollectionFactory);
    }
}
