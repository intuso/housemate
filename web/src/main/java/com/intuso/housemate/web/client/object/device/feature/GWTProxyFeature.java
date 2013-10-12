package com.intuso.housemate.web.client.object.device.feature;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.object.proxy.device.feature.FeatureLoadedListener;
import com.intuso.housemate.object.proxy.device.feature.ProxyFeature;
import com.intuso.housemate.object.proxy.simple.SimpleProxyObject;
import com.intuso.housemate.web.client.object.GWTProxyDevice;

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
        List<HousemateObject.TreeLoadInfo> treeInfos = Lists.newArrayList();
        if(getCommandIds().size() > 0)
            treeInfos.add(makeTreeInfo(Device.COMMANDS_ID, getCommandIds()));
        if(getValueIds().size() > 0)
            treeInfos.add(makeTreeInfo(Device.VALUES_ID, getValueIds()));
        if(getPropertyIds().size() > 0)
            treeInfos.add(makeTreeInfo(Device.PROPERTIES_ID, getPropertyIds()));
        device.load(new LoadManager("featureLoader", treeInfos) {
            @Override
            protected void failed(HousemateObject.TreeLoadInfo failed) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            protected void allLoaded() {
                listener.featureLoaded(device, GWTProxyFeature.this);
            }
        });
    }

    private HousemateObject.TreeLoadInfo makeTreeInfo(String objectName, Set<String> childNames) {
        Map<String, HousemateObject.TreeLoadInfo> children = Maps.newHashMap();
        for(String childName : childNames)
            children.put(childName, new HousemateObject.TreeLoadInfo(childName, new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE)));
        return new HousemateObject.TreeLoadInfo(objectName, children);
    }

    public abstract String getTitle();
    public abstract Widget getWidget();
}
