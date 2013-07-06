package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;

/**
 * @param <RESOURCES> the type of the resources
 * @param <CHILD_RESOURCES> the type of the chil resources
 * @param <TYPE> the type of the type
 * @param <SET_COMMAND> the type of the set command
 * @param <PROPERTY> the type of the property
 */
public abstract class ProxyProperty<
            RESOURCES extends ProxyResources<? extends HousemateObjectFactory<CHILD_RESOURCES, CommandData, SET_COMMAND>>,
            CHILD_RESOURCES extends ProxyResources<?>,
            TYPE extends ProxyType<?, ?, ?, ?, ?, ?>,
            SET_COMMAND extends ProxyCommand<?, ?, ?, ?, SET_COMMAND>,
            PROPERTY extends ProxyProperty<RESOURCES, CHILD_RESOURCES, TYPE, SET_COMMAND, PROPERTY>>
        extends ProxyValueBase<RESOURCES, CHILD_RESOURCES, PropertyData, CommandData, SET_COMMAND, TYPE, PROPERTY>
        implements Property<TYPE, SET_COMMAND, PROPERTY> {

    private SET_COMMAND setCommand;

    /**
     * @param resources {@inheritDoc}
     * @param childResources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyProperty(RESOURCES resources, CHILD_RESOURCES childResources, PropertyData data) {
        super(resources, childResources, data);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        setCommand = (SET_COMMAND) getWrapper(SET_COMMAND_ID);
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
        return setCommand;
    }
}
