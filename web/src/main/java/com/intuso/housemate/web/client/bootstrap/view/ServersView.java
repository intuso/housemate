package com.intuso.housemate.web.client.bootstrap.view;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.inject.Inject;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.web.client.bootstrap.widget.application.ApplicationList;
import com.intuso.housemate.web.client.bootstrap.widget.automation.AutomationList;
import com.intuso.housemate.web.client.bootstrap.widget.device.DeviceList;
import com.intuso.housemate.web.client.bootstrap.widget.hardware.HardwareList;
import com.intuso.housemate.web.client.bootstrap.widget.user.UserList;
import com.intuso.housemate.web.client.handler.SelectedIdsChangedHandler;
import com.intuso.housemate.web.client.object.GWTProxyRoot;
import com.intuso.housemate.web.client.object.GWTProxyServer;
import com.intuso.housemate.web.client.place.ServersPlace;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/09/13
 * Time: 00:15
 * To change this template use File | Settings | File Templates.
 */
public class ServersView extends FlowPanel
        implements com.intuso.housemate.web.client.ui.view.ServersView,
            ListListener<GWTProxyServer>,
            SelectedIdsChangedHandler {

    private final PlaceHistoryMapper placeHistoryMapper;

    @Inject
    public ServersView(PlaceHistoryMapper placeHistoryMapper, GWTProxyRoot root) {
        this.placeHistoryMapper = placeHistoryMapper;
        root.getServers().addObjectListener(this, true);
    }

    @Override
    public void selectedIdsChanged(Set<String> ids) {
        History.newItem(placeHistoryMapper.getToken(new ServersPlace(ids)), false);
    }

    @Override
    public void newPlace(ServersPlace place) {

    }

    @Override
    public void elementAdded(GWTProxyServer client) {
        add(new ApplicationList(client.getName() + ": Applications", client.getTypes(), client.getApplications(), null));
        add(new AutomationList(client.getName() + ": Automations", client.getTypes(), client.getAutomations(), null));
        add(new DeviceList(client.getName() + ": Devices", client.getTypes(), client.getDevices(), null));
        add(new HardwareList(client.getName() + ": Hardware", client.getTypes(), client.getHardwares(), null));
        add(new UserList(client.getName() + ": Users", client.getTypes(), client.getUsers(), null));
    }

    @Override
    public void elementRemoved(GWTProxyServer client) {
        // do nothing atm
    }
}
