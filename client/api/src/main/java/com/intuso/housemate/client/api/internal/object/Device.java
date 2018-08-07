package com.intuso.housemate.client.api.internal.object;

import com.intuso.housemate.client.api.internal.Failable;
import com.intuso.housemate.client.api.internal.Removeable;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.object.view.DeviceConnectedView;
import com.intuso.housemate.client.api.internal.object.view.DeviceGroupView;
import com.intuso.housemate.client.api.internal.object.view.DeviceView;

public interface Device<
        DATA extends Device.Data,
        LISTENER extends Device.Listener<? super DEVICE>,
        RENAME_COMMAND extends Command<?, ?, ?, ?>,
        DEVICE_COMPONENTS extends List<? extends DeviceComponent<?, ?, ?>, ?>,
        VIEW extends DeviceView<?>,
        DEVICE extends Device<DATA, LISTENER, RENAME_COMMAND, DEVICE_COMPONENTS, VIEW, DEVICE>>
        extends
        Object<DATA, LISTENER, VIEW>,
        Renameable<RENAME_COMMAND>,
        DeviceComponent.Container<DEVICE_COMPONENTS> {

    String DEVICE_COMPONENTS_ID = "components";

    /**
     *
     * Listener interface for features
     */
    interface Listener<DEVICE extends Device<?, ?, ?, ?, ?, ?>> extends Object.Listener,
            Renameable.Listener<DEVICE> {}

    /**
     *
     * Interface to show that the implementing object has a list of features
     */
    interface Container<DEVICES extends Iterable<? extends Device<?, ?, ?, ?, ?, ?>>> {

        /**
         * Gets the features list
         * @return the features list
         */
        DEVICES getDevices();
    }

    /**
     * Data object for a device
     */
    class Data extends Object.Data {

        private static final long serialVersionUID = -1L;

        public Data() {}

        public Data(String objectClass, String id, String name, String description) {
            super(objectClass, id, name, description);
        }
    }

    /**
     * @param <DEVICE> the type of the device
     */
    interface Connected<
            RENAME_COMMAND extends Command<?, ?, ?, ?>,
            DEVICE_COMPONENTS extends List<? extends DeviceComponent<?, ?, ?>, ?>,
            DEVICE extends Connected<RENAME_COMMAND, DEVICE_COMPONENTS, DEVICE>>
            extends
            Device<Connected.Data, Connected.Listener<? super DEVICE>, RENAME_COMMAND, DEVICE_COMPONENTS, DeviceConnectedView, DEVICE>  {

        /**
         *
         * Listener interface for devices
         */
        interface Listener<DEVICE extends Connected<?, ?, ?>> extends Device.Listener<DEVICE> {}

        /**
         *
         * Interface to show that the implementing object has a list of features
         */
        interface Container<DEVICES extends Iterable<? extends Device<?, ?, ?, ?, ?, ?>>> {

            /**
             * Gets the features list
             * @return the features list
             */
            DEVICES getDeviceConnecteds();
        }

        /**
         * Data object for a device
         */
        final class Data extends Device.Data {

            private static final long serialVersionUID = -1L;

            public final static String OBJECT_CLASS = "device-connected";

            public Data() {}

            public Data(String id, String name, String description) {
                super(OBJECT_CLASS, id, name, description);
            }
        }
    }

    /**
     * @param <ERROR_VALUE> the type of the error value
     * @param <DEVICE> the type of the device
     */
    interface Group<
            RENAME_COMMAND extends Command<?, ?, ?, ?>,
            REMOVE_COMMAND extends Command<?, ?, ?, ?>,
            ADD_COMMAND extends Command<?, ?, ?, ?>,
            ERROR_VALUE extends Value<?, ?, ?>,
            DEVICE_COMPONENTS extends List<? extends DeviceComponent<?, ?, ?>, ?>,
            DEVICES extends List<? extends Reference<?, ? extends Device<?, ?, ?, ?, ?, ?>, ?>, ?>,
            DEVICE extends Group<RENAME_COMMAND, REMOVE_COMMAND, ADD_COMMAND, ERROR_VALUE, DEVICE_COMPONENTS, DEVICES, DEVICE>>
            extends
            Device<Group.Data, Group.Listener<? super DEVICE>, RENAME_COMMAND, DEVICE_COMPONENTS, DeviceGroupView, DEVICE>,
            Failable<ERROR_VALUE>,
            Removeable<REMOVE_COMMAND> {

        String PLAYBACK = "playbackDevices";
        String ADD_PLAYBACK = "addPlaybackDevice";
        String POWER = "powerDevices";
        String ADD_POWER = "addPowerDevice";
        String RUN = "runDevices";
        String ADD_RUN = "addRunDevice";
        String TEMPERATURE_SENSOR = "temperatureSensorDevices";
        String ADD_TEMPERATURE_SENSOR = "addTemperatureSensorDevice";
        String VOLUME = "volumeDevices";
        String ADD_VOLUME = "addVolumeDevice";

        DEVICES getPlaybackDevices();
        ADD_COMMAND getAddPlaybackDeviceCommand();
        DEVICES getPowerDevices();
        ADD_COMMAND getAddPowerDeviceCommand();
        DEVICES getRunDevices();
        ADD_COMMAND getAddTemperatureSensorDeviceCommand();
        DEVICES getTemperatureSensorDevices();
        ADD_COMMAND getAddRunDeviceCommand();
        DEVICES getVolumeDevices();
        ADD_COMMAND getAddVolumeDeviceCommand();

        /**
         *
         * Listener interface for devices
         */
        interface Listener<DEVICE extends Group<?, ?, ?, ?, ?, ?, ?>> extends Device.Listener<DEVICE>,
                Failable.Listener<DEVICE>,
                Renameable.Listener<DEVICE> {}

        /**
         *
         * Interface to show that the implementing object has a list of features
         */
        interface Container<DEVICES extends Iterable<? extends Device<?, ?, ?, ?, ?, ?>>> {

            /**
             * Gets the features list
             * @return the features list
             */
            DEVICES getDeviceGroups();
        }

        /**
         * Data object for a device
         */
        final class Data extends Device.Data {

            private static final long serialVersionUID = -1L;

            public final static String OBJECT_CLASS = "device-group";

            public Data() {}

            public Data(String id, String name, String description) {
                super(OBJECT_CLASS, id, name, description);
            }
        }
    }
}
