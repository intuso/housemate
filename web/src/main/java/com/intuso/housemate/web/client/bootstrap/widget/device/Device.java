package com.intuso.housemate.web.client.bootstrap.widget.device;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.object.ObjectWidget;
import com.intuso.housemate.web.client.object.GWTProxyDevice;
import org.gwtbootstrap3.client.ui.constants.AlertType;

/**
 * Created by tomc on 05/03/15.
 */
public class Device extends ObjectWidget<GWTProxyDevice> {

    public Device(final ChildOverview childOverview) {
        setName(childOverview.getName());
        Housemate.INJECTOR.getProxyRoot().getDevices().load(new LoadManager(new LoadManager.Callback() {
            @Override
            public void failed(HousemateObject.TreeLoadInfo path) {
                setMessage(AlertType.WARNING, "Failed to load device");
            }

            @Override
            public void allLoaded() {
                setObject(Housemate.INJECTOR.getProxyRoot().getDevices().get(childOverview.getId()));
            }
        }, "loadDevice-" + childOverview.getId(),
                new HousemateObject.TreeLoadInfo(childOverview.getId(), new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE))));
    }

    public Device(GWTProxyDevice device) {
        setObject(device);
    }

    @Override
    protected IsWidget getBodyWidget(GWTProxyDevice object) {
        return new DeviceBody(object);
    }

    @Override
    protected IsWidget getSettingsWidget(GWTProxyDevice object) {
        return new DeviceSettings(object);
    }
}
