package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.api.internal.payload.CommandData;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.PropertyData;
import com.intuso.housemate.object.api.internal.Command;
import com.intuso.housemate.object.api.internal.Property;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.ObjectFactory;

public class ServerProxyProperty
        extends ServerProxyValueBase<PropertyData, CommandData, ServerProxyCommand, Property.Listener<? super ServerProxyProperty>, ServerProxyProperty>
        implements Property<TypeInstances, ServerProxyCommand, ServerProxyProperty> {

    private ServerProxyCommand setCommand;

    /**
     * @param log {@inheritDoc}
     * @param objectFactory {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyProperty(Log log, ListenersFactory listenersFactory, ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> objectFactory, @Assisted PropertyData data) {
        super(log, listenersFactory, objectFactory, data);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        setCommand = getChild(PropertyData.SET_COMMAND_ID);
    }

    @Override
    public void set(final TypeInstances value, Command.PerformListener<? super ServerProxyCommand> listener) {
        getSetCommand().perform(new TypeInstanceMap() {
            {
                getChildren().put(PropertyData.VALUE_PARAM, value);
            }
        }, listener);
    }

    @Override
    public ServerProxyCommand getSetCommand() {
        return setCommand;
    }
}
