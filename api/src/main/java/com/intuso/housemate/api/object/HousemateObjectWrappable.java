package com.intuso.housemate.api.object;

import com.intuso.housemate.api.comms.Message;
import com.intuso.utilities.wrapper.Wrappable;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 26/06/12
 * Time: 15:48
 * To change this template use File | Settings | File Templates.
 */
public abstract class HousemateObjectWrappable<WBL extends HousemateObjectWrappable<?>>
        extends Wrappable<WBL>
        implements Message.Payload {

    private String name;
    private String description;

    protected HousemateObjectWrappable() {}

    public HousemateObjectWrappable(String id, String name, String description) {
        super(id);
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public final String getDescription() {
        return description;
    }

    public abstract HousemateObjectWrappable clone();

    public final HousemateObjectWrappable deepClone() {
        HousemateObjectWrappable result = clone();
        for(Map.Entry<String, WBL> child : getSubWrappables().entrySet())
            result.addWrappable(child.getValue().deepClone());
        return result;
    }
}
