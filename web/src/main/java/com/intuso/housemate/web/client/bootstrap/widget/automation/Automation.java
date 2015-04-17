package com.intuso.housemate.web.client.bootstrap.widget.automation;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.web.client.bootstrap.widget.object.ObjectWidget;
import com.intuso.housemate.web.client.object.GWTProxyAutomation;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;
import org.gwtbootstrap3.client.ui.constants.AlertType;

import java.util.List;

/**
 * Created by tomc on 05/03/15.
 */
public class Automation extends ObjectWidget<GWTProxyAutomation> {

    private final GWTProxyList<TypeData<?>, GWTProxyType> types;

    public Automation(GWTProxyList<TypeData<?>, GWTProxyType> types, final GWTProxyList<AutomationData, GWTProxyAutomation> automations, final ChildOverview childOverview) {
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
            }, new HousemateObject.TreeLoadInfo(childOverview.getId(), new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE))));
        }
    }

    @Override
    protected IsWidget getBodyWidget(GWTProxyAutomation object) {
        return new AutomationBody(types, object);
    }

    @Override
    protected IsWidget getSettingsWidget(GWTProxyAutomation object) {
        return new AutomationSettings(types, object);
    }
}
