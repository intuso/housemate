package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.TypeInstanceMapper;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.real.api.internal.RealType;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created by tomc on 06/11/15.
 */
public class RealTypeBridge<FROM, TO> implements RealType<TO, RealTypeBridge<FROM, TO>> {

    private final com.intuso.housemate.client.v1_0.real.api.RealType<FROM, ?> type;
    private final Function<? super FROM, ? extends TO> convertFrom;
    private final Function<? super TO, ? extends FROM> convertTo;
    private final TypeInstanceMapper typeInstanceMapper;

    @Inject
    public RealTypeBridge(@Assisted com.intuso.housemate.client.v1_0.real.api.RealType<?, ?> type,
                          @Assisted("convertFrom") Function<?, ?> convertFrom,
                          @Assisted("convertTo") Function<?, ?> convertTo,
                          TypeInstanceMapper typeInstanceMapper) {
        this.type = (com.intuso.housemate.client.v1_0.real.api.RealType<FROM, ?>) type;
        this.convertFrom = (Function<? super FROM, ? extends TO>) convertFrom;
        this.convertTo = (Function<? super TO, ? extends FROM>) convertTo;
        this.typeInstanceMapper = typeInstanceMapper;
    }

    public com.intuso.housemate.client.v1_0.real.api.RealType<FROM, ?> getType() {
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
    public ListenerRegistration addObjectListener(Listener<? super RealTypeBridge<FROM, TO>> listener) {
        return null; // todo
    }

    @Override
    public Type.Instance serialise(TO to) {
        return typeInstanceMapper.map(type.serialise(convertTo.apply(to)));
    }

    @Override
    public TO deserialise(Type.Instance instance) {
        return convertFrom.apply(type.deserialise(typeInstanceMapper.map(instance)));
    }

    public interface Factory {
        RealTypeBridge<?, ?> create(com.intuso.housemate.client.v1_0.real.api.RealType<?, ?> type, @Assisted("convertFrom") Function<?, ?> convertFrom, @Assisted("convertTo") Function<?, ?> convertTo);
    }
}
