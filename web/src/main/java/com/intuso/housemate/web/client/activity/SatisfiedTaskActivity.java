package com.intuso.housemate.web.client.activity;

import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.place.SatisfiedTaskPlace;
import com.intuso.housemate.web.client.ui.view.SatisfiedTaskView;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 24/11/12
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
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
