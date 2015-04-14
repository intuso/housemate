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
    private int loaded = 0;

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
     * @param succeeded true if the load was successful
     */
    protected final void finished(boolean succeeded) {
        if(!succeeded || toLoad.size() == ++loaded)
            callback.allLoaded();
    }

    public static interface Callback {

        /**
         * Callback for when the load of some objects failed
         * @param path the path of the failed object
         */
        public void failed(HousemateObject.TreeLoadInfo path);

        /**
         * Callback for when all required objects have been loaded
         */
        public void allLoaded();
    }
}
