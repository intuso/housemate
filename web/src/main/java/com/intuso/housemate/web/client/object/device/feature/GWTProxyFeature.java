package com.intuso.housemate.web.client.object.device.feature;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.client.v1_0.proxy.api.LoadManager;
import com.intuso.housemate.client.v1_0.proxy.api.device.feature.FeatureLoadedListener;
import com.intuso.housemate.client.v1_0.proxy.api.device.feature.ProxyFeature;
import com.intuso.housemate.comms.v1_0.api.RemoteObject;
import com.intuso.housemate.comms.v1_0.api.TreeLoadInfo;
import com.intuso.housemate.comms.v1_0.api.payload.DeviceData;
import com.intuso.housemate.comms.v1_0.api.payload.TypeData;
import com.intuso.housemate.web.client.object.GWTProxyDevice;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GWTProxyFeature
        implements ProxyFeature<GWTProxyFeature, GWTProxyDevice> {

    protected final GWTProxyDevice device;

    public GWTProxyFeature(GWTProxyDevice device) {
        this.device = device;
    }

    public final GWTProxyFeature getThis() {
        return this;
    }

    public void load(final FeatureLoadedListener<GWTProxyDevice, GWTProxyFeature> listener) {
        List<TreeLoadInfo> treeInfos = Lists.newArrayList();
        if(getCommandIds().size() > 0)
            treeInfos.add(makeTreeInfo(DeviceData.COMMANDS_ID, getCommandIds()));
        if(getValueIds().size() > 0)
            treeInfos.add(makeTreeInfo(DeviceData.VALUES_ID, getValueIds()));
        if(getPropertyIds().size() > 0)
            treeInfos.add(makeTreeInfo(DeviceData.PROPERTIES_ID, getPropertyIds()));
        device.load(new LoadManager(new LoadManager.Callback() {
            @Override
            public void failed(List<String> errors) {
                listener.loadFailed(device, GWTProxyFeature.this);
            }

            @Override
            public void succeeded() {
                listener.loadFinished(device, GWTProxyFeature.this);
            }
        }, treeInfos));
    }

    private TreeLoadInfo makeTreeInfo(String objectName, Set<String> childNames) {
        Map<String, TreeLoadInfo> children = Maps.newHashMap();
        for(String childName : childNames)
            children.put(childName, new TreeLoadInfo(childName, new TreeLoadInfo(RemoteObject.EVERYTHING_RECURSIVE)));
        return new TreeLoadInfo(objectName, children);
    }

    public abstract String getTitle();
    public abstract Widget getWidget(GWTProxyList<TypeData<?>, GWTProxyType> types);
}
