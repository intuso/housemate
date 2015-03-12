package com.intuso.housemate.web.client.bootstrap.widget.application;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.object.ObjectWidget;
import com.intuso.housemate.web.client.object.GWTProxyApplication;
import org.gwtbootstrap3.client.ui.constants.AlertType;

/**
 * Created by tomc on 05/03/15.
 */
public class Application extends ObjectWidget<GWTProxyApplication> {

    public Application(final ChildOverview childOverview) {
        GWTProxyApplication user = Housemate.INJECTOR.getProxyRoot().getApplications().get(childOverview.getId());
        if(user != null)
            setObject(user);
        else {
            setName(childOverview.getName());
            loading(true);
            Housemate.INJECTOR.getProxyRoot().getApplications().load(new LoadManager(new LoadManager.Callback() {
                @Override
                public void failed(HousemateObject.TreeLoadInfo path) {
                    loading(false);
                    setMessage(AlertType.WARNING, "Failed to load application");
                }

                @Override
                public void allLoaded() {
                    loading(false);
                    setObject(Housemate.INJECTOR.getProxyRoot().getApplications().get(childOverview.getId()));
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
        return new ApplicationSettings(object);
    }
}
