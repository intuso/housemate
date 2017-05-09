package com.intuso.housemate.client.api.internal.object;

import com.intuso.utilities.collection.ManagedCollection;

import java.io.Serializable;

/**
 * Base interface for all other object interfaces
 * @param <LISTENER> the type of the object's listeners
 */
public interface Object<LISTENER extends Object.Listener> {

    String VERSION = "internal";

    /**
     * Gets the object's type
     * @return the object's type
     */
    String getObjectClass();

    /**
     * Gets the object's id
     * @return the object's id
     */
    String getId();

    /**
     * Gets the object's name
     * @return the object's name
     */
    String getName();

    /**
     * Gets the object's description
     * @return the object's description
     */
    String getDescription();

    /**
     * Adds a listener to this object
     * @param listener the listener to add
     * @return the listener registration
     */
    ManagedCollection.Registration addObjectListener(LISTENER listener);

    Object<?> getChild(String id);

    /**
     *
     * Base listener interface for objects
     */
    interface Listener {}

    /**
     * Base data object for any Housemate object
     */
    class Data implements Serializable {

        private static final long serialVersionUID = -1L;

        private String objectClass;
        private String id;
        private String name;
        private String description;

        protected Data() {}

        /**
         * @param id object's id
         * @param name object's name
         * @param description object's description
         */
        public Data(String objectClass, String id, String name, String description) {
            this.objectClass = objectClass;
            this.id = id;
            this.name = name;
            this.description = description;
        }

        /**
         * Gets the type of the object
         * @return the type of the object
         */
        public String getObjectClass() {
            return objectClass;
        }

        public void setObjectClass(String objectClass) {
            this.objectClass = objectClass;
        }

        /**
         * Gets the id of the object
         * @return the id of the object
         */
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        /**
         * Gets the name of the object
         * @return the name of the object
         */
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        /**
         * Gets the object's description
         * @return the object's description
         */
        public final String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public boolean equals(java.lang.Object o) {
            if (this == o) return true;
            if (!(o instanceof Data)) return false;

            Data that = (Data) o;

            return !(id != null ? !id.equals(that.id) : that.id != null);

        }

        @Override
        public int hashCode() {
            return id != null ? id.hashCode() : 0;
        }
    }
}
