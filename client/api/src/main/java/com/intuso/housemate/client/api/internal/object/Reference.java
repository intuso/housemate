package com.intuso.housemate.client.api.internal.object;

import com.intuso.housemate.client.api.internal.object.view.View;

public interface Reference<
        OBJECT_VIEW extends View,
        OBJECT extends Object<?, ?, ?>,
        REFERENCE extends Reference<OBJECT_VIEW, OBJECT, REFERENCE>>
        extends Object<Reference.Data, Reference.Listener<? super REFERENCE>, OBJECT_VIEW> {

    OBJECT get();

    /**
     *
     * Listener interface for references
     */
    interface Listener<REFERENCE extends Reference<?, ?, ?>> extends Object.Listener {}

    class Data extends Object.Data {

        private static final long serialVersionUID = -1L;

        public final static String OBJECT_CLASS = "reference";

        private String path;

        public Data() {}

        public Data(String id, String name, String description, String path) {
            super(OBJECT_CLASS, id, name, description);
            this.path = path;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
