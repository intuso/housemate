package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.Node;

public interface RealNode<COMMAND extends RealCommand<?, ?, ?>,
        HARDWARE extends RealHardware<?, ?, ?, ?, ?, ?, ?>,
        HARDWARES extends com.intuso.housemate.client.real.api.internal.RealList<? extends HARDWARE, ?>,
        NODE extends RealNode<COMMAND, HARDWARE, HARDWARES, NODE>>
        extends Node<COMMAND, HARDWARES, NODE>,
        RealHardware.Container<HARDWARE, HARDWARES> {
    COMMAND getAddHardwareCommand();

    interface Container<NODE extends RealNode<?, ?, ?, ?>, NODES extends com.intuso.housemate.client.real.api.internal.RealList<? extends NODE, ?>> extends Node.Container<NODES> {
        void addNode(NODE node);
        void removeNode(NODE node);
    }
}
