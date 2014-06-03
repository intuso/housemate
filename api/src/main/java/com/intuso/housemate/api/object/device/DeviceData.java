package com.intuso.housemate.api.object.device;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.object.HousemateData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Data object for a device
 */
public final class DeviceData extends HousemateData<HousemateData<?>> {

    private static final long serialVersionUID = -1L;

    private List<String> featureIds;
    private List<String> customCommandIds;
    private List<String> customValueIds;
    private List<String> customPropertyIds;

    public DeviceData() {}

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

    /**
     * Get the ids of all the device's features
     * @return the ids of all the device's features
     */
    public List<String> getFeatureIds() {
        return featureIds;
    }

    public void setFeatureIds(List<String> featureIds) {
        this.featureIds = featureIds instanceof Serializable ? featureIds : Lists.newArrayList(featureIds);
    }

    /**
     * Get the ids of all the commands that do not belong to features
     * @return the ids of all the commands that do not belong to features
     */
    public List<String> getCustomCommandIds() {
        return customCommandIds;
    }

    public void setCustomCommandIds(List<String> customCommandIds) {
        this.customCommandIds = customCommandIds instanceof Serializable ? customCommandIds : Lists.newArrayList(customCommandIds);
    }

    /**
     * Get the ids of all the values that do not belong to features
     * @return the ids of all the values that do not belong to features
     */
    public List<String> getCustomValueIds() {
        return customValueIds;
    }

    public void setCustomValueIds(List<String> customValueIds) {
        this.customValueIds = customValueIds instanceof Serializable ? customValueIds : Lists.newArrayList(customValueIds);
    }

    /**
     * Get the ids of all the properties that do not belong to features
     * @return the ids of all the properties that do not belong to features
     */
    public List<String> getCustomPropertyIds() {
        return customPropertyIds;
    }

    public void setCustomPropertyIds(List<String> customPropertyIds) {
        this.customPropertyIds = customPropertyIds instanceof Serializable ? customPropertyIds : Lists.newArrayList(customPropertyIds);
    }

    @Override
    public HousemateData clone() {
        return new DeviceData(getId(), getName(), getDescription(), featureIds, customCommandIds, customValueIds,
                customPropertyIds);
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof DeviceData))
            return false;
        DeviceData other = (DeviceData)o;
        if(!listsEqual(other.featureIds, featureIds))
            return false;
        if(!listsEqual(other.customCommandIds, customCommandIds))
            return false;
        if(!listsEqual(other.customValueIds, customValueIds))
            return false;
        if(!listsEqual(other.customPropertyIds, customPropertyIds))
            return false;
        if(!other.getChildData(Device.COMMANDS_ID).equals(getChildData(Device.COMMANDS_ID)))
            return false;
        if(!other.getChildData(Device.VALUES_ID).equals(getChildData(Device.VALUES_ID)))
            return false;
        if(!other.getChildData(Device.PROPERTIES_ID).equals(getChildData(Device.PROPERTIES_ID)))
            return false;
        return true;
    }

    private <T> boolean listsEqual(List<T> one, List<T> two) {
        if(one.size() != two.size())
            return false;
        for(int i = 0; i < one.size(); i++)
            if(!one.get(i).equals(two.get(i)))
                return false;
        return true;
    }

    @Override
    public void ensureSerialisable() {
        super.ensureSerialisable();
        if(featureIds != null && (featureIds instanceof ArrayList))
            featureIds = Lists.newArrayList(featureIds);
        if(customCommandIds != null && (customCommandIds instanceof ArrayList))
            customCommandIds = Lists.newArrayList(customCommandIds);
        if(customValueIds != null && (customValueIds instanceof ArrayList))
            customValueIds = Lists.newArrayList(customValueIds);
        if(customPropertyIds != null && (customPropertyIds instanceof ArrayList))
            customPropertyIds = Lists.newArrayList(customPropertyIds);
    }
}
