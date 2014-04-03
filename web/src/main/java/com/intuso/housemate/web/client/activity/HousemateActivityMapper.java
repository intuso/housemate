package com.intuso.housemate.web.client.activity;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.intuso.housemate.web.client.place.ApplicationsPlace;
import com.intuso.housemate.web.client.place.AutomationsPlace;
import com.intuso.housemate.web.client.place.DevicesPlace;
import com.intuso.housemate.web.client.place.UsersPlace;

/**
 */
public class HousemateActivityMapper implements ActivityMapper {
    @Override
    public Activity getActivity(Place place) {
        if(place instanceof DevicesPlace)
            return new DevicesActivity((DevicesPlace)place);
        else if(place instanceof AutomationsPlace)
            return new AutomationsActivity((AutomationsPlace)place);
        else if(place instanceof UsersPlace)
            return new UsersActivity((UsersPlace)place);
        else if(place instanceof ApplicationsPlace)
            return new ApplicationsActivity((ApplicationsPlace)place);
        return null;
    }
}
