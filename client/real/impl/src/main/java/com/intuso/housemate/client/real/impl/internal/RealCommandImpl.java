package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.Command;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.real.api.internal.RealCommand;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 */
public final class RealCommandImpl
        extends RealObject<Command.Data, Command.Listener<? super RealCommandImpl>>
        implements RealCommand<RealValueImpl<Boolean>, RealListGeneratedImpl<RealParameterImpl<?>>, RealCommandImpl> {

    private final static String ENABLED_DESCRIPTION = "Whether the command is enabled or not";

    private final Performer performer;
    private final RealValueImpl<Boolean> enabledValue;
    private final RealListGeneratedImpl<RealParameterImpl<?>> parameters;

    private JMSUtil.Sender performStatusSender;
    private JMSUtil.Receiver<PerformData> performReceiver;

    /**
     * @param logger {@inheritDoc}
     * @param parameters the command's parameters
     * @param listenersFactory
     */
    @Inject
    protected RealCommandImpl(@Assisted Logger logger,
                              @Assisted("id") String id,
                              @Assisted("name") String name,
                              @Assisted("description") String description,
                              @Assisted Performer performer,
                              @Assisted Iterable<? extends RealParameterImpl<?>> parameters,
                              ListenersFactory listenersFactory,
                              RealValueImpl.Factory<Boolean> booleanValueFactory,
                              RealListGeneratedImpl.Factory<RealParameterImpl<?>> parametersFactory) {
        super(logger, false, new Command.Data(id, name, description), listenersFactory);
        this.performer = performer;
        this.enabledValue = booleanValueFactory.create(ChildUtil.logger(logger, Command.ENABLED_ID),
                Command.ENABLED_ID,
                Command.ENABLED_ID,
                ENABLED_DESCRIPTION,
                1,
                1,
                Lists.newArrayList(true));
        this.parameters = parametersFactory.create(ChildUtil.logger(logger, Command.PARAMETERS_ID),
                Command.PARAMETERS_ID,
                Command.PARAMETERS_ID,
                "The parameters required by the command",
                parameters);
    }

    @Override
    protected final void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        enabledValue.init(ChildUtil.name(name, Command.ENABLED_ID), connection);
        parameters.init(ChildUtil.name(name, Command.PARAMETERS_ID), connection);
        performStatusSender = new JMSUtil.Sender(logger, connection, JMSUtil.Type.Topic, ChildUtil.name(name, Command.PERFORM_STATUS_ID));
        performReceiver = new JMSUtil.Receiver<>(logger, connection, JMSUtil.Type.Queue, ChildUtil.name(name, Command.PERFORM_ID), PerformData.class,
                new JMSUtil.Receiver.Listener<PerformData>() {
                    @Override
                    public void onMessage(final PerformData performData, boolean wasPersisted) {
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
                    }
                });
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        enabledValue.uninit();
        parameters.uninit();
        if(performStatusSender != null) {
            performStatusSender.close();
            performStatusSender = null;
        }
        if(performReceiver != null) {
            performReceiver.close();
            performReceiver = null;
        }
    }

    @Override
    public RealValueImpl<Boolean> getEnabledValue() {
        return enabledValue;
    }

    @Override
    public RealListGeneratedImpl<RealParameterImpl<?>> getParameters() {
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

    private void performStatus(String opId, boolean finished, String error) {
        try {
            performStatusSender.send(new Command.PerformStatusData(opId, finished, error), false);
        } catch(JMSException e) {
            logger.error("Failed to send perform status update ({}, {}, {})", opId, finished, error, e);
        }
    }

    @Override
    public void perform(Type.InstanceMap values) {
        performer.perform(values);
    }

    public interface Performer {
        void perform(Type.InstanceMap values);
    }

    public interface Factory {
        RealCommandImpl create(Logger logger,
                               @Assisted("id") String id,
                               @Assisted("name") String name,
                               @Assisted("description") String description,
                               Performer performer,
                               Iterable<? extends RealParameterImpl<?>> parameters);
    }
}
