package com.intuso.housemate.comms.api.internal.payload;

import java.util.Arrays;
import java.util.Map;

/**
 * Data object for a list
 * @param <DATA> The data object for the objects in the list
 */
public final class ListData<DATA extends HousemateData<?>> extends HousemateData<DATA> {

    private static final long serialVersionUID = -1L;

    public ListData() {}

    public ListData(String id, String name, String description, DATA... childData) {
        this(id, name, description, Arrays.asList(childData));
    }

    public ListData(String id, String name, String description, java.util.List<DATA> childData) {
        super(id, name, description, childData);
    }

    @Override
    public HousemateData clone() {
        return new ListData(getId(), getName(), getDescription());
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof ListData))
            return false;
        ListData<DATA> other = (ListData<DATA>)o;
        if(other.getChildData().size() != getChildData().size())
            return false;
        for(Map.Entry<String, ? extends HousemateData<?>> entry : getChildData().entrySet()) {
            if(!getChildData().containsKey(entry.getKey()))
                return false;
            else if(!getChildData().get(entry.getKey()).equals(entry.getValue()))
                return false;
        }
        return true;
    }
}
