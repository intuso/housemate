package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.type.serialiser.IntegerPrimitiveSerialiser;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.ioc.Type;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Type for an integer
 */
public class IntegerPrimitiveType extends RealPrimitiveType<Integer> {

    @Inject
    public IntegerPrimitiveType(@Type Logger logger,
                                ManagedCollectionFactory managedCollectionFactory,
                                Sender.Factory senderFactory) {
        super(ChildUtil.logger(logger, int.class.getName()),
                new PrimitiveData(int.class.getName(), "Integer", "A whole number"),
                IntegerPrimitiveSerialiser.INSTANCE,
                managedCollectionFactory,
                senderFactory);
    }
}
