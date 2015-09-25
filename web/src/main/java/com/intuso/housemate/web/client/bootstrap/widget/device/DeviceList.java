package com.intuso.housemate.web.client.bootstrap.widget.device;

import com.intuso.housemate.comms.v1_0.api.ChildOverview;
import com.intuso.housemate.comms.v1_0.api.payload.DeviceData;
import com.intuso.housemate.comms.v1_0.api.payload.TypeData;
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
public class DeviceList extends MainList<DeviceData, GWTProxyDevice> {

    private final GWTProxyList<TypeData<?>, GWTProxyType> types;
    private final GWTProxyList<DeviceData, GWTProxyDevice> devices;

    public DeviceList(String title, GWTProxyList<TypeData<?>, GWTProxyType> types, GWTProxyList<DeviceData, GWTProxyDevice> devices, GWTProxyCommand addCommand) {
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
