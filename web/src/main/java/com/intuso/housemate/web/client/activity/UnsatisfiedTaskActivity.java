package com.intuso.housemate.web.client.activity;

import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.place.UnsatisfiedTaskPlace;
import com.intuso.housemate.web.client.ui.view.UnsatisfiedTaskView;

/**
 */
public class UnsatisfiedTaskActivity extends HousemateActivity<UnsatisfiedTaskPlace, UnsatisfiedTaskView> {

    protected UnsatisfiedTaskActivity(UnsatisfiedTaskPlace place) {
        super(place);
    }

    @Override
    public UnsatisfiedTaskView getView() {
        return Housemate.FACTORY.getUnsatisfiedTaskView();
    }
}
