package com.intuso.housemate.web.client.place;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 24/11/12
 * Time: 19:22
 * To change this template use File | Settings | File Templates.
 */
@WithTokenizers({
        HomePlace.Tokeniser.class,
        DevicePlace.Tokeniser.class,
        RulePlace.Tokeniser.class,
        ConditionPlace.Tokeniser.class,
        SatisfiedConsequencePlace.Tokeniser.class,
        UnsatisfiedConsequencePlace.Tokeniser.class,
        AccountPlace.Tokeniser.class
})
public interface HousematePlaceHistoryMapper extends PlaceHistoryMapper {
}
