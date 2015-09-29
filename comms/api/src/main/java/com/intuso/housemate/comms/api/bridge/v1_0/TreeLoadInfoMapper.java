package com.intuso.housemate.comms.api.bridge.v1_0;

import com.google.common.base.Function;
import com.intuso.housemate.comms.api.internal.RemoteObject;

/**
 * Created by tomc on 28/09/15.
 */
public class TreeLoadInfoMapper {

    private final Function<RemoteObject.TreeLoadInfo, com.intuso.housemate.comms.v1_0.api.RemoteObject.TreeLoadInfo> toV1_0Function = new Function<RemoteObject.TreeLoadInfo, com.intuso.housemate.comms.v1_0.api.RemoteObject.TreeLoadInfo>() {
        @Override
        public com.intuso.housemate.comms.v1_0.api.RemoteObject.TreeLoadInfo apply(RemoteObject.TreeLoadInfo treeLoadInfo) {
            return map(treeLoadInfo);
        }
    };

    private final Function<com.intuso.housemate.comms.v1_0.api.RemoteObject.TreeLoadInfo, RemoteObject.TreeLoadInfo> fromV1_0Function = new Function<com.intuso.housemate.comms.v1_0.api.RemoteObject.TreeLoadInfo, RemoteObject.TreeLoadInfo>() {
        @Override
        public RemoteObject.TreeLoadInfo apply(com.intuso.housemate.comms.v1_0.api.RemoteObject.TreeLoadInfo treeLoadInfo) {
            return map(treeLoadInfo);
        }
    };

    public Function<RemoteObject.TreeLoadInfo, com.intuso.housemate.comms.v1_0.api.RemoteObject.TreeLoadInfo> getToV1_0Function() {
        return toV1_0Function;
    }

    public Function<com.intuso.housemate.comms.v1_0.api.RemoteObject.TreeLoadInfo, RemoteObject.TreeLoadInfo> getFromV1_0Function() {
        return fromV1_0Function;
    }

    public RemoteObject.TreeLoadInfo map(com.intuso.housemate.comms.v1_0.api.RemoteObject.TreeLoadInfo treeLoadInfo) {
        if(treeLoadInfo == null)
            return null;
        RemoteObject.TreeLoadInfo result = new RemoteObject.TreeLoadInfo(treeLoadInfo.getId());
        for(String key : treeLoadInfo.getChildren().keySet())
            result.getChildren().put(key, map(treeLoadInfo.getChildren().get(key)));
        return result;
    }

    public com.intuso.housemate.comms.v1_0.api.RemoteObject.TreeLoadInfo map(RemoteObject.TreeLoadInfo treeLoadInfo) {
        if(treeLoadInfo == null)
            return null;
        com.intuso.housemate.comms.v1_0.api.RemoteObject.TreeLoadInfo result = new com.intuso.housemate.comms.v1_0.api.RemoteObject.TreeLoadInfo(treeLoadInfo.getId());
        for(String key : treeLoadInfo.getChildren().keySet())
            result.getChildren().put(key, map(treeLoadInfo.getChildren().get(key)));
        return result;
    }
}
