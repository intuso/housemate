package com.intuso.housemate.web.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.intuso.housemate.web.client.place.HousematePlace;
import com.intuso.housemate.web.client.ui.view.HousemateView;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 24/11/12
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
 */
public abstract class HousemateActivity<P extends HousematePlace, V extends HousemateView<P>> extends AbstractActivity {

    private P place;

    protected HousemateActivity(P place) {
        this.place = place;
    }

    @Override
    public void start(AcceptsOneWidget acceptsOneWidget, EventBus eventBus) {
        V view = getView();
        view.newPlace(place);
        acceptsOneWidget.setWidget(view);
    }

    protected abstract V getView();
}
