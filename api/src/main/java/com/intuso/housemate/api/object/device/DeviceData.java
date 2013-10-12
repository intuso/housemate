package com.intuso.housemate.api.object.device;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.object.HousemateData;

import java.util.List;

/**
 * Data object for a device
 */
public final class DeviceData extends HousemateData<HousemateData<?>> {

    private List<String> featureIds;
    private List<String> customCommandIds;
    private List<String> customValueIds;
    private List<String> customPropertyIds;

    private DeviceData() {}

    public DeviceData(String id, String name, String description, String ... featureIds) {
        this(id, name, description, Lists.newArrayList(featureIds), Lists.<String>newArrayList(),
                Lists.<String>newArrayList(), Lists.<String>newArrayList());
    }

    public DeviceData(String id, String name, String description, List<String> featureIds,
                      List<String> customCommandIds, List<String> customValueIds, List<String> customPropertyIds) {
        super(id, name, description);
        this.featureIds = featureIds;
        this.customCommandIds = customCommandIds;
        this.customValueIds = customValueIds;
        this.customPropertyIds = customPropertyIds;
    }

    public List<String> getFeatureIds() {
        return featureIds;
    }

    public List<String> getCustomCommandIds() {
        return customCommandIds;
    }

    public List<String> getCustomValueIds() {
        return customValueIds;
    }

    public List<String> getCustomPropertyIds() {
        return customPropertyIds;
    }

    @Override
    public HousemateData clone() {
        return new DeviceData(getId(), getName(), getDescription(), featureIds, customCommandIds, customValueIds,
                customPropertyIds);
    }
}
