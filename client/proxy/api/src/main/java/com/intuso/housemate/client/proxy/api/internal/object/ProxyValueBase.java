package com.intuso.housemate.client.proxy.api.internal.object;

import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.object.Value;
import com.intuso.housemate.client.api.internal.object.ValueBase;
import com.intuso.housemate.client.proxy.api.internal.ChildUtil;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

/**
 * @param <DATA> the type of the data
 * @param <TYPE> the type of the type
 * @param <VALUE> the type of the value
 */
public abstract class ProxyValueBase<
            DATA extends Object.Data,
            TYPE extends ProxyType<?>,
            LISTENER extends ValueBase.Listener<? super VALUE>,
            VALUE extends ProxyValueBase<DATA, TYPE, LISTENER, VALUE>>
        extends ProxyObject<DATA, LISTENER>
        implements ValueBase<Type.Instances, TYPE, LISTENER, VALUE> {

    private Session session;
    private JMSUtil.Receiver<Type.Instances> valueReceiver;

    private Type.Instances value;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyValueBase(Logger logger,
                          Class<DATA> dataClass,
                          ListenersFactory listenersFactory) {
        super(logger, dataClass, listenersFactory);
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        valueReceiver = new JMSUtil.Receiver<>(logger,
                session.createConsumer(session.createTopic(ChildUtil.name(name, Value.VALUE_ID) + "?consumer.retroactive=true")),
                Type.Instances.class,
                new JMSUtil.Receiver.Listener<Type.Instances>() {
            @Override
            public void onMessage(Type.Instances instances, boolean wasPersisted) {
                value = instances;
                // todo call object listeners
            }
        });
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        if(valueReceiver != null) {
            try {
                valueReceiver.close();
            } catch(JMSException e) {
                logger.error("Failed to close value receiver");
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
    public TYPE getType() {
        return null; // todo get the type from somewhere
    }

    @Override
    public Type.Instances getValue() {
        return value;
    }
}
