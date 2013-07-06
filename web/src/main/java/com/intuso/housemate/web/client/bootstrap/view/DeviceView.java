package com.intuso.housemate.web.client.bootstrap.view;

import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.web.client.GWTResources;
import com.intuso.housemate.web.client.bootstrap.widget.device.Device;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyDevice;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.place.DevicePlace;

/**
 */
public class DeviceView extends ObjectListView<GWTProxyDevice, DevicePlace> implements com.intuso.housemate.web.client.ui.view.DeviceView {

    public DeviceView(GWTResources<?> resources) {
        super(resources);
    }

    @Override
    protected GWTProxyList<DeviceData, GWTProxyDevice> getList(DevicePlace place) {
        return resources.getRoot().getDevices();
    }

    @Override
    protected GWTProxyCommand getAddCommand(DevicePlace place) {
        return resources.getRoot().getAddDeviceCommand();
    }

    @Override
    protected String getSelectedObjectName(DevicePlace place) {
        return place.getDeviceName();
    }

    @Override
    protected Widget getObjectWidget(DevicePlace place, GWTProxyDevice device) {
        return new Device(device);
    }

    @Override
    protected DevicePlace getPlace(DevicePlace place, GWTProxyDevice device) {
        if(device == null)
            return new DevicePlace();
        else
            return new DevicePlace(device.getId());
    }
}