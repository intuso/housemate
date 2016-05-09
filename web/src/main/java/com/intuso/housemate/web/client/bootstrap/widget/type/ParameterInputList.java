package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.client.v1_0.api.object.Parameter;
import com.intuso.housemate.client.v1_0.data.api.ChildOverview;
import com.intuso.housemate.web.client.object.GWTProxyParameter;

/**
 */
public class ParameterInputList extends TypeInputList<Parameter.Data, GWTProxyParameter> {

    @Override
    protected IsWidget getWidget(ChildOverview childOverview, final GWTProxyParameter parameter) {
        return getWidget(parameter.getTypeId(), parameter.getId());
    }
}
