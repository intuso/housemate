package com.intuso.housemate.web.client.bootstrap.view;

import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.automation.AutomationWrappable;
import com.intuso.housemate.web.client.GWTResources;
import com.intuso.housemate.web.client.bootstrap.widget.automation.Automation;
import com.intuso.housemate.web.client.object.GWTProxyAutomation;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.place.AutomationPlace;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 10/03/12
 * Time: 09:31
 * To change this template use File | Settings | File Templates.
 */
public class AutomationView extends ObjectListView<GWTProxyAutomation, AutomationPlace> implements com.intuso.housemate.web.client.ui.view.AutomationView {

    public AutomationView(GWTResources<?> resources) {
        super(resources);
    }

    @Override
    protected GWTProxyList<AutomationWrappable, GWTProxyAutomation> getList(AutomationPlace place) {
        return resources.getRoot().getAutomations();
    }

    @Override
    protected GWTProxyCommand getAddCommand(AutomationPlace place) {
        return resources.getRoot().getAddAutomationCommand();
    }

    @Override
    protected String getSelectedObjectName(AutomationPlace place) {
        return place.getAutomationName();
    }

    @Override
    protected Widget getObjectWidget(AutomationPlace place, GWTProxyAutomation automation) {
        return new Automation(automation);
    }

    @Override
    protected AutomationPlace getPlace(AutomationPlace place, GWTProxyAutomation automation) {
        if(automation == null)
            return new AutomationPlace();
        else
            return new AutomationPlace(automation.getId());
    }
}