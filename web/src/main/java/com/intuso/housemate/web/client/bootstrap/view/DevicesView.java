package com.intuso.housemate.web.client.bootstrap.view;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.inject.Inject;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.web.client.bootstrap.widget.device.DeviceList;
import com.intuso.housemate.web.client.handler.SelectedIdsChangedHandler;
import com.intuso.housemate.web.client.object.GWTProxyRealClient;
import com.intuso.housemate.web.client.object.GWTProxyRoot;
import com.intuso.housemate.web.client.place.DevicesPlace;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/09/13
 * Time: 00:15
 * To change this template use File | Settings | File Templates.
 */
public class DevicesView extends FlowPanel
        implements com.intuso.housemate.web.client.ui.view.DevicesView,
            ListListener<GWTProxyRealClient>,
            SelectedIdsChangedHandler {

    private final PlaceHistoryMapper placeHistoryMapper;

    @Inject
    public DevicesView(PlaceHistoryMapper placeHistoryMapper, GWTProxyRoot root) {
        this.placeHistoryMapper = placeHistoryMapper;
        root.getRealClients().addObjectListener(this, true);
    }

    @Override
    public void selectedIdsChanged(Set<String> ids) {
        History.newItem(placeHistoryMapper.getToken(new DevicesPlace(ids)), false);
    }

    @Override
    public void newPlace(DevicesPlace place) {

    }

    @Override
    public void elementAdded(GWTProxyRealClient client) {
        add(new DeviceList(client.getDevices(), client.getName()));
    }

    @Override
    public void elementRemoved(GWTProxyRealClient client) {
        // do nothing atm
    }
}
