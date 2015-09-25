package com.intuso.housemate.web.client.bootstrap.widget.application.instance;

import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.comms.v1_0.api.ChildOverview;
import com.intuso.housemate.comms.v1_0.api.payload.ApplicationInstanceData;
import com.intuso.housemate.web.client.bootstrap.widget.list.NestedList;
import com.intuso.housemate.web.client.object.GWTProxyApplicationInstance;

public class ApplicationInstanceList extends NestedList<ApplicationInstanceData, GWTProxyApplicationInstance> {

    @Override
    protected Widget getWidget(ChildOverview childOverview, GWTProxyApplicationInstance applicationInstance) {
        return new ApplicationInstance(applicationInstance);
    }
}