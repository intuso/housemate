package com.intuso.housemate.web.client.bootstrap.widget.application;

import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.web.client.bootstrap.widget.LazyLoadedWidgetCallback;
import com.intuso.housemate.web.client.bootstrap.widget.list.MainList;
import com.intuso.housemate.web.client.object.GWTProxyApplication;
import com.intuso.housemate.web.client.object.GWTProxyList;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/09/13
 * Time: 00:39
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationList extends MainList<ApplicationData, GWTProxyApplication> {

    private final GWTProxyList<ApplicationData, GWTProxyApplication> applications;

    public ApplicationList(GWTProxyList<ApplicationData, GWTProxyApplication> applications, String name) {
        super(name, null, true);
        this.applications = applications;
        setList(applications);
    }

    @Override
    protected void getWidget(ChildOverview childOverview, LazyLoadedWidgetCallback callback) {
        callback.widgetReady(new Application(applications, childOverview));
    }
}
