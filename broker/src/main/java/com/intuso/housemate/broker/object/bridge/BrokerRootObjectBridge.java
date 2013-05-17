package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.intuso.housemate.broker.object.proxy.BrokerProxyDevice;
import com.intuso.housemate.broker.object.proxy.BrokerProxyPrimaryObject;
import com.intuso.housemate.broker.object.proxy.BrokerProxyType;
import com.intuso.housemate.broker.object.real.BrokerRealRule;
import com.intuso.housemate.broker.object.real.BrokerRealUser;
import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.core.HousemateRuntimeException;
import com.intuso.housemate.core.authentication.AuthenticationMethod;
import com.intuso.housemate.core.authentication.AuthenticationResponseHandler;
import com.intuso.housemate.core.comms.Message;
import com.intuso.housemate.core.object.HousemateObject;
import com.intuso.housemate.core.object.HousemateObjectWrappable;
import com.intuso.housemate.core.object.ObjectLifecycleListener;
import com.intuso.housemate.core.object.device.Device;
import com.intuso.housemate.core.object.device.DeviceListener;
import com.intuso.housemate.core.object.device.DeviceWrappable;
import com.intuso.housemate.core.object.list.ListListener;
import com.intuso.housemate.core.object.root.Root;
import com.intuso.housemate.core.object.root.RootListener;
import com.intuso.housemate.core.object.root.RootWrappable;
import com.intuso.housemate.core.object.rule.RuleWrappable;
import com.intuso.housemate.core.object.type.TypeWrappable;
import com.intuso.housemate.core.object.user.UserWrappable;
import com.intuso.listeners.ListenerRegistration;
import com.intuso.listeners.Listeners;
import com.intuso.wrapper.Wrapper;
import com.intuso.wrapper.WrapperListener;

import javax.annotation.Nullable;
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
public class BrokerRootObjectBridge
        extends BridgeObject<RootWrappable, HousemateObjectWrappable<?>, BridgeObject<?, ?, ?, ?, ?>,
            BrokerRootObjectBridge, RootListener<? super BrokerRootObjectBridge>>
        implements Root<BrokerRootObjectBridge, RootListener<? super BrokerRootObjectBridge>>, WrapperListener<HousemateObject<?, ?, ?, ?, ?>> {

    private ListBridge<BrokerRealUser, UserWrappable, UserBridge> users;
    private ListBridge<BrokerProxyType, TypeWrappable<?>, TypeBridge> types;
    private ListBridge<BrokerProxyDevice, DeviceWrappable, DeviceBridge> devices;
    private ListBridge<BrokerRealRule, RuleWrappable, RuleBridge> rules;
    private CommandBridge addUser;
    private CommandBridge addDevice;
    private CommandBridge addRule;

    private DeviceListener<DeviceBridge> deviceListener = new DeviceListener<DeviceBridge>() {
        @Override
        public void error(DeviceBridge device, String description) {}

        @Override
        public void running(DeviceBridge device, boolean running) {
            deviceRunningChanged(device, running);
        }
    };

    private BrokerProxyPrimaryObject.Remover<BrokerProxyDevice> deviceRemover = new BrokerProxyPrimaryObject.Remover<BrokerProxyDevice>() {
        @Override
        public void remove(BrokerProxyDevice primaryObject) {
            if(getResources().getGeneralResources().getProxyResources().getRoot().getDevices().get(primaryObject.getId()) != null) {
                getResources().getGeneralResources().getProxyResources().getRoot().getDevices().remove(primaryObject.getId());
            }
        }
    };

    private final Map<String, Listeners<ObjectLifecycleListener>> objectLifecycleListeners = new HashMap<String, Listeners<ObjectLifecycleListener>>();

    public BrokerRootObjectBridge(final BrokerBridgeResources resources) {
        super(resources, new RootWrappable());
        resources.setRoot(this);
        users = new ListBridge<BrokerRealUser, UserWrappable, UserBridge>(resources,
                resources.getGeneralResources().getRealResources().getRoot().getUsers(),
                new Function<BrokerRealUser, UserBridge>() {
            @Override
            public UserBridge apply(@Nullable BrokerRealUser connection) {
                return new UserBridge(resources, connection);
            }
        });
        types = new ListBridge<BrokerProxyType, TypeWrappable<?>, TypeBridge>(resources,
                resources.getGeneralResources().getProxyResources().getRoot().getTypes(),
                new Function<BrokerProxyType, TypeBridge>() {
            @Override
            public TypeBridge apply(@Nullable BrokerProxyType type) {
                return new TypeBridge(resources, type.getWrappable(), type);
            }
        });
        devices = new ListBridge<BrokerProxyDevice, DeviceWrappable, DeviceBridge>(resources,
                resources.getGeneralResources().getProxyResources().getRoot().getDevices(),
                new Function<BrokerProxyDevice, DeviceBridge>() {
            @Override
            public DeviceBridge apply(@Nullable BrokerProxyDevice device) {
                device.setRemover(deviceRemover);
                return new DeviceBridge(resources, device);
            }
        });
        rules = new ListBridge<BrokerRealRule, RuleWrappable, RuleBridge>(resources,
                resources.getGeneralResources().getRealResources().getRoot().getRules(),
                new Function<BrokerRealRule, RuleBridge>() {
            @Override
            public RuleBridge apply(@Nullable BrokerRealRule rule) {
                return new RuleBridge(resources, rule);
            }
        });
        addUser = new CommandBridge(resources, resources.getGeneralResources().getRealResources().getRoot().getAddUserCommand());
        addDevice = new CommandBridge(resources, resources.getGeneralResources().getRealResources().getRoot().getAddDeviceCommand());
        addRule = new CommandBridge(resources, resources.getGeneralResources().getRealResources().getRoot().getAddRuleCommand());
        addWrapper(users);
        addWrapper(types);
        addWrapper(devices);
        addWrapper(rules);
        addWrapper(addUser);
        addWrapper(addDevice);
        addWrapper(addRule);
        devices.addObjectListener(new ListListener<DeviceBridge>() {
            @Override
            public void elementAdded(DeviceBridge device) {
                getResources().getGeneralResources().getStorage().loadDeviceInfo(device);
                device.addObjectListener(deviceListener);
            }

            @Override
            public void elementRemoved(DeviceBridge device) {
            }
        });
        init(null);
    }

    @Override
    public void connect(AuthenticationMethod method, AuthenticationResponseHandler responseHandler) {
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

    public ListBridge<BrokerRealUser, UserWrappable, UserBridge> getUsers() {
        return users;
    }

    public ListBridge<BrokerProxyType, TypeWrappable<?>, TypeBridge> getTypes() {
        return types;
    }

    public ListBridge<BrokerProxyDevice, DeviceWrappable, DeviceBridge> getDevices() {
        return devices;
    }

    public ListBridge<BrokerRealRule, RuleWrappable, RuleBridge> getRules() {
        return rules;
    }

    @Override
    protected List<ListenerRegistration<?>> registerListeners() {
        List<ListenerRegistration<?>> result = super.registerListeners();
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

    public final ListenerRegistration<ObjectLifecycleListener> addObjectLifecycleListener(String[] ancestorPath, ObjectLifecycleListener listener) {
        String path = Joiner.on(Wrapper.PATH_SEPARATOR).join(ancestorPath);
        Listeners<ObjectLifecycleListener> listeners = objectLifecycleListeners.get(path);
        if(listeners == null) {
            listeners = new Listeners<ObjectLifecycleListener>();
            objectLifecycleListeners.put(path, listeners);
        }
        return listeners.addListener(listener);
    }

    public void deviceRunningChanged(Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> device, boolean running) {
        try {
            getResources().getGeneralResources().getStorage().saveValue(device.getRunningValue().getPath(), Boolean.toString(running));
        } catch(HousemateException e) {
            getResources().getLog().w("Failed to save device running value. Device may not be started correctly the next time");
        }
    }
}
