package com.intuso.housemate.comms.api.internal.payload;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Data object for a device
 */
public final class DeviceData extends HousemateData<HousemateData<?>> {

    private static final long serialVersionUID = -1L;

    public final static String RENAME_ID = "rename";
    public final static String NEW_NAME = "new-name";
    public final static String NAME_ID = "name";
    public final static String REMOVE_ID = "remove";
    public final static String START_ID = "start";
    public final static String STOP_ID = "stop";
    public final static String RUNNING_ID = "running";
    public final static String ERROR_ID = "error";
    public final static String DRIVER_ID = "driver";
    public final static String DRIVER_LOADED_ID = "driver-loaded";
    public final static String COMMANDS_ID = "commands";
    public final static String VALUES_ID = "values";
    public final static String PROPERTIES_ID = "properties";

    private List<String> featureIds;
    private List<String> customCommandIds;
    private List<String> customValueIds;
    private List<String> customPropertyIds;

    public DeviceData() {}

    public DeviceData(String id, String name, String description, String ... featureIds) {
        this(id, name, description, Arrays.asList(featureIds), new ArrayList<String>(),
                new ArrayList<String>(), new ArrayList<String>());
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
        this.featureIds = featureIds;
    }

    /**
     * Get the ids of all the commands that do not belong to features
     * @return the ids of all the commands that do not belong to features
     */
    public List<String> getCustomCommandIds() {
        return customCommandIds;
    }

    public void setCustomCommandIds(List<String> customCommandIds) {
        this.customCommandIds = customCommandIds;
    }

    /**
     * Get the ids of all the values that do not belong to features
     * @return the ids of all the values that do not belong to features
     */
    public List<String> getCustomValueIds() {
        return customValueIds;
    }

    public void setCustomValueIds(List<String> customValueIds) {
        this.customValueIds = customValueIds;
    }

    /**
     * Get the ids of all the properties that do not belong to features
     * @return the ids of all the properties that do not belong to features
     */
    public List<String> getCustomPropertyIds() {
        return customPropertyIds;
    }

    public void setCustomPropertyIds(List<String> customPropertyIds) {
        this.customPropertyIds = customPropertyIds;
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
        if(!iterablesEqual(other.featureIds, featureIds))
            return false;
        if(!iterablesEqual(other.customCommandIds, customCommandIds))
            return false;
        if(!iterablesEqual(other.customValueIds, customValueIds))
            return false;
        if(!iterablesEqual(other.customPropertyIds, customPropertyIds))
            return false;
        if(!other.getChildData(COMMANDS_ID).equals(getChildData(COMMANDS_ID)))
            return false;
        if(!other.getChildData(VALUES_ID).equals(getChildData(VALUES_ID)))
            return false;
        if(!other.getChildData(PROPERTIES_ID).equals(getChildData(PROPERTIES_ID)))
            return false;
        return true;
    }

    public static <T> boolean iterablesEqual(List<T> one, List<T> two) {

        // get iterator for both iterables
        Iterator<T> oneIter = one.iterator();
        Iterator<T> twoIter = two.iterator();

        // while they both have elements, check each element is equal
        while(oneIter.hasNext() && twoIter.hasNext())
            if(!oneIter.next().equals(twoIter.next()))
                return false;

        // check both iterators are finished
        return !oneIter.hasNext() && !twoIter.hasNext();
    }

    @Override
    public void ensureSerialisable() {
        super.ensureSerialisable();
        if(featureIds != null && (featureIds instanceof ArrayList))
            featureIds = newArrayList(featureIds);
        if(customCommandIds != null && (customCommandIds instanceof ArrayList))
            customCommandIds = newArrayList(customCommandIds);
        if(customValueIds != null && (customValueIds instanceof ArrayList))
            customValueIds = newArrayList(customValueIds);
        if(customPropertyIds != null && (customPropertyIds instanceof ArrayList))
            customPropertyIds = newArrayList(customPropertyIds);
    }

    private static <T> ArrayList<T> newArrayList(List<T> iterable) {
        ArrayList<T> result = new ArrayList<>();
        for(T element : iterable)
            result.add(element);
        return result;
    }
}
