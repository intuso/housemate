package com.intuso.housemate.web.client.bootstrap.widget.automation;

import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.list.ObjectList;
import com.intuso.housemate.web.client.object.GWTProxyAutomation;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/09/13
 * Time: 00:39
 * To change this template use File | Settings | File Templates.
 */
public class AutomationList extends ObjectList<AutomationData, GWTProxyAutomation> {
    
    public AutomationList(String title, List<String> filteredIds, boolean includeFiltered) {
        super(Housemate.ENVIRONMENT.getResources().getRoot().getAutomations(), title, filteredIds, includeFiltered);
    }

    @Override
    protected Widget getWidget(GWTProxyAutomation object) {
        return new Automation(object);
    }
}
