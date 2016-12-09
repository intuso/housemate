package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.TypeInstanceMapper;
import com.intuso.housemate.client.v1_0.real.api.object.RealType;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created by tomc on 06/11/15.
 */
public class RealTypeBridgeReverse<FROM, TO> implements RealType<TO, RealTypeBridgeReverse<FROM, TO>> {

    private final com.intuso.housemate.client.real.api.internal.object.RealType<FROM, ?> type;
    private final Function<? super FROM, ? extends TO> convertFrom;
    private final Function<? super TO, ? extends FROM> convertTo;
    private final TypeInstanceMapper typeInstanceMapper;

    @Inject
    public RealTypeBridgeReverse(@Assisted com.intuso.housemate.client.real.api.internal.object.RealType<?, ?> type,
                                 @Assisted("convertFrom") Function<?, ?> convertFrom,
                                 @Assisted("convertTo") Function<?, ?> convertTo,
                                 TypeInstanceMapper typeInstanceMapper) {
        this.type = (com.intuso.housemate.client.real.api.internal.object.RealType<FROM, ?>) type;
        this.convertFrom = (Function<? super FROM, ? extends TO>) convertFrom;
        this.convertTo = (Function<? super TO, ? extends FROM>) convertTo;
        this.typeInstanceMapper = typeInstanceMapper;
    }

    public com.intuso.housemate.client.real.api.internal.object.RealType<FROM, ?> getType() {
        return type;
    }

    @Override
    public String getId() {
        return type.getId();
    }

    @Override
    public String getName() {
        return type.getName();
    }

    @Override
    public String getDescription() {
        return type.getDescription();
    }

    @Override
    public ListenerRegistration addObjectListener(Listener<? super RealTypeBridgeReverse<FROM, TO>> listener) {
        return null; // todo
    }

    @Override
    public Instance serialise(TO to) {
        return typeInstanceMapper.map(type.serialise(convertTo.apply(to)));
    }

    @Override
    public TO deserialise(Instance instance) {
        return convertFrom.apply(type.deserialise(typeInstanceMapper.map(instance)));
    }

    public interface Factory {
        RealTypeBridgeReverse<?, ?> create(com.intuso.housemate.client.real.api.internal.object.RealType<?, ?> type, @Assisted("convertFrom") Function<?, ?> convertFrom, @Assisted("convertTo") Function<?, ?> convertTo);
    }
}
