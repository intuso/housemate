package com.intuso.housemate.web.client.bootstrap.widget.automation;

import com.intuso.housemate.comms.v1_0.api.ChildOverview;
import com.intuso.housemate.comms.v1_0.api.payload.AutomationData;
import com.intuso.housemate.comms.v1_0.api.payload.TypeData;
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
public class AutomationList extends MainList<AutomationData, GWTProxyAutomation> {

    private final GWTProxyList<TypeData<?>, GWTProxyType> types;
    private final GWTProxyList<AutomationData, GWTProxyAutomation> automations;

    public AutomationList(String title, GWTProxyList<TypeData<?>, GWTProxyType> types, GWTProxyList<AutomationData, GWTProxyAutomation> automations, GWTProxyCommand addCommand) {
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
