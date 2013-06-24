package com.intuso.housemate.web.client.place;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

/**
 */
@WithTokenizers({
        HomePlace.Tokeniser.class,
        DevicePlace.Tokeniser.class,
        AutomationPlace.Tokeniser.class,
        ConditionPlace.Tokeniser.class,
        SatisfiedTaskPlace.Tokeniser.class,
        UnsatisfiedTaskPlace.Tokeniser.class,
        AccountPlace.Tokeniser.class
})
public interface HousematePlaceHistoryMapper extends PlaceHistoryMapper {
}
