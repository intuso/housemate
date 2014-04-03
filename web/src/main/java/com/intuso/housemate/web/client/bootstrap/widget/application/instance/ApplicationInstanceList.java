package com.intuso.housemate.web.client.bootstrap.widget.application.instance;

import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceData;
import com.intuso.housemate.web.client.bootstrap.widget.list.ComplexWidgetList;
import com.intuso.housemate.web.client.object.GWTProxyApplicationInstance;
import com.intuso.housemate.web.client.object.GWTProxyList;

import java.util.List;

public class ApplicationInstanceList extends ComplexWidgetList<ApplicationInstanceData, GWTProxyApplicationInstance> {

    public ApplicationInstanceList(GWTProxyList<ApplicationInstanceData, GWTProxyApplicationInstance> list, String title,
                       List<String> filteredIds, boolean showOnEmpty) {
        super(list, title, filteredIds, showOnEmpty);
    }

    @Override
    protected Widget getWidget(GWTProxyApplicationInstance applicationInstance) {
        return new ApplicationInstance(applicationInstance);
    }
}