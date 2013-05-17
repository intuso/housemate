package com.intuso.housemate.broker.object.proxy;

import com.intuso.housemate.core.object.command.CommandListener;
import com.intuso.housemate.core.object.command.CommandWrappable;
import com.intuso.housemate.core.object.property.Property;
import com.intuso.housemate.core.object.property.PropertyWrappable;

import java.util.HashMap;

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
        getSetCommand().perform(new HashMap<String, String>() {
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
