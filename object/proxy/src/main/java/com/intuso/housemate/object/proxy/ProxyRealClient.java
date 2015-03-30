package com.intuso.housemate.object.proxy;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.intuso.housemate.api.comms.message.NoPayload;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.realclient.RealClient;
import com.intuso.housemate.api.object.realclient.RealClientData;
import com.intuso.housemate.api.object.realclient.RealClientListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.BaseObject;
import com.intuso.utilities.object.ObjectListener;

import java.util.Map;

/**
 * @param <USER> the type of the users
 * @param <USERS> the type of the users list
 * @param <TYPE> the type of the types
 * @param <TYPES> the type of the types list
 * @param <DEVICE> the type of the devices
 * @param <DEVICES> the type of the devices list
 * @param <AUTOMATION> the type of the automations
 * @param <AUTOMATIONS> the type of the automations list
 * @param <COMMAND> the type of the command
 * @param <REAL_CLIENT> the type of the root
 */
public abstract class ProxyRealClient<
            APPLICATION extends ProxyApplication<?, ?, ?, ?, APPLICATION>,
            APPLICATIONS extends ProxyList<?, APPLICATION, APPLICATIONS>,
            AUTOMATION extends ProxyAutomation<?, ?, ?, ?, ?, ?, ?>,
            AUTOMATIONS extends ProxyList<?, AUTOMATION, AUTOMATIONS>,
            DEVICE extends ProxyDevice<?, ?, ?, ?, ?, ?, ?, ?>,
            DEVICES extends ProxyList<?, DEVICE, DEVICES>,
            HARDWARE extends ProxyHardware<?, ?>,
            HARDWARES extends ProxyList<?, HARDWARE, HARDWARES>,
            TYPE extends ProxyType<?, ?, ?, ?>,
            TYPES extends ProxyList<?, TYPE, TYPES>,
            USER extends ProxyUser<?, ?, USER>,
            USERS extends ProxyList<?, USER, USERS>,
            COMMAND extends ProxyCommand<?, ?, ?, COMMAND>,
            REAL_CLIENT extends ProxyRealClient<APPLICATION, APPLICATIONS, AUTOMATION, AUTOMATIONS, DEVICE, DEVICES, HARDWARE, HARDWARES, TYPE, TYPES, USER, USERS, COMMAND, REAL_CLIENT>>
        extends ProxyObject<RealClientData, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>, REAL_CLIENT, RealClientListener<? super REAL_CLIENT>>
        implements RealClient<APPLICATIONS, AUTOMATIONS, DEVICES, HARDWARES, TYPES, USERS, COMMAND, REAL_CLIENT>,
            ObjectListener<ProxyObject<?, ?, ?, ?, ?>> {

    public final static String APPLICATIONS_ID = "applications";
    public final static String USERS_ID = "users";
    public final static String HARDWARES_ID = "hardwares";
    public final static String TYPES_ID = "types";
    public final static String DEVICES_ID = "devices";
    public final static String AUTOMATIONS_ID = "automations";
    public final static String ADD_USER_ID = "add-user";
    public final static String ADD_HARDWARE_ID = "add-hardware";
    public final static String ADD_DEVICE_ID = "add-device";
    public final static String ADD_AUTOMATION_ID = "add-automation";

    private final Map<String, Listeners<ObjectLifecycleListener>> objectLifecycleListeners = Maps.newHashMap();

    public ProxyRealClient(Log log, ListenersFactory listenersFactory, RealClientData data) {
        super(log, listenersFactory, data);
    }

    @Override
    public APPLICATIONS getApplications() {
        return (APPLICATIONS) getChild(APPLICATIONS_ID);
    }

    @Override
    public USERS getUsers() {
        return (USERS) getChild(USERS_ID);
    }

    @Override
    public HARDWARES getHardwares() {
        return (HARDWARES) getChild(HARDWARES_ID);
    }

    @Override
    public TYPES getTypes() {
        return (TYPES) getChild(TYPES_ID);
    }

    @Override
    public DEVICES getDevices() {
        return (DEVICES) getChild(DEVICES_ID);
    }

    @Override
    public AUTOMATIONS getAutomations() {
        return (AUTOMATIONS) getChild(AUTOMATIONS_ID);
    }

    public COMMAND getAddUserCommand() {
        return (COMMAND) getChild(ADD_USER_ID);
    }

    public COMMAND getAddHardwareCommand() {
        return (COMMAND) getChild(ADD_HARDWARE_ID);
    }

    public COMMAND getAddDeviceCommand() {
        return (COMMAND) getChild(ADD_DEVICE_ID);
    }

    public COMMAND getAddAutomationCommand() {
        return (COMMAND) getChild(ADD_AUTOMATION_ID);
    }

    @Override
    public void childObjectAdded(String childName, ProxyObject<?, ?, ?, ?, ?> child) {
        // do nothing
    }

    @Override
    public void childObjectRemoved(String childName, ProxyObject<?, ?, ?, ?, ?> child) {
        // do nothing
    }

    @Override
    public void ancestorObjectAdded(String ancestorPath, BaseObject<?, ?, ?, ?> ancestor) {
        if(ancestor instanceof HousemateObject)
            objectAdded(ancestorPath, (HousemateObject<?, ?, ?, ?>) ancestor);
    }

    /**
     * Notifies that an object was added
     * @param path the path of the object
     * @param object the object
     */
    private void objectAdded(String path, HousemateObject<?, ?, ?, ?> object) {
        if(objectLifecycleListeners.get(path) != null) {
            String splitPath[] = path.split(PATH_SEPARATOR);
            for(ObjectLifecycleListener listener : objectLifecycleListeners.get(path))
                listener.objectCreated(splitPath, object);
        }
        for(HousemateObject<?, ?, ?, ?> child : object.getChildren())
            objectAdded(path + PATH_SEPARATOR + child.getId(), child);
    }

    @Override
    public void ancestorObjectRemoved(String ancestorPath, BaseObject<?, ?, ?, ?> ancestor) {
        if(ancestor instanceof HousemateObject)
            objectRemoved(ancestorPath, (HousemateObject<?, ?, ?, ?>) ancestor);
    }

    /**
     * Notifies that an object was removed
     * @param path the path of the object
     * @param object the object
     */
    private void objectRemoved(String path, HousemateObject<?, ?, ?, ?> object) {
        if(objectLifecycleListeners.get(path) != null) {
            String splitPath[] = path.split(PATH_SEPARATOR);
            for(ObjectLifecycleListener listener : objectLifecycleListeners.get(path))
                listener.objectRemoved(splitPath, object);
        }
        for(HousemateObject<?, ?, ?, ?> child : object.getChildren())
            objectRemoved(path + PATH_SEPARATOR + child.getId(), child);
    }

    public final ListenerRegistration addObjectLifecycleListener(String[] ancestorPath, ObjectLifecycleListener listener) {
        String path = Joiner.on(PATH_SEPARATOR).join(ancestorPath);
        Listeners<ObjectLifecycleListener> listeners = objectLifecycleListeners.get(path);
        if(listeners == null) {
            listeners = getListenersFactory().create();
            objectLifecycleListeners.put(path, listeners);
        }
        return listeners.addListener(listener);
    }

    public void clearLoadedObjects() {
        sendMessage("clear-loaded", NoPayload.INSTANCE);
        // clone the set so we can edit it while we iterate it
        for(String childName : Sets.newHashSet(getChildNames()))
            removeChild(childName);
    }
}
