package com.intuso.housemate.client.real.api.internal;

import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.object.api.internal.Parameter;
import org.slf4j.Logger;

/**
 * @param <O> the type of the parameter's value
 */
public interface RealParameter<O>
        extends Parameter<RealParameter<O>> {

    RealType<O> getType();

    interface Factory {
        RealParameter<?> create(Logger logger, @Assisted("id") String id, @Assisted("name") String name,
                                @Assisted("description") String description, RealType<?> type);
    }
}
