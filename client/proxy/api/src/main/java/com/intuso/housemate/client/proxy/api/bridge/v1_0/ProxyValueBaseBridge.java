package com.intuso.housemate.client.proxy.api.bridge.v1_0;

import com.intuso.housemate.client.api.bridge.v1_0.ObjectMapper;
import com.intuso.housemate.client.api.bridge.v1_0.TypeInstancesMapper;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.object.Value;
import com.intuso.housemate.client.api.internal.object.ValueBase;
import com.intuso.housemate.client.proxy.api.internal.ChildUtil;
import com.intuso.housemate.client.proxy.api.internal.object.JMSUtil;
import com.intuso.utilities.listener.ListenersFactory;
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

    private JMSUtil.Sender valueSender;
    private com.intuso.housemate.client.proxy.api.internal.object.JMSUtil.Receiver<com.intuso.housemate.client.v1_0.api.object.Type.Instances> valueReceiver;

    protected ProxyValueBaseBridge(Logger logger,
                                   Class<VERSION_DATA> versionDataClass,
                                   ObjectMapper<VERSION_DATA, INTERNAL_DATA> dataMapper,
                                   TypeInstancesMapper typeInstancesMapper,
                                   ListenersFactory listenersFactory) {
        super(logger, versionDataClass, dataMapper, listenersFactory);
        this.typeInstancesMapper = typeInstancesMapper;
    }

    @Override
    protected void initChildren(String versionName, String internalName, Connection connection) throws JMSException {
        super.initChildren(versionName, internalName, connection);
        valueSender = new JMSUtil.Sender(logger, connection, JMSUtil.Type.Topic, ChildUtil.name(internalName, Value.VALUE_ID));
        valueReceiver = new com.intuso.housemate.client.proxy.api.internal.object.JMSUtil.Receiver<>(logger, connection, JMSUtil.Type.Topic, com.intuso.housemate.client.proxy.api.internal.ChildUtil.name(versionName, Value.VALUE_ID), com.intuso.housemate.client.v1_0.api.object.Type.Instances.class,
                new com.intuso.housemate.client.proxy.api.internal.object.JMSUtil.Receiver.Listener<com.intuso.housemate.client.v1_0.api.object.Type.Instances>() {
            @Override
            public void onMessage(com.intuso.housemate.client.v1_0.api.object.Type.Instances instances, boolean wasPersisted) {
                value = typeInstancesMapper.map(instances);
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
