package com.intuso.housemate.web.client.bootstrap.widget.automation;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.web.client.bootstrap.widget.object.ObjectWidget;
import com.intuso.housemate.web.client.object.GWTProxyAutomation;
import com.intuso.housemate.web.client.object.GWTProxyList;
import org.gwtbootstrap3.client.ui.constants.AlertType;

/**
 * Created by tomc on 05/03/15.
 */
public class Automation extends ObjectWidget<GWTProxyAutomation> {

    public Automation(final GWTProxyList<AutomationData, GWTProxyAutomation> automations, final ChildOverview childOverview) {
        GWTProxyAutomation user = automations.get(childOverview.getId());
        if(user != null)
            setObject(user);
        else {
            setName(childOverview.getName());
            loading(true);
            automations.load(new LoadManager(new LoadManager.Callback() {
                @Override
                public void failed(HousemateObject.TreeLoadInfo path) {
                    loading(false);
                    setMessage(AlertType.WARNING, "Failed to load automation");
                }

                @Override
                public void allLoaded() {
                    loading(false);
                    setObject(automations.get(childOverview.getId()));
                }
            }, "loadAutomation-" + childOverview.getId(),
                    new HousemateObject.TreeLoadInfo(childOverview.getId(), new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE))));
        }
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
