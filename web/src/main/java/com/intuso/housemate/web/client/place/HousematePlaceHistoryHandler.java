package com.intuso.housemate.web.client.place;

import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 17/01/14
 * Time: 07:45
 * To change this template use File | Settings | File Templates.
 */
public class HousematePlaceHistoryHandler extends PlaceHistoryHandler {

    @Inject
    public HousematePlaceHistoryHandler(PlaceHistoryMapper mapper) {
        super(mapper);
    }
}
