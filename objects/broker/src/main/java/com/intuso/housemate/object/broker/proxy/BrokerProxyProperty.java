package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;

public class BrokerProxyProperty
        extends BrokerProxyValueBase<PropertyData, CommandData, BrokerProxyCommand, BrokerProxyProperty>
        implements Property<BrokerProxyType, BrokerProxyCommand, BrokerProxyProperty> {

    private BrokerProxyCommand setCommand;

    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public BrokerProxyProperty(BrokerProxyResources<BrokerProxyFactory.Command> resources, PropertyData data) {
        super(resources, data);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        setCommand = getChild(SET_COMMAND_ID);
    }

    @Override
    public void set(final TypeInstances value, CommandListener<? super BrokerProxyCommand> listener) {
        getSetCommand().perform(new TypeInstanceMap() {
            {
                put(VALUE_PARAM, value);
            }
        }, listener);
    }

    @Override
    public BrokerProxyCommand getSetCommand() {
        return setCommand;
    }
}
