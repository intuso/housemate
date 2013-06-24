package com.intuso.housemate.web.client.activity;

import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.place.AutomationPlace;
import com.intuso.housemate.web.client.ui.view.AutomationView;

/**
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
