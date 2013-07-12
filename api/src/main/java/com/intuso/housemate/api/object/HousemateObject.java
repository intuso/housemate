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
import com.intuso.utilities.object.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Base class for all Housemate object implementations
 *
 * @param <RESOURCES> the type of the resources
 * @param <DATA> the type of this object's data object
 * @param <CHILD_DATA> the type of this object's children's data objects
 * @param <CHILD> the type of this object's children objects
 * @param <LISTENER> the type of this object's listener
 */
public abstract class HousemateObject<
            RESOURCES extends Resources,
            DATA extends HousemateData<CHILD_DATA>,
            CHILD_DATA extends HousemateData<?>,
            CHILD extends HousemateObject<?, ? extends CHILD_DATA, ?, ?, ?>,
            LISTENER extends ObjectListener>
        extends com.intuso.utilities.object.Object<DATA, CHILD_DATA, CHILD, HousemateException>
        implements BaseObject<LISTENER> {

    public final static String CHILD_ADDED = "child-added";
    public final static String CHILD_REMOVED = "child-removed";
    public final static String LOAD_REQUEST = "load-request";
    public final static String LOAD_RESPONSE = "load-response";

    private final RESOURCES resources;

    private String path[];
    private final Listeners<LISTENER> objectListeners = new Listeners<LISTENER>();
    private final Map<String, Listeners<Receiver<?>>> messageListeners = Maps.newHashMap();
    private final List<ListenerRegistration> listenerRegistrations = Lists.newArrayList();

    /**
     * @param resources the resources
     * @param data the data object
     */
    protected HousemateObject(RESOURCES resources, DATA data) {
        super(data);
        this.resources = resources;
    }

    /**
     * Gets this object's description
     * @return this object's description
     */
    public final String getDescription() {
        return getData().getDescription();
    }

    /**
     * Gets this object's name
     * @return this object's name
     */
    public final String getName() {
        return getData().getName();
    }

    /**
     * Gets this object's resources
     * @return this object's resources
     */
    public final RESOURCES getResources() {
        return resources;
    }

    /**
     * Gets this object's log instance
     * @return this object's log instance
     */
    protected final Log getLog() {
        return resources.getLog();
    }

    /**
     * Gets this object's path
     * @return this object's path
     */
    public final String[] getPath() {
        return path;
    }

    /**
     * Gets this object's listeners
     * @return this object's listeners
     */
    public Set<LISTENER> getObjectListeners() {
        return objectListeners.getListeners();
    }

    /**
     * Adds a listener to this object
     * @param listener the listener to add
     * @return the listener registration
     */
    public ListenerRegistration addObjectListener(LISTENER listener) {
        return objectListeners.addListener(listener);
    }

    /**
     * Add a message listener to this object
     * @param type the type of the message to listen for
     * @param listener the listener
     * @return the listener registration
     */
    protected ListenerRegistration addMessageListener(String type, Receiver listener) {
        Listeners<Receiver<?>> listeners = messageListeners.get(type);
        if(listeners == null) {
            listeners = new Listeners<Receiver<?>>();
            messageListeners.put(type, listeners);
        }
        return listeners.addListener(listener);
    }

    /**
     * Distribute a message to this object or its children
     * @param message the message to distribute
     * @throws HousemateException if the message could not be distributed, or an error was thrown when processing it
     */
    public final void distributeMessage(Message<?> message) throws HousemateException {
        if(message.getPath().length == path.length) {
            Listeners<Receiver<?>> listeners = messageListeners.get(message.getType());
            if(listeners == null)
                throw new HousemateException("No listeners known for type \"" + message.getType() + "\" for object " + Arrays.toString(path));
            for(Receiver listener : listeners)
                listener.messageReceived(message);
        } else if(message.getPath().length > path.length) {
            String childName = message.getPath()[path.length];
            HousemateObject<?, ?, ?, ?, ?> child = getChild(childName);
            if(child == null)
                throw new HousemateException("Unknown child \"" + childName + "\" at depth " + path.length + " of " + Arrays.toString(message.getPath()));
            child.distributeMessage(message);
        } else
            throw new HousemateException("Message received for path that is a parent of this element. It should not have got here! Oops!");
    }

    /**
     * Initialise this object
     * @param parent the object's parent
     */
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
        for(CHILD child : getChildren())
            child.init(this);

        initPostRecurseHook(parent);

        listenerRegistrations.addAll(registerListeners());
    }

    /**
     * Registers any listeners for the object
     * @return the list of listener registrations for the added listeners
     */
    protected List<ListenerRegistration> registerListeners() {
        return Lists.newArrayList();
    }

    /**
     * Hook for further intialisation of this object before initialisation recurses to its children
     * @param parent this object's parent object
     */
    protected void initPreRecurseHook(HousemateObject<?, ?, ?, ?, ?> parent) {}

    /**
     * Hook for further intialisation of this object after initialisation recurses to its children
     * @param parent this object's parent object
     */
    protected void initPostRecurseHook(HousemateObject<?, ?, ?, ?, ?> parent) {}

    /**
     * Uninitialise this object
     */
    public final void uninit() {
        for(ListenerRegistration listenerRegistration : listenerRegistrations)
            listenerRegistration.removeListener();
        listenerRegistrations.clear();
        for(CHILD child : getChildren())
            child.uninit();
    }

    /**
     * Gets the object at a path
     * @param path the path to get the object from
     * @return the object at that path, or null if none exists
     */
    public final HousemateObject<?, ?, ?, ?, ?> getObject(String[] path) {
        return getChild(path, this);
    }

    /**
     * Gets the object at a path relative to another object
     * @param path the path to get the object from
     * @param current the object the path is relative to
     * @return the object at the path relative to the other object, or null if none exists
     */
    public final static HousemateObject<?, ?, ?, ?, ?> getChild(String[] path, HousemateObject<?, ?, ?, ?, ?> current) {
        String[] currentPath = current.getPath();
        if(path.length < currentPath.length)
            throw new RuntimeException("Object requested is at a higher level than this object");
        else if(path.length == currentPath.length)
            return current;
        else {
            String childName = path[currentPath.length];
            HousemateObject<?, ?, ?, ?, ?> child = current.getChild(childName);
            if(child == null)
                return null;
            else
                return getChild(path, child);
        }
    }

    /**
     * Message payload for a load request of a remote object
     */
    protected static class LoadRequest implements Message.Payload {

        private String childId;

        private LoadRequest() {}

        /**
         * @param childId the id of the child object to load
         */
        public LoadRequest(String childId) {
            this.childId = childId;
        }

        /**
         * Gets the id of the child object to load
         * @return the id of the child object to load
         */
        public String getChildId() {
            return childId;
        }

        @Override
        public String toString() {
            return childId;
        }
    }

    /**
     * Message payload for a load request of a remote object
     */
    protected static class LoadResponse<DATA extends Data<?>> implements Message.Payload {

        private String childId;
        private DATA data;
        private List<ChildData> childData;
        private String error;

        private LoadResponse() {}

        /**
         * @param childId the id of the child object to load
         */
        public LoadResponse(String childId, DATA data, List<ChildData> childData) {
            this.childId = childId;
            this.data = data;
            this.childData = childData;
        }

        /**
         * @param childId the id of the child object to load
         */
        public LoadResponse(String childId, String error) {
            this.childId = childId;
            this.error = error;
        }

        /**
         * Gets the id of the child object to load
         * @return the id of the child object to load
         */
        public String getChildId() {
            return childId;
        }

        public DATA getData() {
            return data;
        }

        public List<ChildData> getChildData() {
            return childData;
        }

        public String getError() {
            return error;
        }

        @Override
        public String toString() {
            return data != null ? childId + " data" : childId + " failed because " + error;
        }
    }
}
