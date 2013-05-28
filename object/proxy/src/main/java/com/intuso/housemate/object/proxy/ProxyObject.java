package com.intuso.housemate.object.proxy;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.object.BaseObject;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.ObjectListener;
import com.intuso.housemate.api.object.root.proxy.ProxyRoot;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.wrapper.WrapperFactory;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 26/05/12
 * Time: 10:18
 * To change this template use File | Settings | File Templates.
 */
public abstract class ProxyObject<
            R extends ProxyResources<? extends HousemateObjectFactory<SR, SWBL, SWR>>,
            SR extends ProxyResources<?>,
            WBL extends HousemateObjectWrappable<SWBL>,
            SWBL extends HousemateObjectWrappable<?>,
            SWR extends ProxyObject<?, ?, ? extends SWBL, ?, ?, ?, ?>,
            PBO extends ProxyObject<?, SR, WBL, SWBL, SWR, PBO, L>,
            L extends ObjectListener>
        extends HousemateObject<R, WBL, SWBL, SWR, L> implements BaseObject<L> {

    private ProxyRoot<?, ?, ?, ?, ?, ?> proxyRoot;
    private final SR subResources;

    protected ProxyObject(R resources, SR subResources, WBL wrappable) {
        super(resources, wrappable);
        this.subResources = subResources;
    }

    protected ProxyRoot<?, ?, ?, ?, ?, ?> getProxyRoot() {
        return proxyRoot;
    }

    protected SR getSubResources() {
        return subResources;
    }

    public void load(String name) {
        sendMessage(LOAD_REQUEST, new LoadRequest(name));
    }

    protected final <MV extends Message.Payload> void sendMessage(String type, MV value) {
        proxyRoot.sendMessage(new Message<MV>(getPath(), type, value));
    }

    protected List<ListenerRegistration<?>> registerListeners() {
        List<ListenerRegistration<?>> result = Lists.newArrayList();
        result.add(addMessageListener(LOAD_RESPONSE, new Receiver<HousemateObjectWrappable>() {
            @Override
            public void messageReceived(Message<HousemateObjectWrappable> message) throws HousemateException {
                try {
                    SWR object = getResources().getObjectFactory().create(getSubResources(), (SWBL)message.getPayload());
                    object.init(ProxyObject.this);
                    addWrapper(object);
                } catch(HousemateException e) {
                    getLog().e("Failed to unwrap load response");
                    getLog().st(e);
                }
            }
        }));
        return result;
    }

    @Override
    protected void initPreRecurseHook(HousemateObject<?, ?, ?, ?, ?> parent) {

        // get the broker for this object
        if(this instanceof ProxyRoot)
            proxyRoot = (ProxyRoot)this;
        else if(parent != null && parent instanceof ProxyObject)
            proxyRoot = ((ProxyObject)parent).proxyRoot;

        // unwrap children
        try {
            unwrapChildren(new WrapperFactory<SWBL, SWR, HousemateException>() {
                @Override
                public SWR create(SWBL wrappable) throws HousemateException {
                    return getResources().getObjectFactory().create(getSubResources(), wrappable);
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
}
