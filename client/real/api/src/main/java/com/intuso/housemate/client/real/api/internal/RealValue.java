package com.intuso.housemate.client.real.api.internal;

import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.housemate.object.api.internal.Value;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @param <O> the type of this value's value
 */
public interface RealValue<O>
        extends com.intuso.housemate.client.real.api.internal.RealValueBase<O, Value.Listener<? super RealValue<O>>, RealValue<O>>,
        Value<TypeInstances, RealValue<O>> {

    interface Factory {
        RealValue<?> create(@Assisted("id") String id,
                            @Assisted("name") String name,
                            @Assisted("description") String description,
                            RealType<?> type,
                            @Nullable List<?> values);
    }
}
