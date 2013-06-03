package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.api.object.type.TypeValue;
import com.intuso.housemate.api.object.type.TypeValues;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 24/05/12
 * Time: 00:22
 * To change this template use File | Settings | File Templates.
 */
public class BrokerProxyProperty
        extends BrokerProxyValueBase<PropertyWrappable, CommandWrappable, BrokerProxyCommand, BrokerProxyProperty>
        implements Property<BrokerProxyType, BrokerProxyCommand, BrokerProxyProperty> {

    private BrokerProxyCommand setCommand;

    public BrokerProxyProperty(BrokerProxyResources<BrokerProxyFactory.Command> resources, PropertyWrappable wrappable) {
        super(resources, wrappable);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        setCommand = getWrapper(SET_COMMAND);
    }

    @Override
    public void set(final String value, CommandListener<? super BrokerProxyCommand> listener) {
        getSetCommand().perform(new TypeValues() {
            {
                put(VALUE_PARAM, new TypeValue(value));
            }
        }, listener);
    }

    @Override
    public BrokerProxyCommand getSetCommand() {
        return setCommand;
    }
}
