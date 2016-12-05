package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.PropertyMapper;
import com.intuso.housemate.client.api.bridge.v1_0.TypeInstancesMapper;
import com.intuso.housemate.client.api.internal.object.Command;
import com.intuso.housemate.client.api.internal.object.Property;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Created by tomc on 28/11/16.
 */
public class RealPropertyBridge
        extends RealValueBaseBridge<com.intuso.housemate.client.v1_0.api.object.Property.Data, Property.Data, Property.Listener<? super RealPropertyBridge>, RealPropertyBridge>
        implements Property<Type.Instances, RealTypeBridge, RealCommandBridge, RealPropertyBridge> {

    private final RealCommandBridge setCommand;

    @Inject
    public RealPropertyBridge(@Assisted Logger logger,
                              PropertyMapper propertyMapper,
                              TypeInstancesMapper typeInstancesMapper,
                              RealObjectBridge.Factory<RealCommandBridge> commandFactory,
                              ListenersFactory listenersFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.Property.Data.class, propertyMapper, typeInstancesMapper, listenersFactory);
        setCommand = commandFactory.create(ChildUtil.logger(logger, Property.SET_COMMAND_ID));
    }

    @Override
    protected void initChildren(String versionName, String internalName, Connection connection) throws JMSException {
        super.initChildren(versionName, internalName, connection);
        setCommand.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Property.SET_COMMAND_ID),
                ChildUtil.name(internalName, Property.SET_COMMAND_ID),
                connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        setCommand.uninit();
    }

    @Override
    public RealCommandBridge getSetCommand() {
        return setCommand;
    }

    public interface Factory {
        RealPropertyBridge create(Logger logger);
    }

    @Override
    public void set(final Type.Instances value, Command.PerformListener<? super RealCommandBridge> listener) {
        Type.InstanceMap values = new Type.InstanceMap();
        values.getChildren().put(Property.VALUE_ID, value);
        getSetCommand().perform(values, listener);
    }
}
