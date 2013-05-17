package com.intuso.housemate.web.client.activity;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.intuso.housemate.web.client.place.AccountPlace;
import com.intuso.housemate.web.client.place.ConditionPlace;
import com.intuso.housemate.web.client.place.DevicePlace;
import com.intuso.housemate.web.client.place.HomePlace;
import com.intuso.housemate.web.client.place.RulePlace;
import com.intuso.housemate.web.client.place.SatisfiedConsequencePlace;
import com.intuso.housemate.web.client.place.UnsatisfiedConsequencePlace;
import com.intuso.housemate.web.client.place.UserPlace;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 24/11/12
 * Time: 19:23
 * To change this template use File | Settings | File Templates.
 */
public class HousemateActivityMapper implements ActivityMapper {
    @Override
    public Activity getActivity(Place place) {
        if(place instanceof DevicePlace)
            return new DeviceActivity((DevicePlace)place);
        else if(place instanceof ConditionPlace)
            return new ConditionActivity((ConditionPlace)place);
        else if(place instanceof SatisfiedConsequencePlace)
            return new SatisfiedConsequenceActivity((SatisfiedConsequencePlace)place);
        else if(place instanceof UnsatisfiedConsequencePlace)
            return new UnsatisfiedConsequenceActivity((UnsatisfiedConsequencePlace)place);
        else if(place instanceof RulePlace)
            return new RuleActivity((RulePlace)place);
        else if(place instanceof AccountPlace)
            return new AccountActivity((AccountPlace)place);
        else if(place instanceof UserPlace)
            return new UserActivity((UserPlace)place);
        else if(place instanceof HomePlace)
            return new HomeActivity((HomePlace)place);
        return null;
    }
}
