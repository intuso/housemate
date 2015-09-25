package com.intuso.housemate.web.client.bootstrap.widget.application;

import com.intuso.housemate.comms.v1_0.api.ChildOverview;
import com.intuso.housemate.comms.v1_0.api.payload.ApplicationData;
import com.intuso.housemate.comms.v1_0.api.payload.TypeData;
import com.intuso.housemate.web.client.bootstrap.widget.LazyLoadedWidgetCallback;
import com.intuso.housemate.web.client.bootstrap.widget.list.MainList;
import com.intuso.housemate.web.client.object.GWTProxyApplication;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/09/13
 * Time: 00:39
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationList extends MainList<ApplicationData, GWTProxyApplication> {

    private final GWTProxyList<TypeData<?>, GWTProxyType> types;
    private final GWTProxyList<ApplicationData, GWTProxyApplication> applications;

    public ApplicationList(String title, GWTProxyList<TypeData<?>, GWTProxyType> types, GWTProxyList<ApplicationData, GWTProxyApplication> applications, GWTProxyCommand addCommand) {
        super(title, types, addCommand);
        this.types = types;
        this.applications = applications;
        setList(applications);
    }

    @Override
    protected void getWidget(ChildOverview childOverview, LazyLoadedWidgetCallback callback) {
        callback.widgetReady(new Application(types, applications, childOverview));
    }
}
