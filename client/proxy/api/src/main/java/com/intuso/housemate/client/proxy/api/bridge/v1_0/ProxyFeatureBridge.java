package com.intuso.housemate.client.proxy.api.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.FeatureMapper;
import com.intuso.housemate.client.api.internal.object.Feature;
import com.intuso.housemate.client.proxy.api.internal.ChildUtil;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Created by tomc on 28/11/16.
 */
public class ProxyFeatureBridge
        extends ProxyObjectBridge<com.intuso.housemate.client.v1_0.api.object.Feature.Data, Feature.Data, Feature.Listener<? super ProxyFeatureBridge>>
        implements Feature<ProxyListBridge<ProxyCommandBridge>, ProxyListBridge<ProxyValueBridge>, ProxyFeatureBridge> {

    private final ProxyListBridge<ProxyCommandBridge> commands;
    private final ProxyListBridge<ProxyValueBridge> values;

    @Inject
    protected ProxyFeatureBridge(@Assisted Logger logger,
                                 FeatureMapper featureMapper,
                                 Factory<ProxyListBridge<ProxyCommandBridge>> commandsFactory,
                                 Factory<ProxyListBridge<ProxyValueBridge>> valuesFactory,
                                 ListenersFactory listenersFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.Feature.Data.class, featureMapper, listenersFactory);
        commands = commandsFactory.create(ChildUtil.logger(logger, Feature.COMMANDS_ID));
        values = valuesFactory.create(ChildUtil.logger(logger, Feature.VALUES_ID));
    }

    @Override
    protected void initChildren(String versionName, String internalName, Connection connection) throws JMSException {
        super.initChildren(versionName, internalName, connection);
        commands.init(
                com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Feature.COMMANDS_ID),
                ChildUtil.name(internalName, Feature.COMMANDS_ID),
                connection);
        values.init(
                com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Feature.VALUES_ID),
                ChildUtil.name(internalName, Feature.VALUES_ID),
                connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        commands.uninit();
        values.uninit();
    }

    @Override
    public ProxyListBridge<ProxyCommandBridge> getCommands() {
        return commands;
    }

    @Override
    public ProxyListBridge<ProxyValueBridge> getValues() {
        return values;
    }
}
