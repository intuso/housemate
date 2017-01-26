package com.intuso.housemate.client.proxy.api.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.PropertyMapper;
import com.intuso.housemate.client.api.bridge.v1_0.object.TypeInstancesMapper;
import com.intuso.housemate.client.api.internal.object.Command;
import com.intuso.housemate.client.api.internal.object.Property;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.proxy.api.internal.ChildUtil;
import com.intuso.utilities.listener.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Created by tomc on 28/11/16.
 */
public class ProxyPropertyBridge
        extends ProxyValueBaseBridge<com.intuso.housemate.client.v1_0.api.object.Property.Data, Property.Data, Property.Listener<? super ProxyPropertyBridge>, ProxyPropertyBridge>
        implements Property<Type.Instances, ProxyTypeBridge, ProxyCommandBridge, ProxyPropertyBridge> {

    private final ProxyCommandBridge setCommand;

    @Inject
    public ProxyPropertyBridge(@Assisted Logger logger,
                               PropertyMapper propertyMapper,
                               TypeInstancesMapper typeInstancesMapper,
                               ProxyObjectBridge.Factory<ProxyCommandBridge> commandFactory,
                               ManagedCollectionFactory managedCollectionFactory) {
        super(logger, Property.Data.class, propertyMapper, typeInstancesMapper, managedCollectionFactory);
        setCommand = commandFactory.create(ChildUtil.logger(logger, Property.SET_COMMAND_ID));
    }

    @Override
    protected void initChildren(String versionName, String internalName, Connection connection) throws JMSException {
        super.initChildren(versionName, internalName, connection);
        setCommand.init(
                com.intuso.housemate.client.proxy.api.internal.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Property.SET_COMMAND_ID),
                ChildUtil.name(internalName, Property.SET_COMMAND_ID),
                connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        setCommand.uninit();
    }

    @Override
    public ProxyCommandBridge getSetCommand() {
        return setCommand;
    }

    public interface Factory {
        ProxyPropertyBridge create(Logger logger);
    }

    @Override
    public void set(final Type.Instances value, Command.PerformListener<? super ProxyCommandBridge> listener) {
        Type.InstanceMap values = new Type.InstanceMap();
        values.getChildren().put(Property.VALUE_ID, value);
        getSetCommand().perform(values, listener);
    }
}
