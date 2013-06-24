package com.intuso.housemate.web.client.ui.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.web.client.place.HousematePlace;

/**
 */
public interface HousemateView<P extends HousematePlace> extends IsWidget {
    void newPlace(P place);
}
