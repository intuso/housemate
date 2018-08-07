package com.intuso.housemate.client.api.internal.object;

import com.intuso.housemate.client.api.internal.object.view.DeviceComponentView;

import java.util.HashSet;
import java.util.Set;

/**
 * @param <COMMANDS> the type of the commands list
 * @param <VALUES> the type of the values list
 */
public interface DeviceComponent<
        COMMANDS extends List<? extends Command<?, ?, ?, ?>, ?>,
        VALUES extends List<? extends Value<?, ?, ?>, ?>,
        DEVICE_COMPONENT extends DeviceComponent<COMMANDS, VALUES, DEVICE_COMPONENT>>
        extends
        Object<DeviceComponent.Data, DeviceComponent.Listener<? super DEVICE_COMPONENT>, DeviceComponentView>,
        Command.Container<COMMANDS>,
        Value.Container<VALUES> {

    String COMMANDS_ID = "commands";
    String VALUES_ID = "values";

    Set<String> getClasses();
    Set<String> getAbilities();

    /**
     *
     * Listener interface for devices
     */
    interface Listener<DEVICE_COMPONENT extends DeviceComponent<?, ?, ?>> extends Object.Listener {}

    /**
     *
     * Interface to show that the implementing object has a list of devices
     */
    interface Container<DEVICE_COMPONENTS extends Iterable<? extends DeviceComponent<?, ?, ?>>> {

        /**
         * Gets the hardware list
         * @return the hardware list
         */
        DEVICE_COMPONENTS getDeviceComponents();
    }

    /**
     * Data object for a device
     */
    final class Data extends Object.Data {

        private static final long serialVersionUID = -1L;

        public final static String OBJECT_CLASS = "device-component";

        private Set<String> classes = new HashSet<>();
        private Set<String> abilities;

        public Data() {}

        public Data(String id, String name, String description) {
            this(id, name, description, new HashSet<>(), new HashSet<>());
        }

        public Data(String id, String name, String description, Set<String> classes, Set<String> abilities) {
            super(OBJECT_CLASS, id, name, description);
            this.classes = classes;
            this.abilities = abilities;
        }

        public Set<String> getClasses() {
            return classes;
        }

        public void setClasses(Set<String> classes) {
            this.classes = classes;
        }

        public Set<String> getAbilities() {
            return abilities;
        }

        public void setAbilities(Set<String> abilities) {
            this.abilities = abilities;
        }
    }
}
