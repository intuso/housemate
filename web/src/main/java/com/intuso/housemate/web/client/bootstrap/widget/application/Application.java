package com.intuso.housemate.web.client.bootstrap.widget.application;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.web.client.bootstrap.widget.object.ObjectWidget;
import com.intuso.housemate.web.client.object.GWTProxyApplication;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;
import org.gwtbootstrap3.client.ui.constants.AlertType;

/**
 * Created by tomc on 05/03/15.
 */
public class Application extends ObjectWidget<GWTProxyApplication> {

    private final GWTProxyList<TypeData<?>, GWTProxyType> types;

    public Application(GWTProxyList<TypeData<?>, GWTProxyType> types, final GWTProxyList<ApplicationData, GWTProxyApplication> applications, final ChildOverview childOverview) {
        this.types = types;
        GWTProxyApplication user = applications.get(childOverview.getId());
        if(user != null)
            setObject(user);
        else {
            setName(childOverview.getName());
            loading(true);
            applications.load(new LoadManager(new LoadManager.Callback() {
                @Override
                public void failed(HousemateObject.TreeLoadInfo path) {
                    loading(false);
                    setMessage(AlertType.WARNING, "Failed to load application");
                }

                @Override
                public void allLoaded() {
                    loading(false);
                    setObject(applications.get(childOverview.getId()));
                }
            }, "loadApplication-" + childOverview.getId(),
                    new HousemateObject.TreeLoadInfo(childOverview.getId(), new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE))));
        }
    }
    
    @Override
    protected IsWidget getBodyWidget(GWTProxyApplication object) {
        return new ApplicationBody(object);
    }

    @Override
    protected IsWidget getSettingsWidget(GWTProxyApplication object) {
        return new ApplicationSettings(types, object);
    }
}
