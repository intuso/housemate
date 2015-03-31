package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.command.CommandPerformListener;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

public class ServerProxyProperty
        extends ServerProxyValueBase<PropertyData, CommandData, ServerProxyCommand, ServerProxyProperty>
        implements Property<ServerProxyType, ServerProxyCommand, ServerProxyProperty> {

    private ServerProxyCommand setCommand;

    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyProperty(Log log, ListenersFactory listenersFactory, Injector injector, @Assisted PropertyData data) {
        super(log, listenersFactory, injector, data);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        setCommand = getChild(SET_COMMAND_ID);
    }

    @Override
    public void set(final TypeInstances value, CommandPerformListener<? super ServerProxyCommand> listener) {
        getSetCommand().perform(new TypeInstanceMap() {
            {
                getChildren().put(VALUE_PARAM, value);
            }
        }, listener);
    }

    @Override
    public ServerProxyCommand getSetCommand() {
        return setCommand;
    }
}
