package com.intuso.housemate.comms.api.internal;

import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.object.api.internal.BaseHousemateObject;
import com.intuso.housemate.object.api.internal.ObjectListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.BaseObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Base class for all Housemate object implementations
 *
 * @param <DATA> the type of this object's data object
 * @param <CHILD_DATA> the type of this object's children's data objects
 * @param <CHILD> the type of this object's children objects
 * @param <LISTENER> the type of this object's listener
 */
public abstract class RemoteObject<
        DATA extends HousemateData<CHILD_DATA>,
        CHILD_DATA extends HousemateData<?>,
        CHILD extends RemoteObject<? extends CHILD_DATA, ?, ?, ?>,
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
    private final MessageDistributor messageDistributor;
    private final Listeners<LISTENER> objectListeners;
    private final List<ListenerRegistration> listenerRegistrations = new ArrayList<>();

    /**
     * @param data the data object
     */
    protected RemoteObject(Log log, ListenersFactory listenersFactory, DATA data) {
        super(listenersFactory, data);
        this.log = log;
        this.listenersFactory = listenersFactory;
        this.messageDistributor = new MessageDistributor(listenersFactory);
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
        return messageDistributor.registerReceiver(type, listener);
    }

    /**
     * Distribute a message to this object or its children
     * @param message the message to distribute
     * @throws HousemateCommsException if the message could not be distributed, or an error was thrown when processing it
     */
    public final void distributeMessage(Message<Message.Payload> message) throws HousemateCommsException {
        if(path == null) {
            getLog().e("Cannot receive message when not a registered object");
            return;
        }
        if(message.getPath().length == path.length)
            messageDistributor.messageReceived(message);
        else if(message.getPath().length > path.length) {
            String childName = message.getPath()[path.length];
            RemoteObject<?, ?, ?, ?> child = getChild(childName);
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
    public final void init(RemoteObject<?, ?, ?, ?> parent) {

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
    protected void initPreRecurseHook(RemoteObject<?, ?, ?, ?> parent) {}

    /**
     * Hook for further intialisation of this object after initialisation recurses to its children
     * @param parent this object's parent object
     */
    protected void initPostRecurseHook(RemoteObject<?, ?, ?, ?> parent) {}

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
    public final RemoteObject<?, ?, ?, ?> getObject(String[] path) {
        return getChild(this, path);
    }

    /**
     * Gets the object at a path relative to another object
     *
     * @param current the object the path is relative to
     * @param path the path to get the object from
     * @return the object at the path relative to the other object, or null if none exists
     */
    public final static RemoteObject<?, ?, ?, ?> getChild(RemoteObject<?, ?, ?, ?> current, String[] path) {
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
    public final static RemoteObject<?, ?, ?, ?> getChild(RemoteObject<?, ?, ?, ?> current, String[] path, int depth) {
        if(depth >= path.length)
            return current;
        RemoteObject<?, ?, ?, ?> next = current.getChild(path[depth]);
        if(next == null)
            return null;
        return getChild(next, path, depth + 1);
    }

}
