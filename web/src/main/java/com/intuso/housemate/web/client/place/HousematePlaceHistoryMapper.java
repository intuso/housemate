package com.intuso.housemate.web.client.place;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

/**
 */
@WithTokenizers({
        DevicesPlace.Tokeniser.class,
        AutomationsPlace.Tokeniser.class,
        UsersPlace.Tokeniser.class,
        ApplicationsPlace.Tokeniser.class})
public interface HousematePlaceHistoryMapper extends PlaceHistoryMapper {}
