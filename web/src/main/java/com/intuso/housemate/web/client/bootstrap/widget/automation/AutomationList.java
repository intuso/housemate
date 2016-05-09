package com.intuso.housemate.web.client.bootstrap.widget.automation;

import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.data.api.ChildOverview;
import com.intuso.housemate.web.client.bootstrap.widget.LazyLoadedWidgetCallback;
import com.intuso.housemate.web.client.bootstrap.widget.list.MainList;
import com.intuso.housemate.web.client.object.GWTProxyAutomation;
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
public class AutomationList extends MainList<com.intuso.housemate.client.v1_0.api.object.Automation.Data, GWTProxyAutomation> {

    private final GWTProxyList<Type.Data<?>, GWTProxyType> types;
    private final GWTProxyList<com.intuso.housemate.client.v1_0.api.object.Automation.Data, GWTProxyAutomation> automations;

    public AutomationList(String title, GWTProxyList<Type.Data<?>, GWTProxyType> types, GWTProxyList<com.intuso.housemate.client.v1_0.api.object.Automation.Data, GWTProxyAutomation> automations, GWTProxyCommand addCommand) {
        super(title, types, addCommand);
        this.types = types;
        this.automations = automations;
        setList(automations);
    }

    @Override
    protected void getWidget(ChildOverview childOverview, LazyLoadedWidgetCallback callback) {
        callback.widgetReady(new Automation(types, automations, childOverview));
    }
}
