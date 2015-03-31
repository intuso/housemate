package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxySubType;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 * Created by tomc on 11/03/15.
 */
public class SubTypeList extends TypeInputList<SubTypeData, GWTProxySubType> {

    public SubTypeList(GWTProxyList<TypeData<?>, GWTProxyType> types) {
        super(types);
    }

    @Override
    protected IsWidget getWidget(ChildOverview childOverview, GWTProxySubType subType) {
        return getWidget(subType.getTypeId(), subType.getId());
    }
}
