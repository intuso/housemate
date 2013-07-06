package com.intuso.housemate.object.proxy;

import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public abstract class LoadManager {

    private final Set<String> toLoad;
    private final Set<String> loaded = Sets.newHashSet();

    protected LoadManager(String ... toLoad) {
        this(Arrays.asList(toLoad));
    }

    protected LoadManager(Collection<String> toLoad) {
        this(Sets.newHashSet(toLoad));
    }

    protected LoadManager(Set<String> toLoad) {
        this.toLoad = toLoad;
    }

    public final Set<String> getToLoad() {
        return toLoad;
    }

    protected final void finished(String id) {
        loaded.add(id);
        if(toLoad.size() == loaded.size())
            allLoaded();
    }

    protected abstract void failed(String id);
    protected abstract void allLoaded();
}
