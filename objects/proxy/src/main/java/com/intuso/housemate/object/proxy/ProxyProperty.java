package com.intuso.housemate.object.proxy;

import com.google.inject.Injector;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.utilities.log.Log;

/**
 * @param <TYPE> the type of the type
 * @param <SET_COMMAND> the type of the set command
 * @param <PROPERTY> the type of the property
 */
public abstract class ProxyProperty<
            TYPE extends ProxyType<?, ?, ?, ?>,
            SET_COMMAND extends ProxyCommand<?, ?, SET_COMMAND>,
            PROPERTY extends ProxyProperty<TYPE, SET_COMMAND, PROPERTY>>
        extends ProxyValueBase<PropertyData, CommandData, SET_COMMAND, TYPE, PROPERTY>
        implements Property<TYPE, SET_COMMAND, PROPERTY> {

    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyProperty(Log log, Injector injector, PropertyData data) {
        super(log, injector, data);
    }

    @Override
    public void set(final TypeInstances value, CommandListener<? super SET_COMMAND> listener) {
        getSetCommand().perform(new TypeInstanceMap() {
            {
                put(VALUE_ID, value);
            }
        }, listener);
    }

    @Override
    public SET_COMMAND getSetCommand() {
        return (SET_COMMAND) getChild(SET_COMMAND_ID);
    }
}
