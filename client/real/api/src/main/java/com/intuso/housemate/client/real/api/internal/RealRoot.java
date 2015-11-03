package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.comms.api.internal.Message;
import com.intuso.housemate.comms.api.internal.RequiresAccess;
import com.intuso.housemate.object.api.internal.*;
import com.intuso.utilities.listener.ListenerRegistration;

public interface RealRoot
        extends Root<RealRoot.Listener, RealRoot>,
        RequiresAccess,
        Message.Sender,
        ObjectRoot<RealRoot.Listener, RealRoot>,
        Application.Container<RealList<? extends RealApplication>>,
        Automation.Container<RealList<? extends RealAutomation>>,
        Device.Container<RealList<? extends RealDevice<?>>>,
        Hardware.Container<RealList<? extends RealHardware<?>>>,
        Type.Container<RealList<? extends RealType<?>>>,
        User.Container<RealList<? extends RealUser>>,
        RealHardware.RemovedListener,
        RealDevice.RemovedListener,
        RealAutomation.RemovedListener,
        RealUser.RemovedListener {

    boolean checkCanSendMessage(Message<?> message);

    RealCommand getAddHardwareCommand();

    RealCommand getAddDeviceCommand();

    RealCommand getAddAutomationCommand();

    RealCommand getAddUserCommand();

    void addType(RealType<?> type);

    void removeType(String id);

    void addAutomation(RealAutomation automation);

    void removeAutomation(String id);

    void addDevice(RealDevice<?> device);

    void removeDevice(String id);

    void addHardware(RealHardware<?> hardware);

    void removeHardware(String id);

    void addUser(RealUser user);

    void removeUser(String id);

    ListenerRegistration addObjectLifecycleListener(String[] ancestorPath, ObjectLifecycleListener listener);

    interface Listener extends Root.Listener<RealRoot>, RequiresAccess.Listener<RealRoot> {}
}
