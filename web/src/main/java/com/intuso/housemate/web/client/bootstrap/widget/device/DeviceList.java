package com.intuso.housemate.web.client.bootstrap.widget.device;

import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.data.api.ChildOverview;
import com.intuso.housemate.web.client.bootstrap.widget.LazyLoadedWidgetCallback;
import com.intuso.housemate.web.client.bootstrap.widget.list.MainList;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyDevice;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/09/13
 * Time: 00:39
 * To change this template use File | Settings | File Templates.
 */
public class DeviceList extends MainList<com.intuso.housemate.client.v1_0.api.object.Device.Data, GWTProxyDevice> {

    private final GWTProxyList<Type.Data<?>, GWTProxyType> types;
    private final GWTProxyList<com.intuso.housemate.client.v1_0.api.object.Device.Data, GWTProxyDevice> devices;

    public DeviceList(String title, GWTProxyList<Type.Data<?>, GWTProxyType> types, GWTProxyList<com.intuso.housemate.client.v1_0.api.object.Device.Data, GWTProxyDevice> devices, GWTProxyCommand addCommand) {
        super(title, types, addCommand);
        this.types = types;
        this.devices = devices;
        setList(devices);
    }

    @Override
    protected void getWidget(ChildOverview childOverview, LazyLoadedWidgetCallback callback) {
        callback.widgetReady(new Device(types, devices, childOverview));
    }
}
