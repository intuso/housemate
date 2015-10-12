package com.intuso.housemate.comms.api.internal;

import java.util.ArrayList;
import java.util.List;

/**
 * Message payload for a load request of a remote object
 */
public class LoadRequest implements Message.Payload {

    private static final long serialVersionUID = -1L;

    private String loaderId;
    private List<TreeLoadInfo> treeLoadInfos;

    public LoadRequest() {}

    /**
     * @param treeLoadInfos the id of the child object to load
     */
    public LoadRequest(String loaderId, List<TreeLoadInfo> treeLoadInfos) {
        this.loaderId = loaderId;
        this.treeLoadInfos = treeLoadInfos;
    }

    /**
     * Get the loader name
     * @return the loader name
     */
    public String getLoaderId() {
        return loaderId;
    }

    public void setLoaderId(String loaderId) {
        this.loaderId = loaderId;
    }

    /**
     * Gets the id of the child object to load
     * @return the id of the child object to load
     */
    public List<TreeLoadInfo> getTreeLoadInfos() {
        return treeLoadInfos;
    }

    public void setTreeLoadInfos(List<TreeLoadInfo> treeLoadInfos) {
        this.treeLoadInfos = treeLoadInfos;
    }

    @Override
    public String toString() {
        return loaderId;
    }

    @Override
    public void ensureSerialisable() {
        if(treeLoadInfos != null && !(treeLoadInfos instanceof ArrayList))
            treeLoadInfos = new ArrayList<>(treeLoadInfos);
        if(treeLoadInfos != null)
            for(TreeLoadInfo treeLoadInfo : treeLoadInfos)
                treeLoadInfo.ensureSerialisable();
    }
}
