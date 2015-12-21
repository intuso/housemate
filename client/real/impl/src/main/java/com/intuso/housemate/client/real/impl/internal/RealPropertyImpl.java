package com.intuso.housemate.client.real.impl.internal;

import com.intuso.housemate.client.real.api.internal.RealCommand;
import com.intuso.housemate.client.real.api.internal.RealProperty;
import com.intuso.housemate.client.real.api.internal.RealType;
import com.intuso.housemate.comms.api.internal.payload.CommandData;
import com.intuso.housemate.comms.api.internal.payload.PropertyData;
import com.intuso.housemate.object.api.internal.Command;
import com.intuso.housemate.object.api.internal.Property;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * @param <O> the type of the property's value
 */
public class RealPropertyImpl<O>
        extends RealValueBaseImpl<PropertyData, CommandData, RealCommandImpl, O, Property.Listener<? super RealProperty<O>>, RealProperty<O>>
        implements RealProperty<O> {

    private RealCommandImpl setCommand;

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param id the property's id
     * @param name the property's name
     * @param description the property's description
     * @param type the property's type
     * @param values the property's initial value
     */
    public RealPropertyImpl(Logger logger, ListenersFactory listenersFactory, String id, String name, String description, RealType<O> type,
                            O... values) {
        this(logger, listenersFactory, id, name, description, type, Arrays.asList(values));
    }

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param id the property's id
     * @param name the property's name
     * @param description the property's description
     * @param type the property's type
     * @param values the property's initial value
     */
    public RealPropertyImpl(Logger logger, ListenersFactory listenersFactory, String id, String name, String description, RealType<O> type,
                            List<O> values) {
        super(logger, listenersFactory, new PropertyData(id, name, description, type.getId(),
                RealTypeImpl.serialiseAll(type, values)), type);
        setCommand = new RealCommandImpl(LoggerUtil.child(logger, PropertyData.SET_COMMAND_ID), listenersFactory, PropertyData.SET_COMMAND_ID, PropertyData.SET_COMMAND_ID,
                "The function to change the property's value",
                new RealParameterImpl<>(listenersFactory, LoggerUtil.child(logger, PropertyData.SET_COMMAND_ID, PropertyData.VALUE_PARAM), PropertyData.VALUE_PARAM, PropertyData.VALUE_PARAM, "The new value for the property", type)) {
            @Override
            public void perform(TypeInstanceMap values) {
                List<O> typedValues = RealTypeImpl.deserialiseAll(getType(), values.getChildren().get(PropertyData.VALUE_PARAM));
                RealPropertyImpl.this.setTypedValues(typedValues);
            }
        };
        addChild(setCommand);
    }

    @Override
    public void set(final TypeInstances values, Command.PerformListener<? super RealCommand> listener) {
        getSetCommand().perform(new TypeInstanceMap() {
            {
                getChildren().put(PropertyData.VALUE_ID, values);
            }
        }, listener);
    }

    @Override
    public RealCommand getSetCommand() {
        return setCommand;
    }
}
