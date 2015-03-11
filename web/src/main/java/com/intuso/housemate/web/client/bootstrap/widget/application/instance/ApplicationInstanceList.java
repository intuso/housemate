package com.intuso.housemate.web.client.bootstrap.widget.application.instance;

import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceData;
import com.intuso.housemate.web.client.bootstrap.widget.list.NestedList;
import com.intuso.housemate.web.client.object.GWTProxyApplicationInstance;

public class ApplicationInstanceList extends NestedList<ApplicationInstanceData, GWTProxyApplicationInstance> {

    @Override
    protected Widget getWidget(ChildOverview childOverview, GWTProxyApplicationInstance applicationInstance) {
        return new ApplicationInstance(applicationInstance);
    }
}