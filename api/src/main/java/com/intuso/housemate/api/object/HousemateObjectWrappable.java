package com.intuso.housemate.api.object;

import com.intuso.housemate.api.comms.Message;
import com.intuso.utilities.wrapper.Wrappable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Base data object for any Housemate object
 * @param <WBL> the type of the data object for the children of this Housemate object
 */
public abstract class HousemateObjectWrappable<WBL extends HousemateObjectWrappable<?>>
        extends Wrappable<WBL>
        implements Message.Payload {

    private String name;
    private String description;

    protected HousemateObjectWrappable() {}

    /**
     * @param id object's id
     * @param name object's name
     * @param description object's description
     * @param subWrappables object's child wrappables
     */
    public HousemateObjectWrappable(String id, String name, String description, WBL ... subWrappables) {
        this(id, name, description, Arrays.asList(subWrappables));
    }

    /**
     * @param id object's id
     * @param name object's name
     * @param description object's description
     * @param subWrappables object's child wrappables
     */
    public HousemateObjectWrappable(String id, String name, String description, List<WBL> subWrappables) {
        super(id, subWrappables);
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

    /**
     * Gets the object's description
     * @return the object's description
     */
    public final String getDescription() {
        return description;
    }

    /**
     * Clone the Housemate data object
     * @return a clone of this object
     */
    public abstract HousemateObjectWrappable clone();

    /**
     * Deep clone this object, by also cloning all of its child data objects
     * @return a deep cloned copy of this data object
     */
    public final HousemateObjectWrappable deepClone() {
        HousemateObjectWrappable result = clone();
        for(Map.Entry<String, WBL> child : getSubWrappables().entrySet())
            result.addWrappable(child.getValue().deepClone());
        return result;
    }
}
