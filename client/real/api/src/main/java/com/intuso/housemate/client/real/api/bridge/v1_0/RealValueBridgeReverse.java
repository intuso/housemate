package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.TypeInstancesMapper;
import com.intuso.housemate.client.v1_0.api.object.Value;
import com.intuso.housemate.client.v1_0.real.api.RealType;
import com.intuso.housemate.client.v1_0.real.api.RealValue;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.List;

/**
 * Created by tomc on 03/11/15.
 */
public class RealValueBridgeReverse<FROM, TO>
        implements RealValue<TO, RealType<TO, ?>, RealValueBridgeReverse<FROM, TO>> {

    private final com.intuso.housemate.client.real.api.internal.RealValue<FROM, ?, ?> value;
    private final Function<? super FROM, ? extends TO> convertFrom;
    private final Function<? super TO, ? extends FROM> convertTo;
    private final TypeInstancesMapper typeInstancesMapper;

    @Inject
    public RealValueBridgeReverse(@Assisted com.intuso.housemate.client.real.api.internal.RealValue<?, ?, ?> value,
                                  @Assisted("convertFrom") Function<?, ?> convertFrom,
                                  @Assisted("convertTo") Function<?, ?> convertTo,
                                  TypeInstancesMapper typeInstancesMapper) {
        this.value = (com.intuso.housemate.client.real.api.internal.RealValue<FROM, ?, ?>) value;
        this.convertFrom = (Function<? super FROM, ? extends TO>) convertFrom;
        this.convertTo = (Function<? super TO, ? extends FROM>) convertTo;
        this.typeInstancesMapper = typeInstancesMapper;
    }

    public com.intuso.housemate.client.real.api.internal.RealValue<FROM, ?, ?> getMappedValue() {
        return value;
    }

    @Override
    public RealType<TO, ?> getType() {
        return null;// todo
    }

    @Override
    public TO getValue() {
        return convertFrom.apply(value.getValue());
    }

    @Override
    public Iterable<TO> getValues() {
        Iterable<FROM> values = value.getValues();
        return values == null ? null : Iterables.transform(values, convertFrom);
    }

    @Override
    public void setValue(TO value) {
        this.value.setValue(convertTo.apply(value));
    }

    @Override
    public void setValues(List<TO> values) {
        value.setValues(values == null ? null : Lists.transform(values, convertTo));
    }

    @Override
    public String getId() {
        return value.getId();
    }

    @Override
    public String getName() {
        return value.getName();
    }

    @Override
    public String getDescription() {
        return value.getDescription();
    }

    @Override
    public ListenerRegistration addObjectListener(Value.Listener<? super RealValueBridgeReverse<FROM, TO>> listener) {
        return null; // todo
    }

    public interface Factory {
        RealValueBridgeReverse<?, ?> create(com.intuso.housemate.client.real.api.internal.RealValue<?, ?, ?> value, @Assisted("convertFrom") Function<?, ?> convertFrom, @Assisted("convertTo") Function<?, ?> convertTo);
    }
}
