package com.intuso.housemate.web.client.bootstrap.widget.automation;

import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.web.client.bootstrap.widget.LazyLoadedWidgetCallback;
import com.intuso.housemate.web.client.bootstrap.widget.list.MainList;
import com.intuso.housemate.web.client.object.GWTProxyAutomation;
import com.intuso.housemate.web.client.object.GWTProxyList;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/09/13
 * Time: 00:39
 * To change this template use File | Settings | File Templates.
 */
public class AutomationList extends MainList<AutomationData, GWTProxyAutomation> {

    private final GWTProxyList<AutomationData, GWTProxyAutomation> automations;

    public AutomationList(GWTProxyList<AutomationData, GWTProxyAutomation> automations, String name) {
        super(automations, name, null, true);
        this.automations = automations;
    }

    @Override
    protected void getWidget(ChildOverview childOverview, LazyLoadedWidgetCallback callback) {
        callback.widgetReady(new Automation(automations, childOverview));
    }
}
