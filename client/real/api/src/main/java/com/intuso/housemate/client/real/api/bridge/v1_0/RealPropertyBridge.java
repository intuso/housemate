package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.TypeInstancesMapper;
import com.intuso.housemate.client.api.internal.object.Command;
import com.intuso.housemate.client.api.internal.object.Property;
import com.intuso.housemate.client.real.api.internal.RealCommand;
import com.intuso.housemate.client.real.api.internal.RealProperty;
import com.intuso.housemate.client.real.api.internal.RealType;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.List;

/**
 * Created by tomc on 03/11/15.
 */
public class RealPropertyBridge<FROM, TO>
        implements RealProperty<TO, RealType<TO, ?>, RealCommand<?, ?, ?>, RealPropertyBridge<FROM, TO>> {

    private final com.intuso.housemate.client.v1_0.real.api.RealProperty<FROM, ?, ?, ?> property;
    private final Function<? super FROM, ? extends TO> convertFrom;
    private final Function<? super TO, ? extends FROM> convertTo;
    private final CommandMapper commandMapper;
    private final TypeInstancesMapper typeInstancesMapper;

    @Inject
    public RealPropertyBridge(@Assisted com.intuso.housemate.client.v1_0.real.api.RealProperty<?, ?, ?, ?> property,
                              @Assisted("convertFrom") Function<?, ?> convertFrom,
                              @Assisted("convertTo") Function<?, ?> convertTo,
                              CommandMapper commandMapper,
                              TypeInstancesMapper typeInstancesMapper) {
        this.property = (com.intuso.housemate.client.v1_0.real.api.RealProperty<FROM, ?, ?, ?>) property;
        this.convertFrom = (Function<? super FROM, ? extends TO>) convertFrom;
        this.convertTo = (Function<? super TO, ? extends FROM>) convertTo;
        this.commandMapper = commandMapper;
        this.typeInstancesMapper = typeInstancesMapper;
    }

    public com.intuso.housemate.client.v1_0.real.api.RealProperty<FROM, ?, ?, ?> getProperty() {
        return property;
    }

    @Override
    public void set(final TO value, Command.PerformListener<? super RealCommand<?, ?, ?>> listener) {
        property.set(convertTo.apply(value), null /* todo use a PropertySetListener when it's made, see RealProperty */);
    }

    @Override
    public RealCommand<?, ?, ?> getSetCommand() {
        return commandMapper.map(property.getSetCommand());
    }

    @Override
    public RealType<TO, ?> getType() {
        return null; // todo
    }

    @Override
    public TO getValue() {
        return convertFrom.apply(property.getValue());
    }

    @Override
    public Iterable<TO> getValues() {
        Iterable<FROM> values = property.getValues();
        return values == null ? null : Iterables.transform(values, convertFrom);
    }

    @Override
    public void setValue(TO value) {
        property.setValue(convertTo.apply(value));
    }

    @Override
    public void setValues(List<TO> values) {
        property.setValues(values == null ? null : Lists.transform(values, convertTo));
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
    public ListenerRegistration addObjectListener(Property.Listener<? super RealPropertyBridge<FROM, TO>> listener) {
        return null; // todo
    }

    public interface Factory {
        RealPropertyBridge<?, ?> create(com.intuso.housemate.client.v1_0.real.api.RealProperty<?, ?, ?, ?> value, @Assisted("convertFrom") Function<?, ?> convertFrom, @Assisted("convertTo") Function<?, ?> convertTo);
    }
}
