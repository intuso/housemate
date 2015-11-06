package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.RealCommand;
import com.intuso.housemate.client.real.api.internal.RealProperty;
import com.intuso.housemate.client.real.api.internal.RealType;
import com.intuso.housemate.object.api.bridge.v1_0.TypeInstancesMapper;
import com.intuso.housemate.object.api.internal.Command;
import com.intuso.housemate.object.api.internal.Property;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.List;

/**
 * Created by tomc on 03/11/15.
 */
public class RealPropertyBridge<FROM, TO> implements RealProperty<TO> {

    private final com.intuso.housemate.client.v1_0.real.api.RealProperty<FROM> property;
    private final Function<? super FROM, ? extends TO> convertFrom;
    private final Function<? super TO, ? extends FROM> convertTo;
    private final CommandMapper commandMapper;
    private final TypeInstancesMapper typeInstancesMapper;

    @Inject
    public RealPropertyBridge(@Assisted com.intuso.housemate.client.v1_0.real.api.RealProperty<?> property,
                              @Assisted("convertFrom") Function<?, ?> convertFrom,
                              @Assisted("convertTo") Function<?, ?> convertTo,
                              CommandMapper commandMapper,
                              TypeInstancesMapper typeInstancesMapper) {
        this.property = (com.intuso.housemate.client.v1_0.real.api.RealProperty<FROM>) property;
        this.convertFrom = (Function<? super FROM, ? extends TO>) convertFrom;
        this.convertTo = (Function<? super TO, ? extends FROM>) convertTo;
        this.commandMapper = commandMapper;
        this.typeInstancesMapper = typeInstancesMapper;
    }

    public com.intuso.housemate.client.v1_0.real.api.RealProperty<FROM> getProperty() {
        return property;
    }

    @Override
    public void set(TypeInstances value, Command.PerformListener<? super RealCommand> listener) {

    }

    @Override
    public RealCommand getSetCommand() {
        return commandMapper.map(property.getSetCommand());
    }

    @Override
    public RealType<TO> getType() {
        return null; // todo
    }

    @Override
    public TO getTypedValue() {
        return convertFrom.apply(property.getTypedValue());
    }

    @Override
    public List<TO> getTypedValues() {
        return Lists.newArrayList(Lists.transform(property.getTypedValues(), convertFrom));
    }

    @Override
    public void setTypedValues(TO... typedValues) {
        setTypedValues(Lists.newArrayList(typedValues));
    }

    @Override
    public void setTypedValues(List<TO> typedValues) {
        property.setTypedValues(Lists.newArrayList(Lists.transform(typedValues, convertTo)));
    }

    @Override
    public String getTypeId() {
        return property.getTypeId();
    }

    @Override
    public TypeInstances getValue() {
        return typeInstancesMapper.map(property.getValue());
    }

    @Override
    public String getId() {
        return property.getId();
    }

    @Override
    public String getName() {
        return property.getName();
    }

    @Override
    public String getDescription() {
        return property.getDescription();
    }

    @Override
    public String[] getPath() {
        return property.getPath();
    }

    @Override
    public ListenerRegistration addObjectListener(Property.Listener<? super RealProperty<TO>> listener) {
        return null; // todo
    }

    public interface Factory {
        RealPropertyBridge<?, ?> create(com.intuso.housemate.client.v1_0.real.api.RealProperty<?> value, @Assisted("convertFrom") Function<?, ?> convertFrom, @Assisted("convertTo") Function<?, ?> convertTo);
    }
}
