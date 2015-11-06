package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.RealType;
import com.intuso.housemate.client.v1_0.real.api.RealValue;
import com.intuso.housemate.object.api.bridge.v1_0.TypeInstancesMapper;
import com.intuso.housemate.object.v1_0.api.TypeInstances;
import com.intuso.housemate.object.v1_0.api.Value;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.List;

/**
 * Created by tomc on 03/11/15.
 */
public class RealValueBridgeReverse<FROM, TO> implements RealValue<TO> {

    private final com.intuso.housemate.client.real.api.internal.RealValue<FROM> value;
    private final Function<? super FROM, ? extends TO> convertFrom;
    private final Function<? super TO, ? extends FROM> convertTo;
    private final TypeInstancesMapper typeInstancesMapper;

    @Inject
    public RealValueBridgeReverse(@Assisted com.intuso.housemate.client.real.api.internal.RealValue<?> value,
                                  @Assisted("convertFrom") Function<?, ?> convertFrom,
                                  @Assisted("convertTo") Function<?, ?> convertTo,
                                  TypeInstancesMapper typeInstancesMapper) {
        this.value = (com.intuso.housemate.client.real.api.internal.RealValue<FROM>) value;
        this.convertFrom = (Function<? super FROM, ? extends TO>) convertFrom;
        this.convertTo = (Function<? super TO, ? extends FROM>) convertTo;
        this.typeInstancesMapper = typeInstancesMapper;
    }

    public com.intuso.housemate.client.real.api.internal.RealValue<FROM> getMappedValue() {
        return value;
    }

    @Override
    public RealType<TO> getType() {
        return null;// todo
    }

    @Override
    public TO getTypedValue() {
        return convertFrom.apply(value.getTypedValue());
    }

    @Override
    public List<TO> getTypedValues() {
        return Lists.newArrayList(Lists.transform(value.getTypedValues(), convertFrom));
    }

    @Override
    public void setTypedValues(TO... typedValues) {
        setTypedValues(Lists.newArrayList(typedValues));
    }

    @Override
    public void setTypedValues(List<TO> typedValues) {
        value.setTypedValues(Lists.newArrayList(Lists.transform(typedValues, convertTo)));
    }

    @Override
    public String getTypeId() {
        return value.getTypeId();
    }

    @Override
    public TypeInstances getValue() {
        return typeInstancesMapper.map(value.getValue());
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
    public String[] getPath() {
        return value.getPath();
    }

    @Override
    public ListenerRegistration addObjectListener(Value.Listener<? super RealValue<TO>> listener) {
        return null; // todo
    }

    public interface Factory {
        RealValueBridgeReverse<?, ?> create(com.intuso.housemate.client.real.api.internal.RealValue<?> value, @Assisted("convertFrom") Function<?, ?> convertFrom, @Assisted("convertTo") Function<?, ?> convertTo);
    }
}
