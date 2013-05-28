package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.object.BaseObject;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.ObjectListener;
import com.intuso.housemate.api.object.connection.ClientWrappable;
import com.intuso.housemate.object.broker.DisconnectListener;
import com.intuso.housemate.object.broker.RemoteClient;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.wrapper.WrapperFactory;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 26/05/12
 * Time: 10:18
 * To change this template use File | Settings | File Templates.
 */
public abstract class BrokerProxyObject<WBL extends HousemateObjectWrappable<SWBL>,
            SWBL extends HousemateObjectWrappable<?>,
            SWR extends BrokerProxyObject<? extends SWBL, ?, ?, ?, ?>,
            PBO extends BrokerProxyObject<?, ?, ?, ?, ?>,
            L extends ObjectListener>
        extends HousemateObject<BrokerProxyResources<? extends HousemateObjectFactory<BrokerProxyResources<?>, SWBL, ? extends SWR>>, WBL, SWBL, SWR, L> implements BaseObject<L>, DisconnectListener {

    private RemoteClient client;
    private ListenerRegistration clientListener;

    protected BrokerProxyObject(BrokerProxyResources<? extends HousemateObjectFactory<BrokerProxyResources<?>, SWBL, ? extends SWR>> resources, WBL wrappable) {
        super(resources, wrappable);
    }

    public void setClient(RemoteClient client) throws HousemateException {
        if(this.client != null)
            throw new HousemateException("This object already has a client");
        else if(client.getType() != ClientWrappable.Type.REAL)
            throw new HousemateException("Client is not of type " + ClientWrappable.Type.REAL);
        if(clientListener != null)
            clientListener.removeListener();
        this.client = client;
        clientListener = client.addDisconnectListener(this);
        for(BrokerProxyObject<?, ?, ?, ?, ?> child : getWrappers())
                ((BrokerProxyObject)child).setClient(client);
    }

    protected final <MV extends Message.Payload> void sendMessage(String type, MV value) throws HousemateException {
        getResources().getComms().sendMessageToClient(getPath(), type, value, client);
    }

    @Override
    protected void initPreRecurseHook(HousemateObject<?, ?, ?, ?, ?> parent) {
        // unwrap children
        try {
            unwrapChildren(new WrapperFactory<SWBL, SWR, HousemateException>() {
                @Override
                public SWR create(SWBL wrappable) throws HousemateException {
                    return getResources().getFactory().create(getResources(), wrappable);
                }
            });
        } catch(HousemateException e) {
            throw new HousemateRuntimeException("Failed to unwrap child object", e);
        }
    }

    @Override
    protected void initPostRecurseHook(HousemateObject<?, ?, ?, ?, ?> parent) {
        getChildObjects();
    }

    protected void getChildObjects() {}

    protected final PBO getThis() {
        return (PBO)this;
    }

    @Override
    public void disconnected(RemoteClient client) {
        clientListener.removeListener();
        client = null;
    }
}
