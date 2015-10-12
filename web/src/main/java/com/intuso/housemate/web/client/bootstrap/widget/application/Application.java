package com.intuso.housemate.web.client.bootstrap.widget.application;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.client.v1_0.proxy.api.LoadManager;
import com.intuso.housemate.comms.v1_0.api.ChildOverview;
import com.intuso.housemate.comms.v1_0.api.RemoteObject;
import com.intuso.housemate.comms.v1_0.api.TreeLoadInfo;
import com.intuso.housemate.comms.v1_0.api.payload.ApplicationData;
import com.intuso.housemate.comms.v1_0.api.payload.TypeData;
import com.intuso.housemate.web.client.bootstrap.widget.object.ObjectWidget;
import com.intuso.housemate.web.client.object.GWTProxyApplication;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;
import org.gwtbootstrap3.client.ui.constants.AlertType;

import java.util.List;

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
                public void failed(List<String> errors) {
                    loading(false);
                    setMessage(AlertType.WARNING, "Failed to load application");
                }

                @Override
                public void succeeded() {
                    loading(false);
                    setObject(applications.get(childOverview.getId()));
                }
            }, new TreeLoadInfo(childOverview.getId(), new TreeLoadInfo(RemoteObject.EVERYTHING_RECURSIVE))));
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
