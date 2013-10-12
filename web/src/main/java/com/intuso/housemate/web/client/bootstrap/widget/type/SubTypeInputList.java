package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxySubType;

/**
 */
public class SubTypeInputList extends TypeInputList<SubTypeData, GWTProxySubType> {

    public SubTypeInputList(GWTProxyList<SubTypeData, GWTProxySubType> list, TypeInstances typeInstances) {
        super(list, typeInstances);
    }

    @Override
    protected IsWidget getWidget(GWTProxySubType subType) {
        return getWidget(subType.getType(), subType.getId());
    }
}
