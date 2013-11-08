package com.intuso.housemate.web.client.bootstrap.widget.device;

import com.github.gwtbootstrap.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.list.ObjectList;
import com.intuso.housemate.web.client.object.GWTProxyDevice;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/09/13
 * Time: 00:39
 * To change this template use File | Settings | File Templates.
 */
public class DeviceList extends ObjectList<DeviceData, GWTProxyDevice> {

    public DeviceList(String title, List<String> filteredIds, boolean includeFiltered) {
        super(Housemate.ENVIRONMENT.getResources().getRoot().getDevices(), title, filteredIds, includeFiltered);
    }

    @Override
    protected Widget getWidget(final String id, GWTProxyDevice object) {
        if(object != null)
            return new Device(object);
        else {
            final SimplePanel panel = new SimplePanel();
            Housemate.ENVIRONMENT.getResources().getRoot().getDevices().load(new LoadManager("load-device",
                    new HousemateObject.TreeLoadInfo(id, new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE))) {
                @Override
                protected void failed(HousemateObject.TreeLoadInfo path) {
                    panel.clear();
                    panel.add(new Label("Failed to load device"));
                }

                @Override
                protected void allLoaded() {
                    panel.clear();
                    panel.add(new Device(Housemate.ENVIRONMENT.getResources().getRoot().getDevices().get(id)));
                }
            });
            return panel;
        }
    }
}
