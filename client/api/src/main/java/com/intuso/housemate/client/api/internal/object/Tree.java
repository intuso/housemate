package com.intuso.housemate.client.api.internal.object;

import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.utilities.collection.ManagedCollection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tree {

    private Object.Data data;
    private Map<String, Tree> children = new HashMap<>();

    public Tree() {}

    public Tree(Object.Data data) {
        this.data = data;
    }

    public Object.Data getData() {
        return data;
    }

    public void setData(Object.Data data) {
        this.data = data;
    }

    public Map<String, Tree> getChildren() {
        return children;
    }

    public void setChildren(Map<String, Tree> children) {
        this.children = children;
    }

    public interface ReferenceHandler {
        void handle(String path, View view, Tree.ReferenceHandler referenceHandler, Tree.Listener listener, List<ManagedCollection.Registration> listenerRegistrations);
    }

    public interface Listener {
        void updated(String path, Tree tree);
    }
}
