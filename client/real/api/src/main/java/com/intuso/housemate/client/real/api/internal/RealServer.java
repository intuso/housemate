package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.Automation;
import com.intuso.housemate.client.api.internal.object.Node;
import com.intuso.housemate.client.api.internal.object.Server;
import com.intuso.housemate.client.api.internal.object.System;
import com.intuso.housemate.client.api.internal.object.User;

public interface RealServer<COMMAND extends RealCommand<?, ?, ?>,
        AUTOMATION extends RealAutomation<?, ?, ?, ?, ?, ?>,
        AUTOMATIONS extends RealList<? extends AUTOMATION, ?>,
        SYSTEM extends RealSystem<?, ?, ?, ?>,
        SYSTEMS extends RealList<? extends SYSTEM, ?>,
        USER extends RealUser<?, ?, ?>,
        USERS extends RealList<? extends USER, ?>,
        NODE extends Node<?, ?, ?, ?>,
        NODES extends RealList<? extends NODE, ?>,
        SERVER extends RealServer<COMMAND, AUTOMATION, AUTOMATIONS, SYSTEM, SYSTEMS, USER, USERS, NODE, NODES, SERVER>>
        extends Server<COMMAND, AUTOMATIONS, SYSTEMS, USERS, NODES, SERVER>,
        Automation.Container<AUTOMATIONS>,
        System.Container<SYSTEMS>,
        User.Container<USERS>,
        Node.Container<NODES>{}
