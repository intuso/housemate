package com.intuso.housemate.object.server.real;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.command.CommandPerformListener;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.real.RealType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.List;

/**
 * @param <O> the type of the property
 */
public class ServerRealProperty<O>
        extends ServerRealValueBase<PropertyData, CommandData, ServerRealCommand, O, ServerRealProperty<O>>
        implements Property<RealType<?, ?, O>, ServerRealCommand, ServerRealProperty<O>> {

    private ServerRealCommand setCommand;

    /**
     * @param log {@inheritDoc}
     * @param id the object's id
     * @param name the object's name
     * @param description the object's description
     * @param type the type of the property
     * @param values the initial values of the property
     */
    public ServerRealProperty(Log log, ListenersFactory listenersFactory, String id, String name, String description,
                              RealType<?, ?, O> type, O... values) {
        this(log, listenersFactory, id, name, description, type, Arrays.asList(values));
    }

    /**
     * @param log {@inheritDoc}
     * @param id the object's id
     * @param name the object's name
     * @param description the object's description
     * @param type the type of the property
     * @param values the initial values of the property
     */
    public ServerRealProperty(Log log, ListenersFactory listenersFactory, String id, String name, String description,
                              RealType<?, ?, O> type, List<O> values) {
        super(log, listenersFactory, new PropertyData(id, name, description, type.getId(), null), type, values);
        setCommand = new ServerRealCommand(log, listenersFactory, SET_COMMAND_ID, SET_COMMAND_ID, "The command to change the property's values",
                Arrays.<ServerRealParameter<?>>asList(new ServerRealParameter<O>(log, listenersFactory, VALUE_PARAM, VALUE_PARAM, "The new values for the property", type))) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                List<O> objects = RealType.deserialiseAll(getType(), values.getChildren().get(VALUE_PARAM));
                ServerRealProperty.this.setTypedValues(objects);
            }
        };
        addChild(setCommand);
    }

    @Override
    public void set(final TypeInstances value, CommandPerformListener<? super ServerRealCommand> listener) {
        getSetCommand().perform(new TypeInstanceMap() {
            {
                getChildren().put(VALUE_ID, value);
            }
        }, listener);
    }

    @Override
    public ServerRealCommand getSetCommand() {
        return setCommand;
    }
}
