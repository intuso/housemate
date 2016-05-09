package com.intuso.housemate.client.real.impl.internal;

import com.intuso.housemate.client.api.internal.object.Command;
import com.intuso.housemate.client.api.internal.object.Serialiser;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.object.Value;
import com.intuso.housemate.client.real.api.internal.RealCommand;
import com.intuso.housemate.client.real.impl.internal.type.BooleanType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.*;
import java.util.Arrays;
import java.util.List;

/**
 */
public abstract class RealCommandImpl
        extends RealObject<Command.Data, Command.Listener<? super RealCommandImpl>>
        implements RealCommand<RealValueImpl<Boolean>, RealListImpl<RealParameterImpl<?>>, RealCommandImpl>, MessageListener {

    private final static String ENABLED_DESCRIPTION = "Whether the command is enabled or not";

    private final RealValueImpl<Boolean> enabledValue;
    private final RealListImpl<RealParameterImpl<?>> parameters;

    private Session session;
    private MessageProducer performStatusProducer;
    private MessageConsumer performConsumer;

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param parameters the command's parameters
     */
    protected RealCommandImpl(Logger logger,
                              Command.Data data,
                              ListenersFactory listenersFactory,
                              RealParameterImpl<?>... parameters) {
        this(logger, data, listenersFactory, Arrays.asList(parameters));
    }

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param parameters the command's parameters
     */
    protected RealCommandImpl(Logger logger,
                              Command.Data data,
                              ListenersFactory listenersFactory,
                              List<RealParameterImpl<?>> parameters) {
        super(logger, data, listenersFactory);
        enabledValue = new RealValueImpl<>(ChildUtil.logger(logger, Command.ENABLED_ID),
                new Value.Data(Command.ENABLED_ID, Command.ENABLED_ID, ENABLED_DESCRIPTION),
                listenersFactory,
                new BooleanType(listenersFactory), true);
        this.parameters = new RealListImpl<>(ChildUtil.logger(logger, Command.PARAMETERS_ID),
                new com.intuso.housemate.client.api.internal.object.List.Data(Command.PARAMETERS_ID, Command.PARAMETERS_ID, "The parameters required by the command"),
                listenersFactory,
                parameters);
    }

    @Override
    protected final void initChildren(String name, Session session) throws JMSException {
        super.initChildren(name, session);
        enabledValue.init(ChildUtil.name(name, Command.ENABLED_ID), session);
        parameters.init(ChildUtil.name(name, Command.PARAMETERS_ID), session);
        this.session = session;
        performStatusProducer = session.createProducer(session.createTopic(ChildUtil.name(name, Command.PERFORM_STATUS_ID)));
        performConsumer = session.createConsumer(session.createQueue(ChildUtil.name(name, Command.PERFORM_ID)));
        performConsumer.setMessageListener(this);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        enabledValue.uninit();
        parameters.uninit();
        if(performStatusProducer != null) {
            try {
                performStatusProducer.close();
            } catch(JMSException e) {
                logger.error("Failed to close perform status producer");
            }
            performStatusProducer = null;
        }
        if(performConsumer != null) {
            try {
                performConsumer.close();
            } catch(JMSException e) {
                logger.error("Failed to close perform producer");
            }
            performConsumer = null;
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
    public RealValueImpl<Boolean> getEnabledValue() {
        return enabledValue;
    }

    @Override
    public RealListImpl<RealParameterImpl<?>> getParameters() {
        return parameters;
    }

    @Override
    public void perform(Type.InstanceMap values, PerformListener<? super RealCommandImpl> listener) {
        try {
            listener.commandStarted(this);
            perform(values);
            listener.commandFinished(this);
        } catch(Throwable t) {
            logger.error("Failed to perform command", t);
            listener.commandFailed(this, t.getMessage());
        }
    }

    /**
     * Performs the command
     * @param values the values of the parameters to use
     */
    public abstract void perform(Type.InstanceMap values);

    @Override
    public void onMessage(Message message) {
        if(message instanceof StreamMessage) {
            StreamMessage streamMessage = (StreamMessage) message;
            try {
                java.lang.Object messageObject = streamMessage.readObject();
                if(messageObject instanceof byte[]) {
                    java.lang.Object object = Serialiser.deserialise((byte[]) messageObject);
                    if (object instanceof PerformData) {
                        final PerformData performData = (PerformData) object;
                        perform(performData.getInstanceMap(), new PerformListener<RealCommandImpl>() {

                            @Override
                            public void commandStarted(RealCommandImpl command) {
                                performStatus(performData.getOpId(), false, null);
                            }

                            @Override
                            public void commandFinished(RealCommandImpl command) {
                                performStatus(performData.getOpId(), true, null);
                            }

                            @Override
                            public void commandFailed(RealCommandImpl command, String error) {
                                performStatus(performData.getOpId(), true, error);
                            }
                        });
                    } else
                        logger.warn("Deserialised message object that wasn't a {}", PerformData.class.getName());
                } else
                    logger.warn("Message data was not a byte[]");
            } catch(JMSException e) {
                logger.error("Failed to read object from message", e);
            }
        } else
            logger.warn("Received message that wasn't a {}", StreamMessage.class.getName());
    }

    private void performStatus(String opId, boolean finished, String error) {
        try {
            StreamMessage streamMessage = session.createStreamMessage();
            streamMessage.writeObject(Serialiser.serialise(new PerformStatusData(opId, finished, error)));
            performStatusProducer.send(streamMessage);
        } catch(JMSException e) {
            logger.error("Failed to send perform status update ({}, {}, {})", opId, finished, error, e);
        }
    }
}
