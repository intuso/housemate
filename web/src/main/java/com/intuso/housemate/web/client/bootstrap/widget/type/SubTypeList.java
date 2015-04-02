package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.web.client.object.GWTProxySubType;

/**
 * Created by tomc on 11/03/15.
 */
public class SubTypeList extends TypeInputList<SubTypeData, GWTProxySubType> {

    @Override
    protected IsWidget getWidget(ChildOverview childOverview, GWTProxySubType subType) {
        return getWidget(subType.getTypeId(), subType.getId());
    }
}
