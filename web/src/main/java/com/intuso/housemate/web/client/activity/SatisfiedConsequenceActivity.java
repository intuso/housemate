package com.intuso.housemate.web.client.activity;

import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.place.SatisfiedConsequencePlace;
import com.intuso.housemate.web.client.ui.view.SatisfiedConsequenceView;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 24/11/12
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
 */
public class SatisfiedConsequenceActivity extends HousemateActivity<SatisfiedConsequencePlace, SatisfiedConsequenceView> {

    protected SatisfiedConsequenceActivity(SatisfiedConsequencePlace place) {
        super(place);
    }

    @Override
    public SatisfiedConsequenceView getView() {
        return Housemate.FACTORY.getSatisfiedConsequenceView();
    }
}
