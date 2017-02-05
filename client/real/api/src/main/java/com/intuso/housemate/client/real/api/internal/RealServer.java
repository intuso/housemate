package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.Node;
import com.intuso.housemate.client.api.internal.object.Server;

public interface RealServer<COMMAND extends RealCommand<?, ?, ?>,
        AUTOMATION extends RealAutomation<?, ?, ?, ?, ?, ?>,
        AUTOMATIONS extends RealList<? extends AUTOMATION, ?>,
        DEVICE extends RealDevice<?, ?, ?, ?>,
        DEVICES extends RealList<? extends DEVICE, ?>,
        USER extends RealUser<?, ?, ?>,
        USERS extends RealList<? extends USER, ?>,
        NODE extends Node<?, ?, ?, ?>,
        NODES extends RealList<? extends NODE, ?>,
        SERVER extends RealServer<COMMAND, AUTOMATION, AUTOMATIONS, DEVICE, DEVICES, USER, USERS, NODE, NODES, SERVER>>
        extends Server<COMMAND, AUTOMATIONS, DEVICES, USERS, NODES, SERVER>,
        RealAutomation.Container<AUTOMATION, AUTOMATIONS>,
        RealDevice.Container<DEVICE, DEVICES>,
        RealUser.Container<USER, USERS>,
        RealNode.Container<NODE, NODES>{}
