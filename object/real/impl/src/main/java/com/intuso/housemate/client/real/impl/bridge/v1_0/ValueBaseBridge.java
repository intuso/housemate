package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.intuso.housemate.client.api.bridge.v1_0.ObjectMapper;
import com.intuso.housemate.client.api.bridge.v1_0.TypeInstancesMapper;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.object.Value;
import com.intuso.housemate.client.api.internal.object.ValueBase;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.JMSUtil;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

/**
 * Created by tomc on 28/11/16.
 */
public abstract class ValueBaseBridge<
        VERSION_DATA extends com.intuso.housemate.client.v1_0.api.object.Object.Data,
        INTERNAL_DATA extends Object.Data,
        LISTENER extends ValueBase.Listener<? super VALUE>,
        VALUE extends ValueBaseBridge<VERSION_DATA, INTERNAL_DATA, LISTENER, VALUE>>
        extends BridgeObject<VERSION_DATA, INTERNAL_DATA, LISTENER>
        implements ValueBase<Type.Instances, TypeBridge, LISTENER, VALUE> {

    private final TypeInstancesMapper typeInstancesMapper;

    private Type.Instances value;

    private Session session;
    private JMSUtil.Sender valueSender;
    private com.intuso.housemate.client.v1_0.real.impl.JMSUtil.Receiver<com.intuso.housemate.client.v1_0.api.object.Type.Instances> valueReceiver;

    protected ValueBaseBridge(Logger logger,
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
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        valueSender = new JMSUtil.Sender(session, session.createProducer(session.createTopic(ChildUtil.name(internalName, Value.VALUE_ID) + "?consumer.retroactive=true")));
        valueReceiver = new com.intuso.housemate.client.v1_0.real.impl.JMSUtil.Receiver<>(logger,
                session.createConsumer(session.createTopic(com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, Value.VALUE_ID) + "?consumer.retroactive=true")),
                com.intuso.housemate.client.v1_0.api.object.Type.Instances.class,
                new com.intuso.housemate.client.v1_0.real.impl.JMSUtil.Receiver.Listener<com.intuso.housemate.client.v1_0.api.object.Type.Instances>() {
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
            try {
                valueSender.close();
            } catch(JMSException e) {
                logger.error("Failed to close value producer");
            }
            valueSender = null;
        }
        if(valueReceiver != null) {
            try {
                valueReceiver.close();
            } catch(JMSException e) {
                logger.error("Failed to close value consumer");
            }
            valueReceiver = null;
        }
        if(session != null) {
            try {
                session.close();
            } catch(JMSException e) {
                logger.error("Failed to close session");
            }
            session = null;
        }
    }

    @Override
    public TypeBridge getType() {
        return null; // todo get the type from somewhere
    }

    @Override
    public Type.Instances getValue() {
        return value;
    }
}
