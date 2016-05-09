package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.client.v1_0.api.object.SubType;
import com.intuso.housemate.client.v1_0.data.api.ChildOverview;
import com.intuso.housemate.web.client.object.GWTProxySubType;

/**
 * Created by tomc on 11/03/15.
 */
public class SubTypeList extends TypeInputList<SubType.Data, GWTProxySubType> {

    @Override
    protected IsWidget getWidget(ChildOverview childOverview, GWTProxySubType subType) {
        return getWidget(subType.getTypeId(), subType.getId());
    }
}
