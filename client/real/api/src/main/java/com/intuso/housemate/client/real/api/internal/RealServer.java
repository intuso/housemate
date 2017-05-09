package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.api.internal.object.List;
import com.intuso.housemate.client.api.internal.object.Node;
import com.intuso.housemate.client.api.internal.object.Server;

public interface RealServer<COMMAND extends RealCommand<?, ?, ?>,
        AUTOMATIONS extends RealList<? extends RealAutomation<?, ?, ?, ?, ?, ?>, ?>,
        DEVICE_GROUPS extends RealList<? extends RealDeviceGroup<?, ?, ?, ?, ?, ?, ?, ?>, ?>,
        USERS extends RealList<? extends RealUser<?, ?, ?>, ?>,
        NODES extends RealList<? extends Node<?, ?, ?, ?>, ?>,
        SERVER extends RealServer<COMMAND, AUTOMATIONS, DEVICE_GROUPS, USERS, NODES, SERVER>>
        extends Server<COMMAND, List<? extends Device<?, ?, ?, ?, ?>, ?>, AUTOMATIONS, DEVICE_GROUPS, USERS, NODES, SERVER> {}
