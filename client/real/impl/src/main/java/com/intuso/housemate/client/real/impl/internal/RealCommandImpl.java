package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.Command;
import com.intuso.housemate.client.api.internal.object.Tree;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.object.view.CommandView;
import com.intuso.housemate.client.api.internal.object.view.ListView;
import com.intuso.housemate.client.api.internal.object.view.ValueView;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.real.api.internal.RealCommand;
import com.intuso.housemate.client.real.impl.internal.type.TypeRepository;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;

/**
 */
public final class RealCommandImpl
        extends RealObject<Command.Data, Command.Listener<? super RealCommandImpl>, CommandView>
        implements RealCommand<RealValueImpl<Boolean>, RealListGeneratedImpl<RealParameterImpl<?>>, RealCommandImpl> {

    private final static String ENABLED_DESCRIPTION = "Whether the command is enabled or not";

    private final Receiver.Factory receiverFactory;

    private final Performer performer;
    private final RealValueImpl<Boolean> enabledValue;
    private final RealListGeneratedImpl<RealParameterImpl<?>> parameters;

    private Sender performStatusSender;
    private Receiver<PerformData> performReceiver;

    /**
     * @param logger {@inheritDoc}
     * @param parameters the command's parameters
     * @param managedCollectionFactory
     */
    @Inject
    protected RealCommandImpl(@Assisted Logger logger,
                              @Assisted("id") String id,
                              @Assisted("name") String name,
                              @Assisted("description") String description,
                              @Assisted Performer performer,
                              @Assisted Iterable<? extends RealParameterImpl<?>> parameters,
                              ManagedCollectionFactory managedCollectionFactory,
                              Receiver.Factory receiverFactory,
                              Sender.Factory senderFactory,
                              RealValueImpl.Factory valueFactory,
                              RealListGeneratedImpl.Factory<RealParameterImpl<?>> parametersFactory,
                              TypeRepository typeRepository) {
        super(logger, new Command.Data(id, name, description), managedCollectionFactory, senderFactory);
        this.receiverFactory = receiverFactory;
        this.performer = performer;
        this.enabledValue = (RealValueImpl<Boolean>) valueFactory.create(ChildUtil.logger(logger, Command.ENABLED_ID),
                Command.ENABLED_ID,
                Command.ENABLED_ID,
                ENABLED_DESCRIPTION,
                typeRepository.getType(new TypeSpec(Boolean.class)),
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
    public CommandView createView(View.Mode mode) {
        return new CommandView(mode);
    }

    @Override
    public Tree getTree(CommandView view, Tree.Listener listener, List<ManagedCollection.Registration> listenerRegistrations) {

        // register the listener
        addTreeListener(view, listener, listenerRegistrations);

        // create a result even for a null view
        Tree result = new Tree(getData());

        // get anything else the view wants
        if(view != null && view.getMode() != null) {
            switch (view.getMode()) {

                // get recursively
                case ANCESTORS:
                    result.getChildren().put(ENABLED_ID, enabledValue.getTree(new ValueView(View.Mode.ANCESTORS), listener, listenerRegistrations));
                    result.getChildren().put(PARAMETERS_ID, parameters.getTree(new ListView(View.Mode.ANCESTORS), listener, listenerRegistrations));
                    break;

                    // get all children using inner view. NB all children non-null because of load(). Can give children null views
                case CHILDREN:
                    result.getChildren().put(ENABLED_ID, enabledValue.getTree(view.getEnabledValue(), listener, listenerRegistrations));
                    result.getChildren().put(PARAMETERS_ID, parameters.getTree(view.getParameters(), listener, listenerRegistrations));
                    break;

                case SELECTION:
                    if(view.getEnabledValue() != null)
                        result.getChildren().put(ENABLED_ID, enabledValue.getTree(view.getEnabledValue(), listener, listenerRegistrations));
                    if(view.getParameters() != null)
                        result.getChildren().put(PARAMETERS_ID, parameters.getTree(view.getParameters(), listener, listenerRegistrations));
                    break;
            }

        }

        return result;
    }

    @Override
    protected final void initChildren(String name) {
        super.initChildren(name);
        enabledValue.init(ChildUtil.name(name, Command.ENABLED_ID));
        parameters.init(ChildUtil.name(name, Command.PARAMETERS_ID));
        performStatusSender = senderFactory.create(logger, ChildUtil.name(name, Command.PERFORM_STATUS_ID));
        performReceiver = receiverFactory.create(logger, ChildUtil.name(name, Command.PERFORM_ID), PerformData.class);
        performReceiver.listen(new Receiver.Listener<PerformData>() {
            @Override
            public void onMessage(final PerformData performData, boolean persistent) {
                logger.info("Performing");
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
    public RealObject<?, ?, ?> getChild(String id) {
        if(ENABLED_ID.equals(id))
            return enabledValue;
        else if(PARAMETERS_ID.equals(id))
            return parameters;
        return null;
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
        } catch(Throwable t) {
            logger.error("Failed to send perform status update ({}, {}, {})", opId, finished, error, t);
        }
    }

    @Override
    public void perform(Type.InstanceMap values) {
        performer.perform(values);
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
