package com.intuso.housemate.client.real.impl.internal;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.SubType;
import com.intuso.housemate.client.real.api.internal.RealSubType;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * @param <O> the type of the sub type's value
 */
public final class RealSubTypeImpl<O>
        extends RealObject<SubType.Data, SubType.Listener<? super RealSubTypeImpl<O>>>
        implements RealSubType<O, RealTypeImpl<O>, RealSubTypeImpl<O>> {

    private final RealTypeImpl<O> type;

    /**
     * @param logger {@inheritDoc}
     * @param managedCollectionFactory
     * @param type
     */
    @Inject
    public RealSubTypeImpl(@Assisted Logger logger,
                           @Assisted("id") String id,
                           @Assisted("name") String name,
                           @Assisted("description") String description,
                           @Assisted RealTypeImpl type,
                           @Assisted("min") int minValues,
                           @Assisted("max") int maxValues,
                           ManagedCollectionFactory managedCollectionFactory) {
        super(logger, new SubType.Data(id, name, description, type.getId(), minValues, maxValues), managedCollectionFactory);
        this.type = type;
    }

    @Override
    public final RealTypeImpl<O> getType() {
        return type;
    }

    public interface Factory {
        RealSubTypeImpl<?> create(Logger logger,
                                  @Assisted("id") String id,
                                  @Assisted("name") String name,
                                  @Assisted("description") String description,
                                  RealTypeImpl type,
                                  @Assisted("min") int minValues,
                                  @Assisted("max") int maxValues);
    }
}
