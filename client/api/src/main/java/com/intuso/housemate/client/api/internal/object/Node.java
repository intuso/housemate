package com.intuso.housemate.client.api.internal.object;

public interface Node<
        COMMAND extends Command<?, ?, ?, ?>,
        TYPES extends List<? extends Type<?>, ?>,
        HARDWARES extends List<? extends Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?>, ?>,
        NODE extends Node<COMMAND, TYPES, HARDWARES, NODE>>
        extends Object<Node.Listener<? super NODE>>,
        Type.Container<TYPES>,
        Hardware.Container<HARDWARES> {

    String TYPES_ID = "types";
    String HARDWARES_ID = "hardwares";
    String ADD_HARDWARE_ID = "add-hardware";

    COMMAND getAddHardwareCommand();

    /**
     *
     * Listener interface for server
     */
    interface Listener<NODE extends Node> extends Object.Listener {}

    /**
     *
     * Interface to show that the implementing object has a list of server
     */
    interface Container<NODES extends List<? extends Node<?, ?, ?, ?>, ?>> {

        /**
         * Gets the commands list
         * @return the commands list
         */
        NODES getNodes();
    }

    /**
     * Data object for a command
     */
    final class Data extends Object.Data {

        private static final long serialVersionUID = -1L;

        public final static String OBJECT_TYPE = "node";

        public Data() {}

        public Data(String id, String name, String description) {
            super(OBJECT_TYPE, id, name, description);
        }
    }
}
