package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.primary.PrimaryListener;
import com.intuso.housemate.api.object.primary.PrimaryObject;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.broker.real.BrokerRealCommand;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 20/01/13
 * Time: 00:51
 * To change this template use File | Settings | File Templates.
 */
public class BrokerProxyPrimaryObject<WBL extends HousemateObjectWrappable<HousemateObjectWrappable<?>>,
            PO extends BrokerProxyPrimaryObject<WBL, PO, L>,
            L extends PrimaryListener<? super PO>>
        extends BrokerProxyObject<WBL, HousemateObjectWrappable<?>, BrokerProxyObject<?, ?, ?, ?, ?>, PO, L>
        implements PrimaryObject<BrokerProxyProperty, BrokerRealCommand, BrokerProxyCommand, BrokerProxyValue,
            BrokerProxyValue, PO, L> {

    private BrokerProxyCommand realRemoveCommand;
    private BrokerRealCommand remove;
    private BrokerProxyValue running;
    private BrokerProxyCommand start;
    private BrokerProxyCommand stop;
    private BrokerProxyValue error;
    private Remover<PO> remover;

    protected BrokerProxyPrimaryObject(BrokerProxyResources<? extends HousemateObjectFactory<BrokerProxyResources<?>, HousemateObjectWrappable<?>, ? extends BrokerProxyObject<?, ?, ?, ?, ?>>> resources, WBL wrappable) {
        super(resources, wrappable);
    }

    public void setRemover(Remover<PO> remover) {
        this.remover = remover;
    }

    @Override
    protected void getChildObjects() {
        realRemoveCommand = (BrokerProxyCommand)getWrapper(REMOVE_COMMAND);
        running = (BrokerProxyValue)getWrapper(RUNNING_VALUE);
        start = (BrokerProxyCommand)getWrapper(START_COMMAND);
        stop = (BrokerProxyCommand)getWrapper(STOP_COMMAND);
        error = (BrokerProxyValue)getWrapper(ERROR_VALUE);
        remove = getResources().getLifecycleHandler().createRemovePrimaryObjectCommand(realRemoveCommand, getThis(), remover);
    }

    @Override
    public BrokerRealCommand getRemoveCommand() {
        return remove;
    }

    @Override
    public final Boolean isRunning() {
        return BooleanType.SERIALISER.deserialise(running.getValue());
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
        return error.getValue();
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
                public void valueChanged(BrokerProxyValue value) {
                    for(PrimaryListener<? super PO> listener : getObjectListeners())
                        listener.running(getThis(), isRunning());
                }
            }));
        }
        if(error != null) {
            result.add(error.addObjectListener(new ValueListener<BrokerProxyValue>() {
                @Override
                public void valueChanged(BrokerProxyValue value) {
                    for (PrimaryListener<? super PO> listener : getObjectListeners())
                        listener.error(getThis(), error.getValue());
                }
            }));
        }
        return result;
    }

    public static interface Remover<PO extends BrokerProxyPrimaryObject<?, ?, ?>> {
        public void remove(PO primaryObject);
    }
}
