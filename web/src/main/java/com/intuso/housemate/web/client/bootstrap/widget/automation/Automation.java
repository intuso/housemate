package com.intuso.housemate.web.client.bootstrap.widget.automation;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.object.ObjectWidget;
import com.intuso.housemate.web.client.object.GWTProxyAutomation;
import org.gwtbootstrap3.client.ui.constants.AlertType;

/**
 * Created by tomc on 05/03/15.
 */
public class Automation extends ObjectWidget<GWTProxyAutomation> {

    public Automation(final ChildOverview childOverview) {
        setName(childOverview.getName());
        Housemate.INJECTOR.getProxyRoot().getAutomations().load(new LoadManager(new LoadManager.Callback() {
            @Override
            public void failed(HousemateObject.TreeLoadInfo path) {
                setMessage(AlertType.WARNING, "Failed to load automation");
            }

            @Override
            public void allLoaded() {
                setObject(Housemate.INJECTOR.getProxyRoot().getAutomations().get(childOverview.getId()));
            }
        }, "loadAutomation-" + childOverview.getId(),
                new HousemateObject.TreeLoadInfo(childOverview.getId(), new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE))));
    }

    public Automation(GWTProxyAutomation automation) {
        setObject(automation);
    }

    @Override
    protected IsWidget getBodyWidget(GWTProxyAutomation object) {
        return new AutomationBody(object);
    }

    @Override
    protected IsWidget getSettingsWidget(GWTProxyAutomation object) {
        return new AutomationSettings(object);
    }
}
