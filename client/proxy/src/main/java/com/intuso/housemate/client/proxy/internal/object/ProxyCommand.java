package com.intuso.housemate.client.proxy.internal.object;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.Command;
import com.intuso.housemate.client.api.internal.object.Tree;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.object.view.CommandView;
import com.intuso.housemate.client.api.internal.object.view.ListView;
import com.intuso.housemate.client.api.internal.object.view.ValueView;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @param <VALUE> the type of the value for the enabled status of the command
 * @param <PARAMETERS> the type of the parameters list
 * @param <COMMAND> the type of the command
 */
public abstract class ProxyCommand<
        VALUE extends ProxyValue<?, ?>,
        PARAMETERS extends ProxyList<? extends ProxyParameter<?, ?>, ?>,
        COMMAND extends ProxyCommand<VALUE, PARAMETERS, COMMAND>>
        extends ProxyObject<Command.Data, Command.Listener<? super COMMAND>, CommandView>
        implements Command<Type.InstanceMap, VALUE, PARAMETERS, COMMAND> {

    private final ProxyObject.Factory<VALUE> valueFactory;
    private final ProxyObject.Factory<PARAMETERS> parametersFactory;

    private VALUE enabledValue;
    private PARAMETERS parameters;

    private Sender performSender;
    private Receiver<PerformStatusData> performStatusReceiver;

    private int nextId;
    private final Map<String, Command.PerformListener<? super COMMAND>> listenerMap = Maps.newHashMap();

    /**
     * @param logger {@inheritDoc}
     */
    protected ProxyCommand(Logger logger,
                           String name,
                           ManagedCollectionFactory managedCollectionFactory,
                           Receiver.Factory receiverFactory,
                           Sender.Factory senderFactory,
                           ProxyObject.Factory<VALUE> valueFactory,
                           ProxyObject.Factory<PARAMETERS> parametersFactory) {
        super(logger, name, Command.Data.class, managedCollectionFactory, receiverFactory);
        this.valueFactory = valueFactory;
        this.parametersFactory = parametersFactory;

        performSender = senderFactory.create(logger, ChildUtil.name(name, PERFORM_ID));
        performStatusReceiver = receiverFactory.create(logger, ChildUtil.name(name, PERFORM_STATUS_ID), PerformStatusData.class);
        performStatusReceiver.listen(new Receiver.Listener<PerformStatusData>() {
            @Override
            public void onMessage(PerformStatusData performStatusData, boolean wasPersisted) {
                if (listenerMap.containsKey(performStatusData.getOpId())) {
                    if (performStatusData.isFinished()) {
                        if (performStatusData.getError() == null)
                            listenerMap.remove(performStatusData.getOpId()).commandFinished(ProxyCommand.this.getThis());
                        else
                            listenerMap.remove(performStatusData.getOpId()).commandFailed(ProxyCommand.this.getThis(), performStatusData.getError());
                    } else
                        listenerMap.get(performStatusData.getOpId()).commandStarted(ProxyCommand.this.getThis());
                }
                // todo call object listeners
            }
        });
    }

    @Override
    public CommandView createView(View.Mode mode) {
        return new CommandView(mode);
    }

    @Override
    public Tree getTree(CommandView view, Tree.Listener listener, List<ManagedCollection.Registration> listenerRegistrations) {

        // register the listener
        addTreeListener(view, listener, listenerRegistrations);

        // make sure what they want is loaded
        load(view);

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
    public void load(CommandView view) {

        super.load(view);

        // create things according to the view's mode, sub-views, and what's already created
        switch (view.getMode()) {
            case ANCESTORS:
            case CHILDREN:
                if (enabledValue == null)
                    enabledValue = valueFactory.create(ChildUtil.logger(logger, ENABLED_ID), ChildUtil.name(name, ENABLED_ID));
                if (parameters == null)
                    parameters = parametersFactory.create(ChildUtil.logger(logger, PARAMETERS_ID), ChildUtil.name(name, PARAMETERS_ID));
                break;
            case SELECTION:
                if (enabledValue == null && view.getEnabledValue() != null)
                    enabledValue = valueFactory.create(ChildUtil.logger(logger, ENABLED_ID), ChildUtil.name(name, ENABLED_ID));
                if (parameters == null && view.getParameters() != null)
                    parameters = parametersFactory.create(ChildUtil.logger(logger, PARAMETERS_ID), ChildUtil.name(name, PARAMETERS_ID));
                break;
        }

        // view things according to the view's mode and sub-views
        switch (view.getMode()) {
            case ANCESTORS:
                enabledValue.load(new ValueView(View.Mode.ANCESTORS));
                parameters.load(new ListView(View.Mode.ANCESTORS));
                break;
            case CHILDREN:
            case SELECTION:
                if (view.getEnabledValue() != null)
                    enabledValue.load(view.getEnabledValue());
                if (view.getParameters() != null)
                    parameters.load(view.getParameters());
                break;
        }
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        if(enabledValue != null)
            enabledValue.uninit();
        if(parameters != null)
            parameters.uninit();
        if(performSender != null) {
            performSender.close();
            performSender = null;
        }
        if(performStatusReceiver != null) {
            performStatusReceiver.close();
            performStatusReceiver = null;
        }
    }

    public boolean isEnabled() {
        return enabledValue != null
                && enabledValue.getValues() != null
                && enabledValue.getValues().getFirstValue() != null
                && Boolean.parseBoolean(enabledValue.getValues().getFirstValue());
    }

    @Override
    public VALUE getEnabledValue() {
        return enabledValue;
    }

    @Override
    public PARAMETERS getParameters() {
        return parameters;
    }

    /**
     * Performs the command without any type values. It is not correct to use this method on a command that has parameters
     * @param listener the listener for progress of the command
     */
    public final void perform(Command.PerformListener<? super COMMAND> listener) {
        perform(new Type.InstanceMap(), listener);
    }

    /**
     * Performs the command without any type values and blocks until the command is complete or failed
     * @param timeout
     */
    public final void performSync(long timeout) throws InterruptedException {
        performSync(new Type.InstanceMap(), timeout);
    }

    /**
     * Performs the command and blocks until the command is complete or failed
     */
    public final void performSync(Type.InstanceMap values, long timeout) throws InterruptedException {
        SyncListener syncListener = new SyncListener();
        perform(values, syncListener);
        syncListener.block(timeout);
    }

    private COMMAND getThis() {
        return (COMMAND) this;
    }

    @Override
    public final synchronized void perform(Type.InstanceMap values, Command.PerformListener<? super COMMAND> listener) {
        String id = null;
        if(listener != null) {
            id = "" + nextId++;
            listenerMap.put(id, listener);
        }
        logger.info("Performing");
        try {
            performSender.send(new PerformData(id, values), true);
        } catch(Throwable t) {
            if(listener != null) {
                listenerMap.remove(id);
                listener.commandFailed(getThis(), "Failed to send perform message: " + t.getMessage());
            }
            throw new HousemateException("Failed to send perform message", t);
        }
    }

    @Override
    public ProxyObject<?, ?, ?> getChild(String id) {
        if(ENABLED_ID.equals(id)) {
            if (enabledValue == null)
                enabledValue = valueFactory.create(ChildUtil.logger(logger, ENABLED_ID), ChildUtil.name(name, ENABLED_ID));
            return enabledValue;
        } else if(PARAMETERS_ID.equals(id)) {
            if (parameters == null)
                parameters = parametersFactory.create(ChildUtil.logger(logger, PARAMETERS_ID), ChildUtil.name(name, PARAMETERS_ID));
            return parameters;
        }
        return null;
    }

    /**
     * Created with IntelliJ IDEA.
     * User: tomc
     * Date: 14/01/14
     * Time: 13:16
     * To change this template use File | Settings | File Templates.
     */
    public static final class Simple extends ProxyCommand<
            ProxyValue.Simple,
            ProxyList.Simple<ProxyParameter.Simple>,
            Simple> {

        @Inject
        public Simple(@Assisted Logger logger,
                      @Assisted String name,
                      ManagedCollectionFactory managedCollectionFactory,
                      Receiver.Factory receiverFactory,
                      Sender.Factory senderFactory,
                      Factory<ProxyValue.Simple> valueFactory,
                      Factory<ProxyList.Simple<ProxyParameter.Simple>> parametersFactory) {
            super(logger, name, managedCollectionFactory, receiverFactory, senderFactory, valueFactory, parametersFactory);
        }
    }

    public static abstract class DelegatingPerformListener<COMMAND extends Command<?, ?, ?, ?>> implements PerformListener<COMMAND> {

        private final PerformListener<? super COMMAND> delegate;

        protected DelegatingPerformListener(PerformListener<? super COMMAND> delegate) {
            this.delegate = delegate;
        }

        @Override
        public void commandStarted(COMMAND command) {
            if(delegate != null)
                delegate.commandStarted(command);
        }

        @Override
        public void commandFinished(COMMAND command) {
            if(delegate != null)
                delegate.commandFinished(command);
        }

        @Override
        public void commandFailed(COMMAND command, String error) {
            if(delegate != null)
                delegate.commandFailed(command, error);
        }
    }

    public static class SyncListener extends DelegatingPerformListener<ProxyCommand<?, ?, ?>> {

        private final CountDownLatch countDownLatch = new CountDownLatch(1);
        private String error = null;

        protected SyncListener() {
            super(null);
        }

        protected SyncListener(PerformListener<? super ProxyCommand<?, ?, ?>> delegate) {
            super(delegate);
        }

        @Override
        public void commandFinished(ProxyCommand<?, ?, ?> command) {
            countDownLatch.countDown();
            super.commandFinished(command);
        }

        @Override
        public void commandFailed(ProxyCommand<?, ?, ?> command, String error) {
            this.error = error;
            countDownLatch.countDown();
            super.commandFailed(command, error);
        }

        private void block(long timeout) throws InterruptedException {
            countDownLatch.await(timeout, TimeUnit.MILLISECONDS);
            if(error != null)
                throw new HousemateException(error);
        }
    }
}
