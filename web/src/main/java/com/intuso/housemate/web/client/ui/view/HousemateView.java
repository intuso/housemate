package com.intuso.housemate.web.client.ui.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.web.client.place.HousematePlace;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 24/11/12
 * Time: 19:25
 * To change this template use File | Settings | File Templates.
 */
public interface HousemateView<P extends HousematePlace> extends IsWidget {
    void newPlace(P place);
}
