package com.intuso.housemate.comms.api.bridge.v1_0;

import com.google.common.base.Function;
import com.intuso.housemate.comms.v1_0.api.TreeLoadInfo;

/**
 * Created by tomc on 28/09/15.
 */
public class TreeLoadInfoMapper {

    private final Function<com.intuso.housemate.comms.api.internal.TreeLoadInfo, TreeLoadInfo> toV1_0Function = new Function<com.intuso.housemate.comms.api.internal.TreeLoadInfo, TreeLoadInfo>() {
        @Override
        public TreeLoadInfo apply(com.intuso.housemate.comms.api.internal.TreeLoadInfo treeLoadInfo) {
            return map(treeLoadInfo);
        }
    };

    private final Function<TreeLoadInfo, com.intuso.housemate.comms.api.internal.TreeLoadInfo> fromV1_0Function = new Function<TreeLoadInfo, com.intuso.housemate.comms.api.internal.TreeLoadInfo>() {
        @Override
        public com.intuso.housemate.comms.api.internal.TreeLoadInfo apply(TreeLoadInfo treeLoadInfo) {
            return map(treeLoadInfo);
        }
    };

    public Function<com.intuso.housemate.comms.api.internal.TreeLoadInfo, TreeLoadInfo> getToV1_0Function() {
        return toV1_0Function;
    }

    public Function<TreeLoadInfo, com.intuso.housemate.comms.api.internal.TreeLoadInfo> getFromV1_0Function() {
        return fromV1_0Function;
    }

    public com.intuso.housemate.comms.api.internal.TreeLoadInfo map(TreeLoadInfo treeLoadInfo) {
        if(treeLoadInfo == null)
            return null;
        com.intuso.housemate.comms.api.internal.TreeLoadInfo result = new com.intuso.housemate.comms.api.internal.TreeLoadInfo(treeLoadInfo.getId());
        for(String key : treeLoadInfo.getChildren().keySet())
            result.getChildren().put(key, map(treeLoadInfo.getChildren().get(key)));
        return result;
    }

    public TreeLoadInfo map(com.intuso.housemate.comms.api.internal.TreeLoadInfo treeLoadInfo) {
        if(treeLoadInfo == null)
            return null;
        TreeLoadInfo result = new TreeLoadInfo(treeLoadInfo.getId());
        for(String key : treeLoadInfo.getChildren().keySet())
            result.getChildren().put(key, map(treeLoadInfo.getChildren().get(key)));
        return result;
    }
}
