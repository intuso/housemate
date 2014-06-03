package com.intuso.housemate.api.object;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
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
public abstract class HousemateObject<
            DATA extends HousemateData<CHILD_DATA>,
            CHILD_DATA extends HousemateData<?>,
            CHILD extends HousemateObject<? extends CHILD_DATA, ?, ?, ?>,
            LISTENER extends ObjectListener>
        extends BaseObject<DATA, CHILD_DATA, CHILD, HousemateException>
        implements BaseHousemateObject<LISTENER> {

    public final static String EVERYTHING = "*";
    public final static String EVERYTHING_RECURSIVE = "**";

    public final static String LOAD_REQUEST = "load-request";
    public final static String LOAD_RESPONSE = "load-response";
    public final static String CHILD_OVERVIEWS_REQUEST = "child-overviews-request";
    public final static String CHILD_OVERVIEWS_RESPONSE = "child-overviews-response";
    public final static String CHILD_ADDED = "child-added";
    public final static String CHILD_REMOVED = "child-removed";

    private final Log log;
    private final ListenersFactory listenersFactory;

    private String path[];
    private final Listeners<LISTENER> objectListeners;
    private final Map<String, Listeners<Receiver<?>>> messageListeners = Maps.newHashMap();
    private final List<ListenerRegistration> listenerRegistrations = Lists.newArrayList();

    /**
     * @param data the data object
     */
    protected HousemateObject(Log log, ListenersFactory listenersFactory, DATA data) {
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
    protected ListenerRegistration addMessageListener(String type, Receiver listener) {
        Listeners<Receiver<?>> listeners = messageListeners.get(type);
        if(listeners == null) {
            listeners = listenersFactory.create();
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
        if(path == null) {
            getLog().e("Cannot receive message when not a registered object");
            return;
        }
        if(message.getPath().length == path.length) {
            Listeners<Receiver<?>> listeners = messageListeners.get(message.getType());
            if(listeners == null)
                throw new HousemateException("No listeners known for type \"" + message.getType() + "\" for object " + Arrays.toString(path));
            for(Receiver listener : listeners)
                listener.messageReceived(message);
        } else if(message.getPath().length > path.length) {
            String childName = message.getPath()[path.length];
            HousemateObject<?, ?, ?, ?> child = getChild(childName);
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
    public final void init(HousemateObject<?, ?, ?, ?> parent) {

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

    protected void addListenerRegistration(ListenerRegistration listenerRegistration) {
        listenerRegistrations.add(listenerRegistration);
    }

    /**
     * Hook for further intialisation of this object before initialisation recurses to its children
     * @param parent this object's parent object
     */
    protected void initPreRecurseHook(HousemateObject<?, ?, ?, ?> parent) {}

    /**
     * Hook for further intialisation of this object after initialisation recurses to its children
     * @param parent this object's parent object
     */
    protected void initPostRecurseHook(HousemateObject<?, ?, ?, ?> parent) {}

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
    public final HousemateObject<?, ?, ?, ?> getObject(String[] path) {
        return getChild(this, path);
    }

    /**
     * Gets the object at a path relative to another object
     *
     * @param current the object the path is relative to
     * @param path the path to get the object from
     * @return the object at the path relative to the other object, or null if none exists
     */
    public final static HousemateObject<?, ?, ?, ?> getChild(HousemateObject<?, ?, ?, ?> current, String[] path) {
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
    public final static HousemateObject<?, ?, ?, ?> getChild(HousemateObject<?, ?, ?, ?> current, String[] path, int depth) {
        if(depth >= path.length)
            return current;
        HousemateObject<?, ?, ?, ?> next = current.getChild(path[depth]);
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
            this.childOverviews = childOverviews == null || childOverviews instanceof Serializable ? childOverviews : Lists.newArrayList(childOverviews);
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
                childOverviews = Lists.newArrayList(childOverviews);
        }
    }

    /**
     * Container class for data about what objects a client wants to load
     */
    public static class TreeLoadInfo implements Message.Payload {

        private static final long serialVersionUID = -1L;

        private String id;
        private boolean load;
        private Map<String, TreeLoadInfo> children;

        public static TreeLoadInfo create(String ... path) {
            return create(path, null);
        }

        public static TreeLoadInfo create(String[] path, String ending) {
            if(ending == null) {
                if(path == null)
                    throw new HousemateRuntimeException("Null path to load");
                else if(path.length == 0)
                    throw new HousemateRuntimeException("Empty path to load");
                final HousemateObject.TreeLoadInfo root = new HousemateObject.TreeLoadInfo(path[0]);
                HousemateObject.TreeLoadInfo current = root;
                for(int i = 1; i < path.length; i++) {
                    HousemateObject.TreeLoadInfo child = new HousemateObject.TreeLoadInfo(path[i]);
                    current.getChildren().put(path[i], child);
                    current = child;
                }
                return root;
            } else {
                if(path == null || path.length == 0)
                    return new HousemateObject.TreeLoadInfo(ending);
                else {
                    final HousemateObject.TreeLoadInfo root = new HousemateObject.TreeLoadInfo(path[0]);
                    HousemateObject.TreeLoadInfo current = root;
                    for(int i = 1; i < path.length; i++) {
                        HousemateObject.TreeLoadInfo child = new HousemateObject.TreeLoadInfo(path[i]);
                        current.getChildren().put(path[i], child);
                        current = child;
                    }
                    HousemateObject.TreeLoadInfo child = new HousemateObject.TreeLoadInfo(ending);
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
            Map<String, TreeLoadInfo> result = Maps.newHashMap();
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
         * Is there data to load
         * @return true if there is data to load
         */
        public boolean isLoad() {
            return load;
        }

        /**
         * Set whether there is data to load
         * @param load true if there is data to load
         */
        public void setLoad(boolean load) {
            this.load = load;
        }

        /**
         * Get the load info for child objects
         * @return the load info for child objects
         */
        public Map<String, TreeLoadInfo> getChildren() {
            return children;
        }

        public void setChildren(Map<String, TreeLoadInfo> children) {
            this.children = children == null || children instanceof Serializable ? children : Maps.newHashMap(children);
        }

        @Override
        public void ensureSerialisable() {
            if(children != null && !(children instanceof HashMap))
                children = Maps.newHashMap(children);
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
        private Map<String, ChildOverview> childData;

        public TreeData() {}

        public TreeData(String id, HousemateData<?> data, Map<String, TreeData> children, Map<String, ChildOverview> childData) {
            this.id = id;
            this.data = data;
            this.children = children;
            this.childData = childData;
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
            this.children = children == null || children instanceof Serializable ? children : Maps.newHashMap(children);
        }

        /**
         * Get the overviews of other unloaded children
         * @return the overviews of other unloaded children
         */
        public Map<String, ChildOverview> getChildData() {
            return childData;
        }

        public void setChildData(Map<String, ChildOverview> childData) {
            this.childData = childData == null || childData instanceof Serializable ? childData : Maps.newHashMap(childData);
        }

        @Override
        public void ensureSerialisable() {
            if(children != null && !(children instanceof HashMap))
                children = Maps.newHashMap(children);
            if(childData != null && !(childData instanceof HashMap))
                childData = Maps.newHashMap(childData);
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

        private String loaderName;
        private TreeLoadInfo loadInfo;

        public LoadRequest() {}

        /**
         * @param loadInfo the id of the child object to load
         */
        public LoadRequest(String loaderName, TreeLoadInfo loadInfo) {
            this.loaderName = loaderName;
            this.loadInfo = loadInfo;
        }

        /**
         * Get the loader name
         * @return the loader name
         */
        public String getLoaderName() {
            return loaderName;
        }

        public void setLoaderName(String loaderName) {
            this.loaderName = loaderName;
        }

        /**
         * Gets the id of the child object to load
         * @return the id of the child object to load
         */
        public TreeLoadInfo getLoadInfo() {
            return loadInfo;
        }

        public void setLoadInfo(TreeLoadInfo loadInfo) {
            this.loadInfo = loadInfo;
        }

        @Override
        public String toString() {
            return loaderName + " " + loadInfo.getId();
        }

        @Override
        public void ensureSerialisable() {
            if(loadInfo != null)
                loadInfo.ensureSerialisable();
        }
    }

    /**
     * Message payload for a load response of a remote object
     */
    public static class LoadResponse implements Message.Payload {

        private static final long serialVersionUID = -1L;

        private String loaderName;
        private TreeData treeData;
        private String error;

        public LoadResponse() {}

        public LoadResponse(String loaderName, TreeData treeData) {
            this(loaderName, treeData, null);
        }

        public LoadResponse(String loaderName, TreeData treeData, String error) {
            this.loaderName = loaderName;
            this.treeData = treeData;
            this.error = error;
        }

        /**
         * Get the loader name
         * @return the loader name
         */
        public String getLoaderName() {
            return loaderName;
        }

        public void setLoaderName(String loaderName) {
            this.loaderName = loaderName;
        }

        /**
         * Get the loaded object's data
         * @return the loaded object's data
         */
        public TreeData getTreeData() {
            return treeData;
        }

        public void setTreeData(TreeData treeData) {
            this.treeData = treeData;
        }

        /**
         * Get the error that occurred
         * @return the error that occurred
         */
        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        @Override
        public String toString() {
            return loaderName + " tree " + treeData.getId() + " " + (treeData.getData() != null ? "data" : (error != null ? "failed because " + error : ""));
        }

        @Override
        public void ensureSerialisable() {
            if(treeData != null)
                treeData.ensureSerialisable();
        }
    }
}
