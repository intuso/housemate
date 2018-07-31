package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.PropertyMapper;
import com.intuso.housemate.client.api.bridge.v1_0.object.TypeInstancesMapper;
import com.intuso.housemate.client.api.internal.object.Command;
import com.intuso.housemate.client.api.internal.object.Property;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.object.view.PropertyView;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class RealPropertyBridge
        extends RealValueBaseBridge<com.intuso.housemate.client.v1_0.api.object.Property.Data, Property.Data, Property.Listener<? super RealPropertyBridge>, PropertyView, RealPropertyBridge>
        implements Property<Type.Instances, RealTypeBridge, RealCommandBridge, RealPropertyBridge> {

    private final RealCommandBridge setCommand;

    @Inject
    public RealPropertyBridge(@Assisted Logger logger,
                              PropertyMapper propertyMapper,
                              TypeInstancesMapper typeInstancesMapper,
                              ManagedCollectionFactory managedCollectionFactory,
                              RealObjectBridge.Factory<RealCommandBridge> commandFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.Property.Data.class, propertyMapper, typeInstancesMapper, managedCollectionFactory);
        setCommand = commandFactory.create(ChildUtil.logger(logger, Property.SET_COMMAND_ID));
    }

    @Override
    protected void initChildren(String versionName, String internalName, Sender.Factory internalSenderFactory, com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory, com.intuso.housemate.client.v1_0.messaging.api.Sender.Factory v1_0SenderFactory, Receiver.Factory v1_0ReceiverFactory) {
        super.initChildren(versionName, internalName, internalSenderFactory, internalReceiverFactory, v1_0SenderFactory, v1_0ReceiverFactory);
        setCommand.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Property.SET_COMMAND_ID),
                ChildUtil.name(internalName, Property.SET_COMMAND_ID),
                internalSenderFactory, internalReceiverFactory, v1_0SenderFactory, v1_0ReceiverFactory);
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

    @Override
    public RealObjectBridge<?, ?, ?, ?> getChild(String id) {
        if(SET_COMMAND_ID.equals(id))
            return setCommand;
        return null;
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
