package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.comms.api.internal.Message;
import com.intuso.housemate.comms.api.internal.RequiresAccess;
import com.intuso.housemate.object.api.internal.ObjectLifecycleListener;
import com.intuso.housemate.object.api.internal.ObjectRoot;
import com.intuso.housemate.object.api.internal.Root;
import com.intuso.utilities.listener.ListenerRegistration;
import org.slf4j.Logger;

public interface RealRoot
        extends Root<RealRoot.Listener, RealRoot>,
        RequiresAccess,
        Message.Sender,
        ObjectRoot<RealRoot.Listener, RealRoot>,
        RealApplication.Container,
        RealAutomation.Container,
        RealDevice.Container,
        RealHardware.Container,
        RealType.Container,
        RealUser.Container {
    Logger getLogger();
    RealCommand getAddHardwareCommand();
    RealCommand getAddDeviceCommand();
    RealCommand getAddAutomationCommand();
    RealCommand getAddUserCommand();
    ListenerRegistration addObjectLifecycleListener(String[] ancestorPath, ObjectLifecycleListener listener);
    interface Listener extends Root.Listener<RealRoot>, RequiresAccess.Listener<RealRoot> {}
}
