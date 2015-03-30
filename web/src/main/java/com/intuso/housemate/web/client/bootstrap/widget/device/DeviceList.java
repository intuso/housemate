package com.intuso.housemate.web.client.bootstrap.widget.device;

import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.web.client.bootstrap.widget.LazyLoadedWidgetCallback;
import com.intuso.housemate.web.client.bootstrap.widget.list.MainList;
import com.intuso.housemate.web.client.object.GWTProxyDevice;
import com.intuso.housemate.web.client.object.GWTProxyList;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/09/13
 * Time: 00:39
 * To change this template use File | Settings | File Templates.
 */
public class DeviceList extends MainList<DeviceData, GWTProxyDevice> {

    private final GWTProxyList<DeviceData, GWTProxyDevice> devices;

    public DeviceList(GWTProxyList<DeviceData, GWTProxyDevice> devices, String name) {
        super(devices, name, null, true);
        this.devices = devices;
    }

    @Override
    protected void getWidget(ChildOverview childOverview, LazyLoadedWidgetCallback callback) {
        callback.widgetReady(new Device(devices, childOverview));
    }
}
