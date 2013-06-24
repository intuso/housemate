package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.primary.PrimaryListener;
import com.intuso.housemate.api.object.primary.PrimaryObject;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.List;

/**
 */
public abstract class ProxyPrimaryObject<
            R extends ProxyResources<? extends HousemateObjectFactory<SR, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            SR extends ProxyResources<?>,
            WBL extends HousemateObjectWrappable<HousemateObjectWrappable<?>>,
            C extends ProxyCommand<?, ?, ?, ?, C>,
            V extends ProxyValue<?, ?, V>,
            PO extends ProxyPrimaryObject<R, SR, WBL, C, V, PO, L>,
            L extends PrimaryListener<? super PO>>
        extends ProxyObject<R, SR, WBL, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>, PO, L>
        implements PrimaryObject<C, C, V, V, V, PO, L> {

    private C remove;
    private V connected;
    private V running;
    private C start;
    private C stop;
    private V error;

    protected ProxyPrimaryObject(R resources, SR subResources, WBL wrappable) {
        super(resources, subResources, wrappable);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        remove = (C)getWrapper(REMOVE_COMMAND_ID);
        connected = (V)getWrapper(CONNECTED_VALUE_ID);
        running = (V)getWrapper(RUNNING_VALUE_ID);
        start = (C)getWrapper(START_COMMAND_ID);
        stop = (C)getWrapper(STOP_COMMAND_ID);
        error = (V)getWrapper(ERROR_VALUE_ID);
    }

    @Override
    public C getRemoveCommand() {
        return remove;
    }

    @Override
    public boolean isConnected() {
        return connected.getTypeInstance() != null ? Boolean.parseBoolean(connected.getTypeInstance().getValue()) : null;
    }

    @Override
    public V getConnectedValue() {
        return connected;
    }

    @Override
    public final boolean isRunning() {
        return running.getTypeInstance() != null ? Boolean.parseBoolean(running.getTypeInstance().getValue()) : null;
    }

    @Override
    public V getRunningValue() {
        return running;
    }

    @Override
    public C getStartCommand() {
        return start;
    }

    @Override
    public C getStopCommand() {
        return stop;
    }

    @Override
    public final String getError() {
        return error.getTypeInstance() != null ? error.getTypeInstance().getValue() : null;
    }

    @Override
    public V getErrorValue() {
        return error;
    }

    @Override
    public List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        if(running != null) {
            result.add(running.addObjectListener(new ValueListener<V>() {

                @Override
                public void valueChanging(V value) {
                    // do nothing
                }

                @Override
                public void valueChanged(V value) {
                    for(PrimaryListener<? super PO> listener : getObjectListeners())
                        listener.running(getThis(), isRunning());
                }
            }));
        }
        if(error != null) {
            result.add(error.addObjectListener(new ValueListener<V>() {

                @Override
                public void valueChanging(V value) {
                    // do nothing
                }

                @Override
                public void valueChanged(V value) {
                    for (PrimaryListener<? super PO> listener : getObjectListeners())
                        listener.error(getThis(), getError());
                }
            }));
        }
        return result;
    }
}
