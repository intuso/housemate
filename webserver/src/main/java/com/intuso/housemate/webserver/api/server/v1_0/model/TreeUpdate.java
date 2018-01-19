package com.intuso.housemate.webserver.api.server.v1_0.model;

import com.intuso.housemate.client.v1_0.api.object.Tree;

import java.io.Serializable;

public class TreeUpdate implements Serializable {

    private static final long serialVersionUID = -1L;

    private String path;
    private Tree tree;

    public TreeUpdate() {}

    public TreeUpdate(String path, Tree tree) {
        this.path = path;
        this.tree = tree;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Tree getTree() {
        return tree;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }
}
