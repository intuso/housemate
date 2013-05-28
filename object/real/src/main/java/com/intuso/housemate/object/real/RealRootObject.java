package com.intuso.housemate.object.real;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.authentication.AuthenticationResponseHandler;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.connection.ClientWrappable;
import com.intuso.housemate.api.object.device.DeviceWrappable;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.root.RootWrappable;
import com.intuso.housemate.api.object.root.real.RealRoot;
import com.intuso.housemate.api.object.type.TypeWrappable;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 05/08/12
 * Time: 16:51
 * To change this template use File | Settings | File Templates.
 */
public class RealRootObject
        extends RealObject<RootWrappable, HousemateObjectWrappable<?>, RealObject<?, ? extends HousemateObjectWrappable<?>, ?, ?>, RootListener<? super RealRootObject>>
        implements RealRoot<RealType<?, ?, ?>, RealList<TypeWrappable<?>, RealType<?, ?, ?>>, RealDevice,
            RealList<DeviceWrappable, RealDevice>, RealRootObject> {

    private final RealList<TypeWrappable<?>, RealType<?, ?, ?>> types;
    private final RealList<DeviceWrappable, RealDevice> devices;

    private final Router.Registration routerRegistration;
    private AuthenticationMethod method = null;
    private AuthenticationResponseHandler responseHandler = null;

    public RealRootObject(RealResources resources) {
        super(resources, new RootWrappable());

        routerRegistration = resources.getRouter().registerReceiver(this);

        types = new RealList<TypeWrappable<?>, RealType<?, ?, ?>>(resources, TYPES, TYPES, "Defined types");
        devices = new RealList<DeviceWrappable, RealDevice>(resources, DEVICES, DEVICES, "Defined devices");

        init(null);

        addWrapper(types);
        types.init(this);
        addWrapper(devices);
        devices.init(this);
    }

    @Override
    public void connect(AuthenticationMethod method, AuthenticationResponseHandler responseHandler) {
        if(this.method != null)
            throw new HousemateRuntimeException("Authentication already in progress/succeeded");
        this.method = method;
        this.responseHandler = responseHandler;
        sendMessage(AUTHENTICATION_REQUEST, new AuthenticationRequest(ClientWrappable.Type.REAL, method));
    }

    @Override
    public void disconnect() {
        if(this.method == null)
            throw new HousemateRuntimeException("Not connected");
        routerRegistration.disconnect();
        method = null;
    }

    @Override
    public ListenerRegistration addObjectLifecycleListener(String[] path, ObjectLifecycleListener listener) {
        throw new HousemateRuntimeException("This root object is not intended to have listeners on its child objects");
    }

    @Override
    public void sendMessage(Message<?> message) {
        routerRegistration.sendMessage(message);
    }

    @Override
    public void messageReceived(Message<Message.Payload> message) throws HousemateException {
        distributeMessage(message);
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(AUTHENTICATION_RESPONSE, new Receiver<AuthenticationResponse>() {
            @Override
            public void messageReceived(Message<AuthenticationResponse> message) throws HousemateException {
                if(responseHandler != null)
                    responseHandler.responseReceived(message.getPayload());
                // if authentication failed remove responseHandler so that if can be tried again
                if(message.getPayload().getUserId() == null) {
                    method = null;
                    responseHandler = null;
                }
            }
        }));
        return result;
    }

    @Override
    public final RealList<TypeWrappable<?>, RealType<?, ?, ?>> getTypes() {
        return types;
    }

    @Override
    public final void addType(RealType<?, ?, ?> type) {
        types.add(type);
    }

    @Override
    public final void removeType(String name) {
        types.remove(name);
    }

    @Override
    public final RealList<DeviceWrappable, RealDevice> getDevices() {
        return devices;
    }

    @Override
    public final void addDevice(RealDevice device) {
        devices.add(device);
    }

    @Override
    public final void removeDevice(String name) {
        devices.remove(name);
    }
}
