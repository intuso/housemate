package com.intuso.housemate.client.proxy.api.internal.object;

import com.intuso.housemate.client.api.internal.object.Feature;
import com.intuso.housemate.client.proxy.api.internal.ChildUtil;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Base interface for all proxy features
 * @param <FEATURE> the feature type
 */
public abstract class ProxyFeature<
        COMMANDS extends ProxyList<? extends ProxyCommand<?, ?, ?>, ?>,
        VALUES extends ProxyList<? extends ProxyValue<?, ?>, ?>,
        FEATURE extends ProxyFeature<COMMANDS, VALUES, FEATURE>>
        extends ProxyObject<Feature.Data, Feature.Listener<? super FEATURE>>
        implements Feature<COMMANDS, VALUES, FEATURE> {

    private final COMMANDS commands;
    private final VALUES values;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyFeature(Logger logger,
                        ListenersFactory listenersFactory,
                        ProxyObject.Factory<COMMANDS> commandsFactory,
                        ProxyObject.Factory<VALUES> valuesFactory) {
        super(logger, Feature.Data.class, listenersFactory);
        commands = commandsFactory.create(ChildUtil.logger(logger, Feature.COMMANDS_ID));
        values = valuesFactory.create(ChildUtil.logger(logger, Feature.VALUES_ID));
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        commands.init(ChildUtil.name(name, Feature.COMMANDS_ID), connection);
        values.init(ChildUtil.name(name, Feature.VALUES_ID), connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        commands.uninit();
        values.uninit();
    }

    @Override
    public final COMMANDS getCommands() {
        return commands;
    }

    @Override
    public final VALUES getValues() {
        return values;
    }
}
