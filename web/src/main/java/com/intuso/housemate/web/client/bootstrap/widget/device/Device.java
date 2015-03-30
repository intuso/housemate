package com.intuso.housemate.web.client.bootstrap.widget.device;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.web.client.bootstrap.widget.object.ObjectWidget;
import com.intuso.housemate.web.client.object.GWTProxyDevice;
import com.intuso.housemate.web.client.object.GWTProxyList;
import org.gwtbootstrap3.client.ui.constants.AlertType;

/**
 * Created by tomc on 05/03/15.
 */
public class Device extends ObjectWidget<GWTProxyDevice> {

    public Device(final GWTProxyList<DeviceData, GWTProxyDevice> devices, final ChildOverview childOverview) {
        GWTProxyDevice user = devices.get(childOverview.getId());
        if(user != null)
            setObject(user);
        else {
            setName(childOverview.getName());
            loading(true);
            devices.load(new LoadManager(new LoadManager.Callback() {
                @Override
                public void failed(HousemateObject.TreeLoadInfo path) {
                    loading(false);
                    setMessage(AlertType.WARNING, "Failed to load device");
                }

                @Override
                public void allLoaded() {
                    loading(false);
                    setObject(devices.get(childOverview.getId()));
                }
            }, "loadDevice-" + childOverview.getId(),
                    new HousemateObject.TreeLoadInfo(childOverview.getId(), new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE))));
        }
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
