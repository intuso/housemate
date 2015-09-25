package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.comms.v1_0.api.ChildOverview;
import com.intuso.housemate.comms.v1_0.api.payload.ParameterData;
import com.intuso.housemate.web.client.object.GWTProxyParameter;

/**
 */
public class ParameterInputList extends TypeInputList<ParameterData, GWTProxyParameter> {

    @Override
    protected IsWidget getWidget(ChildOverview childOverview, final GWTProxyParameter parameter) {
        return getWidget(parameter.getTypeId(), parameter.getId());
    }
}
