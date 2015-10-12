package com.intuso.housemate.comms.api.internal;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Container class for data about what objects a client wants to load
 */
public class TreeLoadInfo implements Message.Payload {

    private static final long serialVersionUID = -1L;

    private String id;
    private Map<String, TreeLoadInfo> children;

    public static TreeLoadInfo create(String ... path) {
        return create(path, null);
    }

    public static TreeLoadInfo create(String[] path, String ending) {
        if(ending == null) {
            if(path == null)
                throw new HousemateCommsException("Null path to load");
            else if(path.length == 0)
                throw new HousemateCommsException("Empty path to load");
            final TreeLoadInfo root = new TreeLoadInfo(path[0]);
            TreeLoadInfo current = root;
            for(int i = 1; i < path.length; i++) {
                TreeLoadInfo child = new TreeLoadInfo(path[i]);
                current.getChildren().put(path[i], child);
                current = child;
            }
            return root;
        } else {
            if(path == null || path.length == 0)
                return new TreeLoadInfo(ending);
            else {
                final TreeLoadInfo root = new TreeLoadInfo(path[0]);
                TreeLoadInfo current = root;
                for(int i = 1; i < path.length; i++) {
                    TreeLoadInfo child = new TreeLoadInfo(path[i]);
                    current.getChildren().put(path[i], child);
                    current = child;
                }
                TreeLoadInfo child = new TreeLoadInfo(ending);
                current.getChildren().put(ending, child);
                return root;
            }
        }
    }

    public TreeLoadInfo() {}

    public TreeLoadInfo(String id, TreeLoadInfo... children) {
        this(id, asMap(children));
    }

    private static Map<String, TreeLoadInfo> asMap(TreeLoadInfo... children) {
        Map<String, TreeLoadInfo> result = new HashMap<>();
        for(TreeLoadInfo child : children)
            result.put(child.getId(), child);
        return result;
    }

    public TreeLoadInfo(String id, Map<String, TreeLoadInfo> children) {
        this.id = id;
        this.children = children;
    }

    /**
     * Get the id of the object to load
     * @return the id of the object to load
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the load info for child objects
     * @return the load info for child objects
     */
    public Map<String, TreeLoadInfo> getChildren() {
        return children;
    }

    public void setChildren(Map<String, TreeLoadInfo> children) {
        this.children = children == null || children instanceof Serializable ? children : new HashMap<>(children);
    }

    @Override
    public void ensureSerialisable() {
        if(children != null && !(children instanceof HashMap))
            children = new HashMap<>(children);
        if(children != null)
            for(TreeLoadInfo child : children.values())
                child.ensureSerialisable();
    }
}
