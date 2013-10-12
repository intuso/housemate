package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyParameter;

/**
 */
public class ParameterInputList extends TypeInputList<ParameterData, GWTProxyParameter> {

    public ParameterInputList(GWTProxyList<ParameterData, GWTProxyParameter> list, TypeInstances typeInstances) {
        super(list, typeInstances);
    }

    @Override
    protected IsWidget getWidget(GWTProxyParameter parameter) {
        return getWidget(parameter.getType(), parameter.getId());
    }
}
