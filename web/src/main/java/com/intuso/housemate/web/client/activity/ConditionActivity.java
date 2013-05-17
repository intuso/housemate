package com.intuso.housemate.web.client.activity;

import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.place.ConditionPlace;
import com.intuso.housemate.web.client.ui.view.ConditionView;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 24/11/12
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
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
