package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.comms.message.StringPayload;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.primary.PrimaryListener;
import com.intuso.housemate.api.object.primary.PrimaryObject;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 * @param <DATA> the type of the data
 * @param <COMMAND> the type of the command
 * @param <VALUE> the type of the value
 * @param <PRIMARY_OBJECT> the type of the primary object
 * @param <LISTENER> the type of the listener
 */
public abstract class ProxyPrimaryObject<
            DATA extends HousemateData<HousemateData<?>>,
            COMMAND extends ProxyCommand<?, ?, ?, COMMAND>,
            VALUE extends ProxyValue<?, VALUE>,
            PRIMARY_OBJECT extends ProxyPrimaryObject<DATA, COMMAND, VALUE, PRIMARY_OBJECT, LISTENER>,
            LISTENER extends PrimaryListener<? super PRIMARY_OBJECT>>
        extends ProxyObject<DATA, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>, PRIMARY_OBJECT, LISTENER>
        implements PrimaryObject<COMMAND, COMMAND, COMMAND, VALUE, VALUE, PRIMARY_OBJECT, LISTENER> {

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param data {@inheritDoc}
     */
    protected ProxyPrimaryObject(Log log, ListenersFactory listenersFactory, DATA data) {
        super(log, listenersFactory, data);
    }

    @Override
    public COMMAND getRenameCommand() {
        return (COMMAND) getChild(RENAME_ID);
    }

    @Override
    public COMMAND getRemoveCommand() {
        return (COMMAND) getChild(REMOVE_ID);
    }

    @Override
    public final boolean isRunning() {
        VALUE running = getRunningValue();
        return running.getTypeInstances() != null && running.getTypeInstances().getFirstValue() != null
                ? Boolean.parseBoolean(running.getTypeInstances().getFirstValue()) : false;
    }

    @Override
    public VALUE getRunningValue() {
        return (VALUE) getChild(RUNNING_ID);
    }

    @Override
    public COMMAND getStartCommand() {
        return (COMMAND) getChild(START_ID);
    }

    @Override
    public COMMAND getStopCommand() {
        return (COMMAND) getChild(STOP_ID);
    }

    @Override
    public final String getError() {
        VALUE error = getErrorValue();
        return error.getTypeInstances() != null ? error.getTypeInstances().getFirstValue() : null;
    }

    @Override
    public VALUE getErrorValue() {
        return (VALUE) getChild(ERROR_ID);
    }

    @Override
    public List<ListenerRegistration> registerListeners() {
        final List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(NEW_NAME, new Receiver<StringPayload>() {
            @Override
            public void messageReceived(Message<StringPayload> message) throws HousemateException {
                String oldName = getData().getName();
                String newName = message.getPayload().getValue();
                getData().setName(newName);
                for(PrimaryListener<? super PRIMARY_OBJECT> listener : getObjectListeners())
                    listener.renamed(getThis(), oldName, newName);
            }
        }));
        addChildLoadedListener(RUNNING_ID, new ChildLoadedListener<PRIMARY_OBJECT, ProxyObject<?, ?, ?, ?, ?>>() {
            @Override
            public void childLoaded(PRIMARY_OBJECT object, ProxyObject<?, ?, ?, ?, ?> proxyObject) {
                result.add(getRunningValue().addObjectListener(new ValueListener<VALUE>() {

                    @Override
                    public void valueChanging(VALUE value) {
                        // do nothing
                    }

                    @Override
                    public void valueChanged(VALUE value) {
                        for(PrimaryListener<? super PRIMARY_OBJECT> listener : getObjectListeners())
                            listener.running(getThis(), isRunning());
                    }
                }));
            }
        });
        addChildLoadedListener(ERROR_ID, new ChildLoadedListener<PRIMARY_OBJECT, ProxyObject<?, ?, ?, ?, ?>>() {
            @Override
            public void childLoaded(PRIMARY_OBJECT object, ProxyObject<?, ?, ?, ?, ?> proxyObject) {
                result.add(getErrorValue().addObjectListener(new ValueListener<VALUE>() {

                    @Override
                    public void valueChanging(VALUE value) {
                        // do nothing
                    }

                    @Override
                    public void valueChanged(VALUE value) {
                        for(PrimaryListener<? super PRIMARY_OBJECT> listener : getObjectListeners())
                            listener.error(getThis(), getError());
                    }
                }));
            }
        });
        return result;
    }
}
