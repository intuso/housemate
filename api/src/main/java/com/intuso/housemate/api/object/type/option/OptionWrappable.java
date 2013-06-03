package com.intuso.housemate.api.object.type.option;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.NoChildrenWrappable;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 29/05/12
 * Time: 21:05
 * To change this template use File | Settings | File Templates.
 */
public class OptionWrappable extends HousemateObjectWrappable<NoChildrenWrappable> {

    private List<String> typeIds;

    protected OptionWrappable() {}

    public OptionWrappable(String id, String name, String description, String ... typesIds) {
        this(id, name, description, Arrays.asList(typesIds));
    }

    public OptionWrappable(String id, String name, String description, List<String> typesIds) {
        super(id, name, description);
        this.typeIds = typesIds;
    }

    public List<String> getSubTypes() {
        return typeIds;
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new OptionWrappable(getId(), getName(), getDescription());
    }
}
