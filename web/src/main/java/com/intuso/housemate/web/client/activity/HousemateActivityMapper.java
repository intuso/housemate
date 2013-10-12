package com.intuso.housemate.web.client.activity;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.intuso.housemate.web.client.place.AutomationsPlace;
import com.intuso.housemate.web.client.place.DevicesPlace;
import com.intuso.housemate.web.client.place.UsersPlace;

/**
 */
public class HousemateActivityMapper implements ActivityMapper {
    @Override
    public Activity getActivity(Place place) {
        if(place instanceof DevicesPlace)
            return new DeviceActivity((DevicesPlace)place);
        else if(place instanceof AutomationsPlace)
            return new AutomationActivity((AutomationsPlace)place);
        else if(place instanceof UsersPlace)
            return new UserActivity((UsersPlace)place);
        return null;
    }
}
