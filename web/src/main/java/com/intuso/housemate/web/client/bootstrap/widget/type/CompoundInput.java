package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.type.CompoundTypeData;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxySubType;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 */
public class CompoundInput extends SubTypeInputList implements TypeInput {

    private final GWTProxyType type;
    private TypeInstances typeInstances;

    public CompoundInput(CompoundTypeData typeData, GWTProxyType type, TypeInstances typeInstances) {
        super((GWTProxyList<SubTypeData, GWTProxySubType>) type.getChild("sub-types"), typeInstances);
        this.type = type;
    }
}
