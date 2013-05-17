package com.intuso.housemate.core.object.type;

import com.intuso.housemate.core.object.HousemateObjectWrappable;
import com.intuso.housemate.core.object.list.ListWrappable;
import com.intuso.housemate.core.object.type.option.OptionWrappable;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 29/05/12
 * Time: 21:05
 * To change this template use File | Settings | File Templates.
 */
public final class MultiChoiceTypeWrappable extends TypeWrappable<ListWrappable<OptionWrappable>> {

    private MultiChoiceTypeWrappable() {}

    public MultiChoiceTypeWrappable(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new MultiChoiceTypeWrappable(getId(), getName(), getDescription());
    }
}
