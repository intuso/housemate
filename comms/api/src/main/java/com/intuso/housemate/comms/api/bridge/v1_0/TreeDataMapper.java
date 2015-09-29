package com.intuso.housemate.comms.api.bridge.v1_0;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.comms.api.internal.ChildOverview;
import com.intuso.housemate.comms.api.internal.RemoteObject;

/**
 * Created by tomc on 28/09/15.
 */
public class TreeDataMapper {

    private final ChildOverviewMapper childOverviewMapper;
    private final DataMapper dataMapper;

    @Inject
    public TreeDataMapper(ChildOverviewMapper childOverviewMapper, DataMapper dataMapper) {
        this.childOverviewMapper = childOverviewMapper;
        this.dataMapper = dataMapper;
    }

    public RemoteObject.TreeData map(com.intuso.housemate.comms.v1_0.api.RemoteObject.TreeData treeData) {
        if(treeData == null)
            return null;
        RemoteObject.TreeData result = new RemoteObject.TreeData(treeData.getId(), dataMapper.map(treeData.getData()),
                Maps.<String, RemoteObject.TreeData>newHashMap(), Maps.<String, ChildOverview>newHashMap());
        for(String key : treeData.getChildren().keySet())
            result.getChildren().put(key, map(treeData.getChildren().get(key)));
        for(String key : treeData.getChildOverviews().keySet())
            result.getChildOverviews().put(key, childOverviewMapper.map(treeData.getChildOverviews().get(key)));
        return result;
    }

    public com.intuso.housemate.comms.v1_0.api.RemoteObject.TreeData map(RemoteObject.TreeData treeData) {
        if(treeData == null)
            return null;
        com.intuso.housemate.comms.v1_0.api.RemoteObject.TreeData result = new com.intuso.housemate.comms.v1_0.api.RemoteObject.TreeData(treeData.getId(), dataMapper.map(treeData.getData()),
                Maps.<String, com.intuso.housemate.comms.v1_0.api.RemoteObject.TreeData>newHashMap(), Maps.<String, com.intuso.housemate.comms.v1_0.api.ChildOverview>newHashMap());
        for(String key : treeData.getChildren().keySet())
            result.getChildren().put(key, map(treeData.getChildren().get(key)));
        for(String key : treeData.getChildOverviews().keySet())
            result.getChildOverviews().put(key, childOverviewMapper.map(treeData.getChildOverviews().get(key)));
        return result;
    }
}
