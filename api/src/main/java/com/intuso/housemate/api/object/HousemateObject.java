package com.intuso.housemate.api.object;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.resources.Resources;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.wrapper.Wrapper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 26/05/12
 * Time: 10:18
 * To change this template use File | Settings | File Templates.
 */
public abstract class HousemateObject<R extends Resources,
            WBL extends HousemateObjectWrappable<SWBL>,
            SWBL extends HousemateObjectWrappable<?>,
            SWR extends HousemateObject<?, ? extends SWBL, ?, ?, ?>,
            L extends ObjectListener>
        extends Wrapper<WBL, SWBL, SWR, HousemateException>
        implements BaseObject<L> {

    public final static String LOAD_REQUEST = "load-request";
    public final static String LOAD_RESPONSE = "load-response";

    private final R resources;

    private String path[];
    private final Listeners<L> objectListeners = new Listeners<L>();
    private final Map<String, Listeners<Receiver<?>>> messageListeners = Maps.newHashMap();
    private final List<ListenerRegistration> listenerRegistrations = Lists.newArrayList();

    protected HousemateObject(R resources, WBL wrappable) {
        super(wrappable);
        this.resources = resources;
    }

    public final String getDescription() {
        return getWrappable().getDescription();
    }

    public final String getName() {
        return getWrappable().getName();
    }

    public R getResources() {
        return resources;
    }

    protected final Log getLog() {
        return resources.getLog();
    }

    public String[] getPath() {
        return path;
    }

    public Set<L> getObjectListeners() {
        return objectListeners.getListeners();
    }

    public ListenerRegistration addObjectListener(L listener) {
        return objectListeners.addListener(listener);
    }

    protected ListenerRegistration addMessageListener(String type, Receiver listener) {
        Listeners<Receiver<?>> listeners = messageListeners.get(type);
        if(listeners == null) {
            listeners = new Listeners<Receiver<?>>();
            messageListeners.put(type, listeners);
        }
        return listeners.addListener(listener);
    }

    public final void distributeMessage(Message<?> message) throws HousemateException {
        if(message.getPath().length == path.length) {
            Listeners<Receiver<?>> listeners = messageListeners.get(message.getType());
            if(listeners == null)
                throw new HousemateException("No listeners known for type \"" + message.getType() + "\" for object " + Arrays.toString(path));
            for(Receiver listener : listeners)
                listener.messageReceived(message);
        } else if(message.getPath().length > path.length) {
            String childName = message.getPath()[path.length];
            HousemateObject<?, ?, ?, ?, ?> subWrapper = getWrapper(childName);
            if(subWrapper == null)
                throw new HousemateException("Unknown child \"" + childName + "\" at depth " + path.length + " of " + Arrays.toString(message.getPath()));
            subWrapper.distributeMessage(message);
        } else
            throw new HousemateException("Message received for path that is a parent of this element. It should not have got here! Oops!");
    }

    public final void init(HousemateObject<?, ?, ?, ?, ?> parent) {

        // build the path
        if(parent != null) {
            path = new String[parent.path.length + 1];
            System.arraycopy(parent.path, 0, path, 0, parent.path.length);
            path[path.length - 1] = getId();
        } else {
            path = new String[] {getId()};
        }

        initPreRecurseHook(parent);

        // recurse
        for(SWR baseWrapper : getWrappers())
            baseWrapper.init(this);

        initPostRecurseHook(parent);

        listenerRegistrations.addAll(registerListeners());
    }

    protected List<ListenerRegistration> registerListeners() {
        return Lists.newArrayList();
    }

    protected void initPreRecurseHook(HousemateObject<?, ?, ?, ?, ?> parent) {}
    protected void initPostRecurseHook(HousemateObject<?, ?, ?, ?, ?> parent) {}

    public final void uninit() {
        for(ListenerRegistration listenerRegistration : listenerRegistrations)
            listenerRegistration.removeListener();
        listenerRegistrations.clear();
        for(SWR baseWrapper : getWrappers())
            baseWrapper.uninit();
    }

    protected static class LoadRequest implements Message.Payload {

        private String subWrapperName;

        private LoadRequest() {}

        public LoadRequest(String subWrapperName) {
            this.subWrapperName = subWrapperName;
        }

        public String getSubWrapperName() {
            return subWrapperName;
        }
    }

    public final HousemateObject<?, ?, ?, ?, ?> getWrapper(String[] path) {
        return getWrapper(path, this);
    }

    public final static HousemateObject<?, ?, ?, ?, ?> getWrapper(String[] path, HousemateObject<?, ?, ?, ?, ?> current) {
        String[] currentPath = current.getPath();
        if(path.length < currentPath.length)
            throw new RuntimeException("Object requested is at a higher level than this object");
        else if(path.length == currentPath.length)
            return current;
        else {
            String childName = path[currentPath.length];
            HousemateObject<?, ?, ?, ?, ?> child = current.getWrapper(childName);
            if(child == null)
                return null;
            else
                return getWrapper(path, child);
        }
    }
}
