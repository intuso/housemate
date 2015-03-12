package com.intuso.housemate.web.client.bootstrap.widget.application;

import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.LazyLoadedWidgetCallback;
import com.intuso.housemate.web.client.bootstrap.widget.list.MainList;
import com.intuso.housemate.web.client.object.GWTProxyApplication;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/09/13
 * Time: 00:39
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationList extends MainList<ApplicationData, GWTProxyApplication> {

    public ApplicationList(String title, List<String> filteredIds, boolean includeFiltered) {
        super(Housemate.INJECTOR.getProxyRoot().getApplications(), title, filteredIds, includeFiltered);
    }

    @Override
    protected void getWidget(ChildOverview childOverview, LazyLoadedWidgetCallback callback) {
        callback.widgetReady(new Application(childOverview));
    }
}
