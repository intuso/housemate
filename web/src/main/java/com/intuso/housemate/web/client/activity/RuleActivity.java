package com.intuso.housemate.web.client.activity;

import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.place.RulePlace;
import com.intuso.housemate.web.client.ui.view.RuleView;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 24/11/12
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
 */
public class RuleActivity extends HousemateActivity<RulePlace, RuleView> {

    protected RuleActivity(RulePlace place) {
        super(place);
    }

    @Override
    public RuleView getView() {
        return Housemate.FACTORY.getRuleView();
    }
}
