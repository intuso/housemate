package com.intuso.housemate.web.client.place;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 17/01/14
 * Time: 07:46
 * To change this template use File | Settings | File Templates.
 */
public class HousematePlaceController extends PlaceController {

    @Inject
    public HousematePlaceController(EventBus eventBus) {
        super(eventBus);
    }
}
