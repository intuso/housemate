package com.intuso.housemate.object.proxy;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.intuso.housemate.api.object.HousemateObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Base class for managing the loading of remote objects
 */
public class LoadManager {

    private final Callback callback;
    private final String name;
    private final Map<String, HousemateObject.TreeLoadInfo> toLoad;
    private final Set<String> loaded = Sets.newHashSet();

    public LoadManager(Callback callback, String name, HousemateObject.TreeLoadInfo... toLoad) {
        this(callback, name, Lists.newArrayList(toLoad));
    }

    public LoadManager(Callback callback, String name, List<HousemateObject.TreeLoadInfo> toLoad) {
        this.callback = callback;
        this.name = name;
        this.toLoad = Maps.newHashMap();
        for(HousemateObject.TreeLoadInfo tl : toLoad)
            this.toLoad.put(tl.getId(), tl);
    }

    public LoadManager(Callback callback, String name, Map<String, HousemateObject.TreeLoadInfo> toLoad) {
        this.callback = callback;
        this.name = name;
        this.toLoad = toLoad;
    }

    /**
     * Get the name of the loader
     * @return the name of the loader
     */
    public String getName() {
        return name;
    }

    /**
     * Get the info of the objects to load
     * @return the info of the objects to load
     */
    public final Map<String, HousemateObject.TreeLoadInfo> getToLoad() {
        return toLoad;
    }

    /**
     * Callback for when an object's load has finished
     * @param objectId the id of the object whose load finished
     * @param succeeded true if the load was successful
     */
    protected final void responseReceived(String objectId, boolean succeeded) {
        if(objectId != null) {
            if(toLoad.containsKey(objectId))
                loaded.add(objectId);
            if(!succeeded)
                callback.failed(toLoad.get(objectId));
        }
        if(toLoad.size() == loaded.size())
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
