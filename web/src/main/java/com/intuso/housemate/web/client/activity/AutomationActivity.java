package com.intuso.housemate.web.client.activity;

import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.place.AutomationPlace;
import com.intuso.housemate.web.client.ui.view.AutomationView;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 24/11/12
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
 */
public class AutomationActivity extends HousemateActivity<AutomationPlace, AutomationView> {

    protected AutomationActivity(AutomationPlace place) {
        super(place);
    }

    @Override
    public AutomationView getView() {
        return Housemate.FACTORY.getAutomationView();
    }
}
