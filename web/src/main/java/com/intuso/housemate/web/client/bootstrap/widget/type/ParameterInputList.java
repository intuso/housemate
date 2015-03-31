package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyParameter;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 */
public class ParameterInputList extends TypeInputList<ParameterData, GWTProxyParameter> {

    public ParameterInputList(GWTProxyList<TypeData<?>, GWTProxyType> types) {
        super(types);
    }

    @Override
    protected IsWidget getWidget(ChildOverview childOverview, final GWTProxyParameter parameter) {
        return getWidget(parameter.getTypeId(), parameter.getId());
    }
}
