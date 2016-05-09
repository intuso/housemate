package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.Server;

public interface RealServer<COMMAND extends RealCommand<?, ?, ?>,
        AUTOMATION extends RealAutomation<?, ?, ?, ?, ?, ?>,
        AUTOMATIONS extends com.intuso.housemate.client.real.api.internal.RealList<? extends AUTOMATION, ?>,
        DEVICE extends RealDevice<?, ?, ?, ?, ?, ?, ?, ?>,
        DEVICES extends com.intuso.housemate.client.real.api.internal.RealList<? extends DEVICE, ?>,
        USER extends RealUser<?, ?, ?>,
        USERS extends com.intuso.housemate.client.real.api.internal.RealList<? extends USER, ?>,
        NODE extends com.intuso.housemate.client.real.api.internal.RealNode<?, ?, ?, ?>,
        NODES extends RealList<? extends NODE, ?>,
        SERVER extends RealServer<COMMAND, AUTOMATION, AUTOMATIONS, DEVICE, DEVICES, USER, USERS, NODE, NODES, SERVER>>
        extends Server<COMMAND, AUTOMATIONS, DEVICES, USERS, NODES, SERVER>,
        RealAutomation.Container<AUTOMATION, AUTOMATIONS>,
        RealDevice.Container<DEVICE, DEVICES>,
        RealUser.Container<USER, USERS>,
        com.intuso.housemate.client.real.api.internal.RealNode.Container<NODE, NODES> {}
