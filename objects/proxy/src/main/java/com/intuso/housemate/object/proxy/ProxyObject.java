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
 * @param <RESOURCES> the type of the resources
 * @param <CHILD_RESOURCES> the type of the child resources
 * @param <DATA> the type of the data
 * @param <CHILD_DATA> the type of the child data
 * @param <CHILD> the type of the children
 * @param <OBJECT> the type of the object
 * @param <LISTENER> the type of the listener
 */
public abstract class ProxyObject<
            RESOURCES extends ProxyResources<? extends HousemateObjectFactory<CHILD_RESOURCES, CHILD_DATA, CHILD>>,
            CHILD_RESOURCES extends ProxyResources<?>,
            DATA extends HousemateObjectWrappable<CHILD_DATA>,
            CHILD_DATA extends HousemateObjectWrappable<?>,
            CHILD extends ProxyObject<?, ?, ? extends CHILD_DATA, ?, ?, ?, ?>,
            OBJECT extends ProxyObject<?, CHILD_RESOURCES, DATA, CHILD_DATA, CHILD, OBJECT, LISTENER>,
            LISTENER extends ObjectListener>
        extends HousemateObject<RESOURCES, DATA, CHILD_DATA, CHILD, LISTENER> implements BaseObject<LISTENER> {

    private ProxyRoot<?, ?, ?, ?, ?, ?> proxyRoot;
    private final CHILD_RESOURCES childResources;

    /**
     * @param resources the resources
     * @param childResources the child resources
     * @param data the data object
     */
    protected ProxyObject(RESOURCES resources, CHILD_RESOURCES childResources, DATA data) {
        super(resources, data);
        this.childResources = childResources;
    }

    /**
     * Gets the root object
      * @return the root object
     */
    protected ProxyRoot<?, ?, ?, ?, ?, ?> getProxyRoot() {
        return proxyRoot;
    }

    /**
     * Gets the child resources
     * @return the child resources
     */
    protected CHILD_RESOURCES getSubResources() {
        return childResources;
    }

    /**
     * Makes a request to load the child object for the given id
     * @param id the id to load
     */
    public void load(String id) {
        sendMessage(LOAD_REQUEST, new LoadRequest(id));
    }

    /**
     * Sends a message to the broker
     * @param type the type of the message to send
     * @param payload the message payload
     * @param <MV> the type of the message payload
     */
    protected final <MV extends Message.Payload> void sendMessage(String type, MV payload) {
        proxyRoot.sendMessage(new Message<MV>(getPath(), type, payload));
    }

    /**
     * Registers the listeners this object requires
     * @return the registrations for the added listeners
     */
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = Lists.newArrayList();
        result.add(addMessageListener(LOAD_RESPONSE, new Receiver<HousemateObjectWrappable>() {
            @Override
            public void messageReceived(Message<HousemateObjectWrappable> message) throws HousemateException {
                try {
                    CHILD object = getResources().getObjectFactory().create(getSubResources(), (CHILD_DATA)message.getPayload());
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
            unwrapChildren(new WrapperFactory<CHILD_DATA, CHILD, HousemateException>() {
                @Override
                public CHILD create(CHILD_DATA data) throws HousemateException {
                    return getResources().getObjectFactory().create(getSubResources(), data);
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

    /**
     * Once created and loaded, gets the child objects this object requires
     */
    protected void getChildObjects() {}

    /**
     * Gets this object as its sub class type
     * @return this object as its sub class type
     */
    protected final OBJECT getThis() {
        return (OBJECT)this;
    }
}
