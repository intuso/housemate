package com.intuso.housemate.web.client.activity;

import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.place.SatisfiedTaskPlace;
import com.intuso.housemate.web.client.ui.view.SatisfiedTaskView;

/**
 */
public class SatisfiedTaskActivity extends HousemateActivity<SatisfiedTaskPlace, SatisfiedTaskView> {

    protected SatisfiedTaskActivity(SatisfiedTaskPlace place) {
        super(place);
    }

    @Override
    public SatisfiedTaskView getView() {
        return Housemate.FACTORY.getSatisfiedTaskView();
    }
}
