package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.*;
import com.intuso.housemate.client.api.internal.type.ObjectReference;
import com.intuso.housemate.client.proxy.internal.object.ProxyDevice;

public interface RealServer<COMMAND extends RealCommand<?, ?, ?>,
        DEVICES extends RealList<? extends RealValue<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?>>, ?, ?>, ?>,
        AUTOMATIONS extends RealList<? extends RealAutomation<?, ?, ?, ?, ?, ?>, ?>,
        DEVICE_COMBIS extends RealList<? extends RealDeviceCombi<?, ?, ?, ?, ?, ?, ?, ?>, ?>,
        USERS extends RealList<? extends RealUser<?, ?, ?>, ?>,
        NODES extends RealList<? extends Node<?, ?, ?, ?>, ?>,
        SERVER extends RealServer<COMMAND, DEVICES, AUTOMATIONS, DEVICE_COMBIS, USERS, NODES, SERVER>>
        extends Server<COMMAND, DEVICES, AUTOMATIONS, DEVICE_COMBIS, USERS, NODES, SERVER>,
        Device.Container<Iterable<RealDevice<?, ?, ?, ?, ?>>>{}
