package com.intuso.housemate.web.client.activity;

import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.place.AutomationsPlace;
import com.intuso.housemate.web.client.ui.view.AutomationView;

/**
 */
public class AutomationActivity extends HousemateActivity<AutomationsPlace, AutomationView> {

    protected AutomationActivity(AutomationsPlace place) {
        super(place);
    }

    @Override
    public AutomationView getView() {
        return Housemate.FACTORY.getAutomationView();
    }
}
