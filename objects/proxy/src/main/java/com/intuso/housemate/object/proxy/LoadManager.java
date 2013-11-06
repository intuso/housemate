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
public abstract class LoadManager {

    private final String name;
    private final Map<String, HousemateObject.TreeLoadInfo> toLoad;
    private final Set<String> loaded = Sets.newHashSet();

    protected LoadManager(String name, HousemateObject.TreeLoadInfo... toLoad) {
        this(name, Lists.newArrayList(toLoad));
    }

    protected LoadManager(String name, List<HousemateObject.TreeLoadInfo> toLoad) {
        this.name = name;
        this.toLoad = Maps.newHashMap();
        for(HousemateObject.TreeLoadInfo tl : toLoad)
            this.toLoad.put(tl.getId(), tl);
    }

    protected LoadManager(String name, Map<String, HousemateObject.TreeLoadInfo> toLoad) {
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
        if(toLoad.containsKey(objectId))
            loaded.add(objectId);
        if(!succeeded)
            failed(toLoad.get(objectId));
        if(toLoad.size() == loaded.size())
            allLoaded();
    }

    /**
     * Callback for when the load of some objects failed
     * @param path the path of the failed object
     */
    protected abstract void failed(HousemateObject.TreeLoadInfo path);

    /**
     * Callback for when all required objects have been loaded
     */
    protected abstract void allLoaded();
}
