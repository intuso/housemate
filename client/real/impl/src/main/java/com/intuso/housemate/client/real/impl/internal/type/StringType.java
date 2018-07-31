package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.type.serialiser.StringSerialiser;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.ioc.Type;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Type for a string
 */
public class StringType extends RealPrimitiveType<String> {

    @Inject
    public StringType(@Type Logger logger,
                      ManagedCollectionFactory managedCollectionFactory) {
        super(ChildUtil.logger(logger, String.class.getName()),
                new PrimitiveData("string", "String", "Some text"),
                StringSerialiser.INSTANCE,
                managedCollectionFactory);
    }
}