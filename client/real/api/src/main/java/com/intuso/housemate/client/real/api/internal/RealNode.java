package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.Hardware;
import com.intuso.housemate.client.api.internal.object.Node;

public interface RealNode<COMMAND extends RealCommand<?, ?, ?>,
        TYPES extends RealList<? extends RealType<?, ?>, ?>,
        HARDWARE extends RealHardware<?, ?, ?, ?, ?, ?, ?, ?, ?>,
        HARDWARES extends RealList<? extends HARDWARE, ?>,
        NODE extends RealNode<COMMAND, TYPES, HARDWARE, HARDWARES, NODE>>
        extends Node<COMMAND, TYPES, HARDWARES, NODE>,
        Hardware.Container<HARDWARES> {

    COMMAND getAddHardwareCommand();

    interface Container<NODE extends Node<?, ?, ?, ?>, NODES extends RealList<? extends NODE, ?>> extends Node.Container<NODES> {
        void addNode(NODE node);
        void removeNode(NODE node);
    }
}
