package com.intuso.housemate.object.proxy;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.intuso.housemate.api.object.HousemateObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public String getName() {
        return name;
    }

    public final Map<String, HousemateObject.TreeLoadInfo> getToLoad() {
        return toLoad;
    }

    protected final void responseReceived(String objectName, boolean succeeded) {
        if(toLoad.containsKey(objectName))
            loaded.add(objectName);
        if(!succeeded)
            failed(toLoad.get(objectName));
        if(toLoad.size() == loaded.size())
            allLoaded();
    }

    protected abstract void failed(HousemateObject.TreeLoadInfo path);
    protected abstract void allLoaded();
}
