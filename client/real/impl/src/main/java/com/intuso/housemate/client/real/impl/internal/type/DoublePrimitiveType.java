package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.type.serialiser.DoublePrimitiveSerialiser;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.ioc.Type;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Type for a double
 */
public class DoublePrimitiveType extends RealPrimitiveType<Double> {

    @Inject
    public DoublePrimitiveType(@Type Logger logger,
                               ManagedCollectionFactory managedCollectionFactory,
                               Sender.Factory senderFactory) {
        super(ChildUtil.logger(logger, double.class.getName()),
                new PrimitiveData(double.class.getName(), "Double", "A number with a decimal point"),
                DoublePrimitiveSerialiser.INSTANCE,
                managedCollectionFactory,
                senderFactory);
    }
}
