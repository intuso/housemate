package com.intuso.housemate.web.client.bootstrap.widget.hardware;

import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.data.api.ChildOverview;
import com.intuso.housemate.web.client.bootstrap.widget.LazyLoadedWidgetCallback;
import com.intuso.housemate.web.client.bootstrap.widget.list.MainList;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyHardware;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/09/13
 * Time: 00:39
 * To change this template use File | Settings | File Templates.
 */
public class HardwareList extends MainList<com.intuso.housemate.client.v1_0.api.object.Hardware.Data, GWTProxyHardware> {

    private final GWTProxyList<Type.Data<?>, GWTProxyType> types;
    private final GWTProxyList<com.intuso.housemate.client.v1_0.api.object.Hardware.Data, GWTProxyHardware> hardwares;

    public HardwareList(String title, GWTProxyList<Type.Data<?>, GWTProxyType> types, GWTProxyList<com.intuso.housemate.client.v1_0.api.object.Hardware.Data, GWTProxyHardware> hardwares, GWTProxyCommand addCommand) {
        super(title, types, addCommand);
        this.types = types;
        this.hardwares = hardwares;
        setList(hardwares);
    }

    @Override
    protected void getWidget(ChildOverview childOverview, LazyLoadedWidgetCallback callback) {
        callback.widgetReady(new Hardware(types, hardwares, childOverview));
    }
}
