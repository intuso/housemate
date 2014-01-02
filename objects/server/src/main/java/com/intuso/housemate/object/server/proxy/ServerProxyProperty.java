package com.intuso.housemate.object.server.proxy;

import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;

public class ServerProxyProperty
        extends ServerProxyValueBase<PropertyData, CommandData, ServerProxyCommand, ServerProxyProperty>
        implements Property<ServerProxyType, ServerProxyCommand, ServerProxyProperty> {

    private ServerProxyCommand setCommand;

    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ServerProxyProperty(ServerProxyResources<ServerProxyFactory.Command> resources, PropertyData data) {
        super(resources, data);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        setCommand = getChild(SET_COMMAND_ID);
    }

    @Override
    public void set(final TypeInstances value, CommandListener<? super ServerProxyCommand> listener) {
        getSetCommand().perform(new TypeInstanceMap() {
            {
                put(VALUE_PARAM, value);
            }
        }, listener);
    }

    @Override
    public ServerProxyCommand getSetCommand() {
        return setCommand;
    }
}
