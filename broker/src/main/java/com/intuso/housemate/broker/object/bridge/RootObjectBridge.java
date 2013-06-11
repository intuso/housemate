package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Joiner;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.device.DeviceWrappable;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.root.RootWrappable;
import com.intuso.housemate.api.object.rule.RuleWrappable;
import com.intuso.housemate.api.object.type.TypeWrappable;
import com.intuso.housemate.api.object.user.UserWrappable;
import com.intuso.housemate.object.broker.proxy.BrokerProxyDevice;
import com.intuso.housemate.object.broker.proxy.BrokerProxyType;
import com.intuso.housemate.object.broker.real.BrokerRealRule;
import com.intuso.housemate.object.broker.real.BrokerRealUser;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.wrapper.Wrapper;
import com.intuso.utilities.wrapper.WrapperListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 12/03/13
 * Time: 18:31
 * To change this template use File | Settings | File Templates.
 */
public class RootObjectBridge
        extends BridgeObject<RootWrappable, HousemateObjectWrappable<?>, BridgeObject<?, ?, ?, ?, ?>,
            RootObjectBridge, RootListener<? super RootObjectBridge>>
        implements Root<RootObjectBridge, RootListener<? super RootObjectBridge>>, WrapperListener<HousemateObject<?, ?, ?, ?, ?>> {

    private ListBridge<UserWrappable, BrokerRealUser, UserBridge> users;
    private ListBridge<TypeWrappable<?>, BrokerProxyType, TypeBridge> types;
    private ListBridge<DeviceWrappable, BrokerProxyDevice, DeviceBridge> devices;
    private ListBridge<RuleWrappable, BrokerRealRule, RuleBridge> rules;
    private CommandBridge addUser;
    private CommandBridge addDevice;
    private CommandBridge addRule;

    private final Map<String, Listeners<ObjectLifecycleListener>> objectLifecycleListeners = new HashMap<String, Listeners<ObjectLifecycleListener>>();

    public RootObjectBridge(final BrokerBridgeResources resources) {
        super(resources, new RootWrappable());
        resources.setRoot(this);
        users = new ListBridge<UserWrappable, BrokerRealUser, UserBridge>(resources,
                resources.getGeneralResources().getRealResources().getRoot().getUsers(),
                new UserBridge.Converter(resources));
        types = new ListBridge<TypeWrappable<?>, BrokerProxyType, TypeBridge>(resources,
                resources.getGeneralResources().getProxyResources().getRoot().getTypes(),
                new TypeBridge.Converter(resources));
        devices = new ListBridge<DeviceWrappable, BrokerProxyDevice, DeviceBridge>(resources,
                resources.getGeneralResources().getProxyResources().getRoot().getDevices(),
                new DeviceBridge.Converter(resources));
        rules = new ListBridge<RuleWrappable, BrokerRealRule, RuleBridge>(resources,
                resources.getGeneralResources().getRealResources().getRoot().getRules(),
                new RuleBridge.Converter(resources));
        addUser = new CommandBridge(resources, resources.getGeneralResources().getRealResources().getRoot().getAddUserCommand());
        addDevice = new CommandBridge(resources, resources.getGeneralResources().getClient().getAddDeviceCommand());
        addRule = new CommandBridge(resources, resources.getGeneralResources().getRealResources().getRoot().getAddRuleCommand());
        addWrapper(users);
        addWrapper(types);
        addWrapper(devices);
        addWrapper(rules);
        addWrapper(addUser);
        addWrapper(addDevice);
        addWrapper(addRule);
        getResources().getGeneralResources().getStorage().watchDevices(devices);
        init(null);
    }

    @Override
    public Status getStatus() {
        return Status.Connected;
    }

    @Override
    public String getConnectionId() {
        return null;
    }

    @Override
    public void connect(AuthenticationMethod method) {
        throw new HousemateRuntimeException("Cannot connect this type of root object");
    }

    @Override
    public void disconnect() {
        throw new HousemateRuntimeException("Cannot disconnect this type of root object");
    }

    @Override
    public void messageReceived(Message<Message.Payload> message) throws HousemateException {
        distributeMessage(message);
    }

    @Override
    public void sendMessage(Message<?> message) {
        throw new HousemateRuntimeException("Whatever");
    }

    public ListBridge<UserWrappable, BrokerRealUser, UserBridge> getUsers() {
        return users;
    }

    public ListBridge<TypeWrappable<?>, BrokerProxyType, TypeBridge> getTypes() {
        return types;
    }

    public ListBridge<DeviceWrappable, BrokerProxyDevice, DeviceBridge> getDevices() {
        return devices;
    }

    public ListBridge<RuleWrappable, BrokerRealRule, RuleBridge> getRules() {
        return rules;
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addWrapperListener(this));
        return result;
    }

    @Override
    public void childWrapperAdded(String childName, HousemateObject<?, ?, ?, ?, ?> wrapper) {
        // do nothing
    }

    @Override
    public void childWrapperRemoved(String childName, HousemateObject<?, ?, ?, ?, ?> wrapper) {
        // do nothing
    }

    @Override
    public void ancestorAdded(String ancestorPath, Wrapper<?, ?, ?, ?> wrapper) {
        if(wrapper instanceof HousemateObject)
            objectWrapperAdded(ancestorPath, (HousemateObject<?, ?, ?, ?, ?>) wrapper);
    }

    private void objectWrapperAdded(String path, HousemateObject<?, ?, ?, ?, ?> objectWrapper) {
        if(objectLifecycleListeners.get(path) != null && objectLifecycleListeners.get(path).getListeners().size() > 0) {
            String splitPath[] = path.split(Wrapper.PATH_SEPARATOR);
            for(ObjectLifecycleListener listener : objectLifecycleListeners.get(path))
                listener.objectCreated(splitPath, objectWrapper);
        }
        for(HousemateObject<?, ?, ?, ?, ?> child : objectWrapper.getWrappers())
            objectWrapperAdded(path + Wrapper.PATH_SEPARATOR + child.getId(), child);
    }

    @Override
    public void ancestorRemoved(String ancestorPath, Wrapper<?, ?, ?, ?> wrapper) {
        if(wrapper instanceof HousemateObject)
            objectWrapperRemoved(ancestorPath, (HousemateObject<?, ?, ?, ?, ?>) wrapper);
    }

    private void objectWrapperRemoved(String path, HousemateObject<?, ?, ?, ?, ?> objectWrapper) {
        if(objectLifecycleListeners.get(path) != null && objectLifecycleListeners.get(path).getListeners().size() > 0) {
            String splitPath[] = path.split(Wrapper.PATH_SEPARATOR);
            for(ObjectLifecycleListener listener : objectLifecycleListeners.get(path))
                listener.objectRemoved(splitPath, objectWrapper);
        }
        for(HousemateObject<?, ?, ?, ?, ?> child : objectWrapper.getWrappers())
            objectWrapperRemoved(path + Wrapper.PATH_SEPARATOR + child.getId(), child);
    }

    public final ListenerRegistration addObjectLifecycleListener(String[] ancestorPath, ObjectLifecycleListener listener) {
        String path = Joiner.on(Wrapper.PATH_SEPARATOR).join(ancestorPath);
        Listeners<ObjectLifecycleListener> listeners = objectLifecycleListeners.get(path);
        if(listeners == null) {
            listeners = new Listeners<ObjectLifecycleListener>();
            objectLifecycleListeners.put(path, listeners);
        }
        return listeners.addListener(listener);
    }
}
