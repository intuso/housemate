package com.intuso.housemate.api.object.option;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.subtype.SubTypeData;

/**
 *
 * Data object for an option
 */
public class OptionData extends HousemateData<ListData<SubTypeData>> {

    protected OptionData() {}

    public OptionData(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateData clone() {
        return new OptionData(getId(), getName(), getDescription());
    }
}
