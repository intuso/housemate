package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.primary.PrimaryListener;
import com.intuso.housemate.api.object.primary.PrimaryObject;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.broker.real.BrokerRealCommand;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.broker.real.BrokerRealValue;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.List;

/**
 */
public class BrokerProxyPrimaryObject<WBL extends HousemateObjectWrappable<HousemateObjectWrappable<?>>,
            PO extends BrokerProxyPrimaryObject<WBL, PO, L>,
            L extends PrimaryListener<? super PO>>
        extends BrokerProxyObject<WBL, HousemateObjectWrappable<?>, BrokerProxyObject<?, ?, ?, ?, ?>, PO, L>
        implements PrimaryObject<BrokerRealCommand, BrokerProxyCommand, BrokerRealValue<Boolean>,
            BrokerProxyValue, BrokerProxyValue, PO, L> {

    private BrokerProxyCommand realRemoveCommand;
    private BrokerRealCommand remove;
    private BrokerRealValue<Boolean> connected;
    private BrokerProxyValue running;
    private BrokerProxyCommand start;
    private BrokerProxyCommand stop;
    private BrokerProxyValue error;
    private Remover<PO> remover;

    protected BrokerProxyPrimaryObject(BrokerProxyResources<? extends HousemateObjectFactory<BrokerProxyResources<?>, HousemateObjectWrappable<?>, ? extends BrokerProxyObject<?, ?, ?, ?, ?>>> resources,
                                       BrokerRealResources realResources, WBL wrappable) {
        super(resources, wrappable);
        connected = new BrokerRealValue<Boolean>(realResources, CONNECTED_VALUE_ID, CONNECTED_VALUE_ID,
                "Whether the server has a connection open to control the object",
                new BooleanType(realResources.getRealResources()), true);
    }

    public void setRemover(Remover<PO> remover) {
        this.remover = remover;
    }

    @Override
    protected void getChildObjects() {
        realRemoveCommand = (BrokerProxyCommand)getWrapper(REMOVE_COMMAND_ID);
        running = (BrokerProxyValue)getWrapper(RUNNING_VALUE_ID);
        start = (BrokerProxyCommand)getWrapper(START_COMMAND_ID);
        stop = (BrokerProxyCommand)getWrapper(STOP_COMMAND_ID);
        error = (BrokerProxyValue)getWrapper(ERROR_VALUE_ID);
        remove = getResources().getLifecycleHandler().createRemovePrimaryObjectCommand(realRemoveCommand, getThis(), remover);
    }

    @Override
    public BrokerRealCommand getRemoveCommand() {
        return remove;
    }

    @Override
    public boolean isConnected() {
        return connected.getTypedValue() != null ? connected.getTypedValue() : false;
    }

    @Override
    public BrokerRealValue<Boolean> getConnectedValue() {
        return connected;
    }

    @Override
    public final boolean isRunning() {
        Boolean value = BooleanType.SERIALISER.deserialise(running.getTypeInstance());
        return value != null ? value : false;
    }

    @Override
    public BrokerProxyValue getRunningValue() {
        return running;
    }

    @Override
    public BrokerProxyCommand getStartCommand() {
        return start;
    }

    @Override
    public BrokerProxyCommand getStopCommand() {
        return stop;
    }

    @Override
    public final String getError() {
        return StringType.SERIALISER.deserialise(error.getTypeInstance());
    }

    @Override
    public BrokerProxyValue getErrorValue() {
        return error;
    }

    @Override
    public List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        if(running != null) {
            result.add(running.addObjectListener(new ValueListener<BrokerProxyValue>() {

                @Override
                public void valueChanging(BrokerProxyValue value) {
                    // do nothing
                }

                @Override
                public void valueChanged(BrokerProxyValue value) {
                    for(PrimaryListener<? super PO> listener : getObjectListeners())
                        listener.running(getThis(), isRunning());
                }
            }));
        }
        if(error != null) {
            result.add(error.addObjectListener(new ValueListener<BrokerProxyValue>() {

                @Override
                public void valueChanging(BrokerProxyValue value) {
                    // do nothing
                }

                @Override
                public void valueChanged(BrokerProxyValue value) {
                    for (PrimaryListener<? super PO> listener : getObjectListeners())
                        listener.error(getThis(), getError());
                }
            }));
        }
        return result;
    }

    public static interface Remover<PO extends BrokerProxyPrimaryObject<?, ?, ?>> {
        public void remove(PO primaryObject);
    }
}
