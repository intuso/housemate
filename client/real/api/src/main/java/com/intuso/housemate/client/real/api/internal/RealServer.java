package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.CombinationList;
import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.api.internal.object.Node;
import com.intuso.housemate.client.api.internal.object.Server;

public interface RealServer<COMMAND extends RealCommand<?, ?, ?>,
        AUTOMATIONS extends RealList<? extends RealAutomation<?, ?, ?, ?, ?, ?>, ?>,
        DEVICE_COMBIS extends RealList<? extends RealDeviceCombi<?, ?, ?, ?, ?, ?, ?, ?>, ?>,
        USERS extends RealList<? extends RealUser<?, ?, ?>, ?>,
        NODES extends RealList<? extends Node<?, ?, ?, ?>, ?>,
        SERVER extends RealServer<COMMAND, AUTOMATIONS, DEVICE_COMBIS, USERS, NODES, SERVER>>
        extends Server<COMMAND, CombinationList<Device<?, ?, ?, ?, ?>>, AUTOMATIONS, DEVICE_COMBIS, USERS, NODES, SERVER> {}
