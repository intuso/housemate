package com.intuso.housemate.web.client.activity;

import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.place.ConditionPlace;
import com.intuso.housemate.web.client.ui.view.ConditionView;

/**
 */
public class ConditionActivity extends HousemateActivity<ConditionPlace, ConditionView> {

    protected ConditionActivity(ConditionPlace place) {
        super(place);
    }

    @Override
    public ConditionView getView() {
        return Housemate.FACTORY.getConditionView();
    }
}
