package com.intuso.housemate.web.client.bootstrap.widget.automation;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.proxy.api.LoadManager;
import com.intuso.housemate.client.v1_0.data.api.ChildOverview;
import com.intuso.housemate.client.v1_0.data.api.RemoteObject;
import com.intuso.housemate.client.v1_0.data.api.TreeLoadInfo;
import com.intuso.housemate.web.client.bootstrap.widget.object.ObjectWidget;
import com.intuso.housemate.web.client.object.GWTProxyAutomation;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;
import org.gwtbootstrap3.client.ui.constants.AlertType;

import java.util.List;

/**
 * Created by tomc on 05/03/15.
 */
public class Automation extends ObjectWidget<GWTProxyAutomation> implements com.intuso.housemate.client.v1_0.api.object.Automation.Listener<GWTProxyAutomation> {

    private final GWTProxyList<Type.Data<?>, GWTProxyType> types;

    public Automation(GWTProxyList<Type.Data<?>, GWTProxyType> types, final GWTProxyList<com.intuso.housemate.client.v1_0.api.object.Automation.Data, GWTProxyAutomation> automations, final ChildOverview childOverview) {
        this.types = types;
        GWTProxyAutomation user = automations.get(childOverview.getId());
        if(user != null)
            setObject(user);
        else {
            setName(childOverview.getName());
            loading(true);
            automations.load(new LoadManager(new LoadManager.Callback() {
                @Override
                public void failed(List<String> errors) {
                    loading(false);
                    setMessage(AlertType.WARNING, "Failed to load automation");
                }

                @Override
                public void succeeded() {
                    loading(false);
                    setObject(automations.get(childOverview.getId()));
                }
            }, new TreeLoadInfo(childOverview.getId(), new TreeLoadInfo(RemoteObject.EVERYTHING_RECURSIVE))));
        }
    }

    @Override
    protected void setObject(GWTProxyAutomation automation) {
        super.setObject(automation);
        automation.addObjectListener(this);
    }

    @Override
    protected IsWidget getBodyWidget(GWTProxyAutomation object) {
        return new AutomationBody(types, object);
    }

    @Override
    protected IsWidget getSettingsWidget(GWTProxyAutomation object) {
        return new AutomationSettings(types, object);
    }

    @Override
    public void renamed(GWTProxyAutomation automation, String oldName, String newName) {
        updateName(newName);
    }

    @Override
    public void satisfied(GWTProxyAutomation automation, boolean satisfied) {

    }

    @Override
    public void running(GWTProxyAutomation runnable, boolean running) {

    }

    @Override
    public void error(GWTProxyAutomation failable, String error) {

    }
}
