package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.Node;
import com.intuso.housemate.client.api.internal.object.Server;
import com.intuso.housemate.client.api.internal.object.view.DeviceView;
import com.intuso.housemate.client.proxy.internal.object.ProxyDevice;

public interface RealServer<COMMAND extends RealCommand<?, ?, ?>,
        AUTOMATIONS extends RealList<? extends RealAutomation<?, ?, ?, ?, ?, ?>, ?>,
        DEVICES extends RealList<? extends RealReference<DeviceView<?>, ? extends ProxyDevice<?, ?, ?, ?, ?, ?, ?>, ?>, ?>,
        DEVICE_GROUPS extends RealList<? extends RealDeviceGroup<?, ?, ?, ?, ?, ?, ?, ?>, ?>,
        USERS extends RealList<? extends RealUser<?, ?, ?>, ?>,
        NODES extends RealList<? extends Node<?, ?, ?, ?>, ?>,
        SERVER extends RealServer<COMMAND, AUTOMATIONS, DEVICES, DEVICE_GROUPS, USERS, NODES, SERVER>>
        extends Server<COMMAND, AUTOMATIONS, DEVICES, DEVICE_GROUPS, USERS, NODES, SERVER> {}
