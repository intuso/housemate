package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.api.internal.payload.CommandData;
import com.intuso.housemate.comms.api.internal.payload.FeatureData;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.ValueData;
import com.intuso.housemate.object.api.internal.Feature;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.object.ObjectFactory;
import org.slf4j.Logger;

public class ServerProxyFeature
        extends ServerProxyObject<FeatureData, HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>, ServerProxyFeature, Feature.Listener<? super ServerProxyFeature>>
        implements Feature<
        ServerProxyList<CommandData, ServerProxyCommand>,
        ServerProxyList<ValueData, ServerProxyValue>,
        ServerProxyFeature> {

    private ServerProxyList<CommandData, ServerProxyCommand> commands;
    private ServerProxyList<ValueData, ServerProxyValue> values;

    /**
     * @param logger {@inheritDoc}
     * @param objectFactory {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyFeature(Logger logger, ListenersFactory listenersFactory, ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> objectFactory, @Assisted FeatureData data) {
        super(logger, listenersFactory, objectFactory, data);
    }

    @Override
    protected void getChildObjects() {
        commands = (ServerProxyList<CommandData, ServerProxyCommand>) getChild(FeatureData.COMMANDS_ID);
        values = (ServerProxyList<ValueData, ServerProxyValue>) getChild(FeatureData.VALUES_ID);
    }

    @Override
    public ServerProxyList<CommandData, ServerProxyCommand> getCommands() {
        return commands;
    }

    @Override
    public ServerProxyList<ValueData, ServerProxyValue> getValues() {
        return values;
    }

    @Override
    protected void copyValues(HousemateData<?> data) {}
}
