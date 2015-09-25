package com.intuso.housemate.comms.api.internal.payload;

import com.intuso.housemate.comms.api.internal.Message;
import com.intuso.utilities.object.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base data object for any Housemate object
 * @param <DATA> the type of the data object for the children of this Housemate object
 */
public abstract class HousemateData<DATA extends HousemateData<?>>
        extends Data<DATA>
        implements Message.Payload {

    private static final long serialVersionUID = -1L;

    private String name;
    private String description;

    protected HousemateData() {}

    /**
     * @param id object's id
     * @param name object's name
     * @param description object's description
     * @param childData object's child datas
     */
    public HousemateData(String id, String name, String description, DATA... childData) {
        this(id, name, description, Arrays.asList(childData));
    }

    /**
     * @param id object's id
     * @param name object's name
     * @param description object's description
     * @param childData object's child datas
     */
    public HousemateData(String id, String name, String description, List<DATA> childData) {
        super(id, childData);
        this.name = name;
        this.description = description;
    }

    /**
     * Gets the name of the object
     * @return the name of the object
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the object's description
     * @return the object's description
     */
    public final String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Clone the Housemate data object
     * @return a clone of this object
     */
    public abstract HousemateData clone();

    /**
     * Deep clone this object, by also cloning all of its child data objects
     * @return a deep cloned copy of this data object
     */
    public final HousemateData deepClone() {
        HousemateData result = clone();
        for(Map.Entry<String, DATA> child : getChildData().entrySet())
            result.addChildData(child.getValue().deepClone());
        return result;
    }

    @Override
    public boolean equals(Object o) {
        return getClass().equals(o.getClass()) && ((HousemateData)o).getId().equals(getId());
    }

    @Override
    public void ensureSerialisable() {
        if(getChildData() != null && !(getChildData() instanceof HashMap))
            setChildData(new HashMap<>(getChildData()));
        if(getChildData() != null)
            for(HousemateData<?> childData : getChildData().values())
                childData.ensureSerialisable();
    }
}
