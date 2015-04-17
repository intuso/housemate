package com.intuso.housemate.object.proxy;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.object.HousemateObject;

import java.util.List;

/**
 * Base class for managing the loading of remote objects
 */
public class LoadManager {

    private final Callback callback;
    private final List<HousemateObject.TreeLoadInfo> toLoad;

    public LoadManager(Callback callback, HousemateObject.TreeLoadInfo... toLoad) {
        this(callback, Lists.newArrayList(toLoad));
    }

    public LoadManager(Callback callback, List<HousemateObject.TreeLoadInfo> toLoad) {
        this.callback = callback;
        this.toLoad = toLoad;
    }

    /**
     * Get the info of the objects to load
     * @return the info of the objects to load
     */
    public final List<HousemateObject.TreeLoadInfo> getToLoad() {
        return toLoad;
    }

    /**
     * Callback for when an object's load has finished
     */
    protected final void succeeded() {
        callback.succeeded();
    }

    protected final void failed(List<String> errors) {
        callback.failed(errors);
    }

    public static interface Callback {

        /**
         * Callback for when the load of some objects failed
         * @param errors
         */
        public void failed(List<String> errors);

        /**
         * Callback for when all required objects have been loaded
         */
        public void succeeded();
    }
}
