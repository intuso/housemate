package com.intuso.housemate.comms.api.internal;

import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.object.api.internal.BaseHousemateObject;
import com.intuso.housemate.object.api.internal.ObjectListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.BaseObject;

import java.io.Serializable;
import java.util.*;

/**
 * Base class for all Housemate object implementations
 *
 * @param <DATA> the type of this object's data object
 * @param <CHILD_DATA> the type of this object's children's data objects
 * @param <CHILD> the type of this object's children objects
 * @param <LISTENER> the type of this object's listener
 */
public abstract class RemoteLinkedObject<
            DATA extends HousemateData<CHILD_DATA>,
            CHILD_DATA extends HousemateData<?>,
            CHILD extends RemoteLinkedObject<? extends CHILD_DATA, ?, ?, ?>,
            LISTENER extends ObjectListener>
        extends BaseObject<DATA, CHILD_DATA, CHILD>
        implements BaseHousemateObject<LISTENER> {

    public final static String EVERYTHING = "*";
    public final static String EVERYTHING_RECURSIVE = "**";

    public final static String ADD_TYPE = "add";
    public final static String REMOVE_TYPE = "remove";
    public final static String LOAD_REQUEST = "load-request";
    public final static String LOAD_RESPONSE = "load-response-part";
    public final static String LOAD_FINISHED = "load-response-finished";
    public final static String CHILD_OVERVIEWS_REQUEST = "child-overviews-request";
    public final static String CHILD_OVERVIEWS_RESPONSE = "child-overviews-response";
    public final static String CHILD_ADDED = "child-added";
    public final static String CHILD_REMOVED = "child-removed";

    private final Log log;
    private final ListenersFactory listenersFactory;

    private String path[];
    private final Listeners<LISTENER> objectListeners;
    private final Map<String, Listeners<Message.Receiver<?>>> messageListeners = new HashMap<>();
    private final List<ListenerRegistration> listenerRegistrations = new ArrayList<>();

    /**
     * @param data the data object
     */
    protected RemoteLinkedObject(Log log, ListenersFactory listenersFactory, DATA data) {
        super(listenersFactory, data);
        this.log = log;
        this.listenersFactory = listenersFactory;
        this.objectListeners = listenersFactory.create();
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
     * Gets this object's log instance
     * @return this object's log instance
     */
    public final Log getLog() {
        return log;
    }

    public final ListenersFactory getListenersFactory() {
        return listenersFactory;
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
    public Iterable<LISTENER> getObjectListeners() {
        return objectListeners;
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
    protected ListenerRegistration addMessageListener(String type, Message.Receiver<?> listener) {
        Listeners<Message.Receiver<?>> listeners = messageListeners.get(type);
        if(listeners == null) {
            listeners = listenersFactory.create();
            messageListeners.put(type, listeners);
        }
        return listeners.addListener(listener);
    }

    /**
     * Distribute a message to this object or its children
     * @param message the message to distribute
     * @throws HousemateCommsException if the message could not be distributed, or an error was thrown when processing it
     */
    public final void distributeMessage(Message<?> message) throws HousemateCommsException {
        if(path == null) {
            getLog().e("Cannot receive message when not a registered object");
            return;
        }
        if(message.getPath().length == path.length) {
            Listeners<Message.Receiver<?>> listeners = messageListeners.get(message.getType());
            if(listeners == null)
                throw new HousemateCommsException("No listeners known for type \"" + message.getType() + "\" for object " + Arrays.toString(path));
            for(Message.Receiver listener : listeners)
                listener.messageReceived(message);
        } else if(message.getPath().length > path.length) {
            String childName = message.getPath()[path.length];
            RemoteLinkedObject<?, ?, ?, ?> child = getChild(childName);
            if(child == null)
                throw new HousemateCommsException("Unknown child \"" + childName + "\" at depth " + path.length + " of " + Arrays.toString(message.getPath()));
            child.distributeMessage(message);
        } else
            throw new HousemateCommsException("Message received for path that is a parent of this element. It should not have got here! Oops!");
    }

    /**
     * Initialise this object
     * @param parent the object's parent
     */
    public final void init(RemoteLinkedObject<?, ?, ?, ?> parent) {

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
        return new ArrayList<>();
    }

    protected void addListenerRegistration(ListenerRegistration listenerRegistration) {
        listenerRegistrations.add(listenerRegistration);
    }

    /**
     * Hook for further intialisation of this object before initialisation recurses to its children
     * @param parent this object's parent object
     */
    protected void initPreRecurseHook(RemoteLinkedObject<?, ?, ?, ?> parent) {}

    /**
     * Hook for further intialisation of this object after initialisation recurses to its children
     * @param parent this object's parent object
     */
    protected void initPostRecurseHook(RemoteLinkedObject<?, ?, ?, ?> parent) {}

    /**
     * Uninitialise this object
     */
    public final void uninit() {
        for(ListenerRegistration listenerRegistration : listenerRegistrations)
            listenerRegistration.removeListener();
        listenerRegistrations.clear();
        for(CHILD child : getChildren())
            child.uninit();
        getChildren().clear();
    }

    /**
     * Gets the object at a path
     * @param path the path to get the object from
     * @return the object at that path, or null if none exists
     */
    public final RemoteLinkedObject<?, ?, ?, ?> getObject(String[] path) {
        return getChild(this, path);
    }

    /**
     * Gets the object at a path relative to another object
     *
     * @param current the object the path is relative to
     * @param path the path to get the object from
     * @return the object at the path relative to the other object, or null if none exists
     */
    public final static RemoteLinkedObject<?, ?, ?, ?> getChild(RemoteLinkedObject<?, ?, ?, ?> current, String[] path) {
        return getChild(current, path, 0);
    }

    /**
     * Gets the object at a path relative to another object
     *
     * @param current the object the path is relative to
     * @param path the path to get the object from
     * @param depth the current index in the path
     * @return the object at the path relative to the other object, or null if none exists
     */
    public final static RemoteLinkedObject<?, ?, ?, ?> getChild(RemoteLinkedObject<?, ?, ?, ?> current, String[] path, int depth) {
        if(depth >= path.length)
            return current;
        RemoteLinkedObject<?, ?, ?, ?> next = current.getChild(path[depth]);
        if(next == null)
            return null;
        return getChild(next, path, depth + 1);
    }

    public static class ChildOverviews implements Message.Payload {

        private List<ChildOverview> childOverviews;
        private String error;

        public ChildOverviews() {}

        public ChildOverviews(List<ChildOverview> childOverviews) {
            this(childOverviews, null);
        }

        public ChildOverviews(String error) {
            this(null, error);
        }

        public ChildOverviews(List<ChildOverview> childOverviews, String error) {
            this.childOverviews = childOverviews;
            this.error = error;
        }

        public List<ChildOverview> getChildOverviews() {
            return childOverviews;
        }

        public void setChildOverviews(List<ChildOverview> childOverviews) {
            this.childOverviews = childOverviews == null || childOverviews instanceof Serializable ? childOverviews : new ArrayList<>(childOverviews);
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        @Override
        public void ensureSerialisable() {
            if(childOverviews != null && !(childOverviews instanceof ArrayList))
                childOverviews = new ArrayList<>(childOverviews);
        }
    }

    /**
     * Container class for data about what objects a client wants to load
     */
    public static class TreeLoadInfo implements Message.Payload {

        private static final long serialVersionUID = -1L;

        private String id;
        private Map<String, TreeLoadInfo> children;

        public static TreeLoadInfo create(String ... path) {
            return create(path, null);
        }

        public static TreeLoadInfo create(String[] path, String ending) {
            if(ending == null) {
                if(path == null)
                    throw new HousemateCommsException("Null path to load");
                else if(path.length == 0)
                    throw new HousemateCommsException("Empty path to load");
                final RemoteLinkedObject.TreeLoadInfo root = new RemoteLinkedObject.TreeLoadInfo(path[0]);
                RemoteLinkedObject.TreeLoadInfo current = root;
                for(int i = 1; i < path.length; i++) {
                    RemoteLinkedObject.TreeLoadInfo child = new RemoteLinkedObject.TreeLoadInfo(path[i]);
                    current.getChildren().put(path[i], child);
                    current = child;
                }
                return root;
            } else {
                if(path == null || path.length == 0)
                    return new RemoteLinkedObject.TreeLoadInfo(ending);
                else {
                    final RemoteLinkedObject.TreeLoadInfo root = new RemoteLinkedObject.TreeLoadInfo(path[0]);
                    RemoteLinkedObject.TreeLoadInfo current = root;
                    for(int i = 1; i < path.length; i++) {
                        RemoteLinkedObject.TreeLoadInfo child = new RemoteLinkedObject.TreeLoadInfo(path[i]);
                        current.getChildren().put(path[i], child);
                        current = child;
                    }
                    RemoteLinkedObject.TreeLoadInfo child = new RemoteLinkedObject.TreeLoadInfo(ending);
                    current.getChildren().put(ending, child);
                    return root;
                }
            }
        }

        public TreeLoadInfo() {}

        public TreeLoadInfo(String id, TreeLoadInfo ... children) {
            this(id, asMap(children));
        }

        private static Map<String, TreeLoadInfo> asMap(TreeLoadInfo ... children) {
            Map<String, TreeLoadInfo> result = new HashMap<>();
            for(TreeLoadInfo child : children)
                result.put(child.getId(), child);
            return result;
        }

        public TreeLoadInfo(String id, Map<String, TreeLoadInfo> children) {
            this.id = id;
            this.children = children;
        }

        /**
         * Get the id of the object to load
         * @return the id of the object to load
         */
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        /**
         * Get the load info for child objects
         * @return the load info for child objects
         */
        public Map<String, TreeLoadInfo> getChildren() {
            return children;
        }

        public void setChildren(Map<String, TreeLoadInfo> children) {
            this.children = children == null || children instanceof Serializable ? children : new HashMap<>(children);
        }

        @Override
        public void ensureSerialisable() {
            if(children != null && !(children instanceof HashMap))
                children = new HashMap<>(children);
            if(children != null)
                for(TreeLoadInfo child : children.values())
                    child.ensureSerialisable();
        }
    }

    /**
     * Container class for loaded objects
     */
    public static class TreeData implements Message.Payload {

        private static final long serialVersionUID = -1L;

        private String id;
        private HousemateData<?> data;
        private Map<String, TreeData> children;
        private Map<String, ChildOverview> childOverviews;

        public TreeData() {}

        public TreeData(String id, HousemateData<?> data, Map<String, TreeData> children, Map<String, ChildOverview> childOverviews) {
            this.id = id;
            this.data = data;
            this.children = children;
            this.childOverviews = childOverviews;
        }

        /**
         * Get the id of the loaded object
         * @return the id of the loaded object
         */
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        /**
         * Get the object data
         * @return the object data
         */
        public HousemateData<?> getData() {
            return data;
        }

        public void setData(HousemateData<?> data) {
            this.data = data;
        }

        /**
         * Get the children object data
         * @return the children object data
         */
        public Map<String, TreeData> getChildren() {
            return children;
        }

        public void setChildren(Map<String, TreeData> children) {
            this.children = children == null || children instanceof Serializable ? children : new HashMap<>(children);
        }

        /**
         * Get the overviews of other unloaded children
         * @return the overviews of other unloaded children
         */
        public Map<String, ChildOverview> getChildOverviews() {
            return childOverviews;
        }

        public void setChildOverviews(Map<String, ChildOverview> childOverviews) {
            this.childOverviews = childOverviews == null || childOverviews instanceof Serializable ? childOverviews : new HashMap<>(childOverviews);
        }

        @Override
        public void ensureSerialisable() {
            if(children != null && !(children instanceof HashMap))
                children = new HashMap<>(children);
            if(childOverviews != null && !(childOverviews instanceof HashMap))
                childOverviews = new HashMap<>(childOverviews);
            if(data != null)
                data.ensureSerialisable();
            if(children != null)
                for(TreeData child : children.values())
                    child.ensureSerialisable();
        }
    }

    /**
     * Message payload for a load request of a remote object
     */
    public static class LoadRequest implements Message.Payload {

        private static final long serialVersionUID = -1L;

        private String loaderId;
        private List<TreeLoadInfo> treeLoadInfos;

        public LoadRequest() {}

        /**
         * @param treeLoadInfos the id of the child object to load
         */
        public LoadRequest(String loaderId, List<TreeLoadInfo> treeLoadInfos) {
            this.loaderId = loaderId;
            this.treeLoadInfos = treeLoadInfos;
        }

        /**
         * Get the loader name
         * @return the loader name
         */
        public String getLoaderId() {
            return loaderId;
        }

        public void setLoaderId(String loaderId) {
            this.loaderId = loaderId;
        }

        /**
         * Gets the id of the child object to load
         * @return the id of the child object to load
         */
        public List<TreeLoadInfo> getTreeLoadInfos() {
            return treeLoadInfos;
        }

        public void setTreeLoadInfos(List<TreeLoadInfo> treeLoadInfos) {
            this.treeLoadInfos = treeLoadInfos;
        }

        @Override
        public String toString() {
            return loaderId;
        }

        @Override
        public void ensureSerialisable() {
            if(treeLoadInfos != null && !(treeLoadInfos instanceof ArrayList))
                treeLoadInfos = new ArrayList<>(treeLoadInfos);
            if(treeLoadInfos != null)
                for(TreeLoadInfo treeLoadInfo : treeLoadInfos)
                    treeLoadInfo.ensureSerialisable();
        }
    }

    /**
     * Message payload for a load response of a remote object
     */
    public static class LoadFinished implements Message.Payload {

        private static final long serialVersionUID = -1L;

        private String loaderId;
        private List<String> errors;

        public LoadFinished() {}

        public static LoadFinished forSuccess(String loaderName) {
            return new LoadFinished(loaderName, null);
        }

        public static LoadFinished forErrors(String loaderName, String ... errors) {
            return forErrors(loaderName, Arrays.asList(errors));
        }

        public static LoadFinished forErrors(String loaderName, List<String> errors) {
            return new LoadFinished(loaderName, errors);
        }

        private LoadFinished(String loaderId, List<String> errors) {
            this.loaderId = loaderId;
            this.errors = errors;
        }

        /**
         * Get the loader name
         * @return the loader name
         */
        public String getLoaderId() {
            return loaderId;
        }

        public void setLoaderId(String loaderId) {
            this.loaderId = loaderId;
        }

        /**
         * Get the error that occurred
         * @return the error that occurred
         */
        public List<String> getErrors() {
            return errors;
        }

        public void setErrors(List<String> errors) {
            this.errors = errors;
        }

        @Override
        public String toString() {
            return loaderId + " load finished" + (errors != null ? " and failed because of " + errors.size() + " errors" : "");
        }

        @Override
        public void ensureSerialisable() {
            if(errors != null && !(errors instanceof Serializable))
                errors = new ArrayList<>(errors);
        }
    }
}
