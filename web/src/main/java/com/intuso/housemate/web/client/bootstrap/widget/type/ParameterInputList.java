package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.web.client.object.GWTProxyParameter;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 */
public class ParameterInputList extends TypeInputList<ParameterData, GWTProxyParameter> {

    @Override
    protected IsWidget getWidget(ChildOverview childOverview, final GWTProxyParameter parameter) {
        GWTProxyType type = parameter.getType();
        if(type != null)
            return getWidget(parameter.getType(), parameter.getId());
        else {
            final SimplePanel panel = new SimplePanel();
            // todo
            /*Housemate.INJECTOR.getProxyRoot().getTypes().load(new LoadManager(new LoadManager.Callback() {
                @Override
                public void failed(HousemateObject.TreeLoadInfo path) {
                    // todo show error
                }

                @Override
                public void allLoaded() {
                    panel.setWidget(getWidget(parameter.getType(), parameter.getId()));
                }
            }, "loadParameterType-" + parameter.getId(), new HousemateObject.TreeLoadInfo(parameter.getTypeId(), new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE))));*/
            return panel;
        }
    }
}
