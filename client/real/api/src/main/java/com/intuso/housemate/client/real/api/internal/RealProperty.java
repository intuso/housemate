package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.comms.api.internal.payload.CommandData;
import com.intuso.housemate.comms.api.internal.payload.PropertyData;
import com.intuso.housemate.object.api.internal.Command;
import com.intuso.housemate.object.api.internal.Property;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.List;

/**
 * @param <O> the type of the property's value
 */
public class RealProperty<O>
        extends RealValueBase<PropertyData, CommandData, RealCommand, O, Property.Listener<? super RealProperty<O>>, RealProperty<O>>
        implements Property<TypeInstances, RealCommand, RealProperty<O>> {

    private RealCommand setCommand;

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param id the property's id
     * @param name the property's name
     * @param description the property's description
     * @param type the property's type
     * @param values the property's initial value
     */
    public RealProperty(Log log, ListenersFactory listenersFactory, String id, String name, String description, RealType<?, ?, O> type,
                        O... values) {
        this(log, listenersFactory, id, name, description, type, Arrays.asList(values));
    }

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param id the property's id
     * @param name the property's name
     * @param description the property's description
     * @param type the property's type
     * @param values the property's initial value
     */
    public RealProperty(Log log, ListenersFactory listenersFactory, String id, String name, String description, RealType<?, ?, O> type,
                        List<O> values) {
        super(log, listenersFactory, new PropertyData(id, name, description, type.getId(),
                RealType.serialiseAll(type, values)), type);
        setCommand = new RealCommand(log, listenersFactory, PropertyData.SET_COMMAND_ID, PropertyData.SET_COMMAND_ID,
                "The function to change the property's value",
                new RealParameter<>(log, listenersFactory, PropertyData.VALUE_PARAM, PropertyData.VALUE_PARAM, "The new value for the property", type)) {
            @Override
            public void perform(TypeInstanceMap values) {
                List<O> typedValues = RealType.deserialiseAll(getType(), values.getChildren().get(PropertyData.VALUE_PARAM));
                RealProperty.this.setTypedValues(typedValues);
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
