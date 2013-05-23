package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.type.option.OptionWrappable;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 29/05/12
 * Time: 21:05
 * To change this template use File | Settings | File Templates.
 */
public final class SingleChoiceTypeWrappable extends TypeWrappable<ListWrappable<OptionWrappable>> {

    private SingleChoiceTypeWrappable() {}

    public SingleChoiceTypeWrappable(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new SingleChoiceTypeWrappable(getId(), getName(), getDescription());
    }
}
