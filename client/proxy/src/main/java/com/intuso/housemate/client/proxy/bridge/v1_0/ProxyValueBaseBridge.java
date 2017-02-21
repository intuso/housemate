package com.intuso.housemate.client.proxy.bridge.v1_0;

import com.intuso.housemate.client.api.bridge.v1_0.object.ObjectMapper;
import com.intuso.housemate.client.api.bridge.v1_0.object.TypeInstancesMapper;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.object.Value;
import com.intuso.housemate.client.api.internal.object.ValueBase;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.housemate.client.proxy.internal.object.JMSUtil;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Created by tomc on 28/11/16.
 */
public abstract class ProxyValueBaseBridge<
        VERSION_DATA extends com.intuso.housemate.client.v1_0.api.object.Object.Data,
        INTERNAL_DATA extends Object.Data,
        LISTENER extends ValueBase.Listener<? super VALUE>,
        VALUE extends ProxyValueBaseBridge<VERSION_DATA, INTERNAL_DATA, LISTENER, VALUE>>
        extends ProxyObjectBridge<VERSION_DATA, INTERNAL_DATA, LISTENER>
        implements ValueBase<Type.Instances, ProxyTypeBridge, LISTENER, VALUE> {

    private final TypeInstancesMapper typeInstancesMapper;

    private Type.Instances value;

    private com.intuso.housemate.client.proxy.internal.object.JMSUtil.Sender valueSender;
    private JMSUtil.Receiver<Type.Instances> valueReceiver;

    protected ProxyValueBaseBridge(Logger logger,
                                   Class<INTERNAL_DATA> versionDataClass,
                                   ObjectMapper<VERSION_DATA, INTERNAL_DATA> dataMapper,
                                   TypeInstancesMapper typeInstancesMapper,
                                   ManagedCollectionFactory managedCollectionFactory) {
        super(logger, versionDataClass, dataMapper, managedCollectionFactory);
        this.typeInstancesMapper = typeInstancesMapper;
    }

    @Override
    protected void initChildren(String versionName, String internalName, Connection connection) throws JMSException {
        super.initChildren(versionName, internalName, connection);
        valueSender = new com.intuso.housemate.client.proxy.internal.object.JMSUtil.Sender(logger, connection, com.intuso.housemate.client.proxy.internal.object.JMSUtil.Type.Topic, com.intuso.housemate.client.proxy.internal.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Value.VALUE_ID));
        valueReceiver = new JMSUtil.Receiver<>(logger, connection, JMSUtil.Type.Topic, ChildUtil.name(internalName, Value.VALUE_ID), Type.Instances.class,
                new JMSUtil.Receiver.Listener<Type.Instances>() {
            @Override
            public void onMessage(Type.Instances instances, boolean wasPersisted) {
                value = instances;
                try {
                    valueSender.send(typeInstancesMapper.map(instances), wasPersisted);
                } catch (JMSException e) {
                    logger.error("Failed to send new values onto proxy versioned topic");
                }
                // todo call object listeners
            }
        });
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        if(valueSender != null) {
            valueSender.close();
            valueSender = null;
        }
        if(valueReceiver != null) {
            valueReceiver.close();
            valueReceiver = null;
        }
    }

    @Override
    public ProxyTypeBridge getType() {
        return null; // todo get the type from somewhere
    }

    @Override
    public Type.Instances getValue() {
        return value;
    }
}
