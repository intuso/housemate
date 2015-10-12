package com.intuso.housemate.comms.api.bridge.v1_0;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.comms.api.internal.ChildOverview;
import com.intuso.housemate.comms.v1_0.api.TreeData;

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

    public com.intuso.housemate.comms.api.internal.TreeData map(TreeData treeData) {
        if(treeData == null)
            return null;
        com.intuso.housemate.comms.api.internal.TreeData result = new com.intuso.housemate.comms.api.internal.TreeData(treeData.getId(), dataMapper.map(treeData.getData()),
                Maps.<String, com.intuso.housemate.comms.api.internal.TreeData>newHashMap(), Maps.<String, ChildOverview>newHashMap());
        for(String key : treeData.getChildren().keySet())
            result.getChildren().put(key, map(treeData.getChildren().get(key)));
        for(String key : treeData.getChildOverviews().keySet())
            result.getChildOverviews().put(key, childOverviewMapper.map(treeData.getChildOverviews().get(key)));
        return result;
    }

    public TreeData map(com.intuso.housemate.comms.api.internal.TreeData treeData) {
        if(treeData == null)
            return null;
        TreeData result = new TreeData(treeData.getId(), dataMapper.map(treeData.getData()),
                Maps.<String, TreeData>newHashMap(), Maps.<String, com.intuso.housemate.comms.v1_0.api.ChildOverview>newHashMap());
        for(String key : treeData.getChildren().keySet())
            result.getChildren().put(key, map(treeData.getChildren().get(key)));
        for(String key : treeData.getChildOverviews().keySet())
            result.getChildOverviews().put(key, childOverviewMapper.map(treeData.getChildOverviews().get(key)));
        return result;
    }
}
