package com.intuso.housemate.web.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.intuso.housemate.web.client.place.HousematePlace;

/**
 */
public abstract class HousemateActivity<P extends HousematePlace> extends AbstractActivity {

    private P place;

    protected HousemateActivity(P place) {
        this.place = place;
    }

    @Override
    public void start(AcceptsOneWidget acceptsOneWidget, EventBus eventBus) {
        place.getView().newPlace(place);
        acceptsOneWidget.setWidget(place.getView());
    }
}
