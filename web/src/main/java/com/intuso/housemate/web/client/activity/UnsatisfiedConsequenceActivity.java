package com.intuso.housemate.web.client.activity;

import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.place.UnsatisfiedConsequencePlace;
import com.intuso.housemate.web.client.ui.view.UnsatisfiedConsequenceView;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 24/11/12
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
 */
public class UnsatisfiedConsequenceActivity extends HousemateActivity<UnsatisfiedConsequencePlace, UnsatisfiedConsequenceView> {

    protected UnsatisfiedConsequenceActivity(UnsatisfiedConsequencePlace place) {
        super(place);
    }

    @Override
    public UnsatisfiedConsequenceView getView() {
        return Housemate.FACTORY.getUnsatisfiedConsequenceView();
    }
}
