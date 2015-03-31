package com.intuso.housemate.web.client.bootstrap.widget.hardware;

import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.web.client.bootstrap.widget.LazyLoadedWidgetCallback;
import com.intuso.housemate.web.client.bootstrap.widget.list.MainList;
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
public class HardwareList extends MainList<HardwareData, GWTProxyHardware> {

    private final GWTProxyList<TypeData<?>, GWTProxyType> types;
    private final GWTProxyList<HardwareData, GWTProxyHardware> hardwares;

    public HardwareList(GWTProxyList<TypeData<?>, GWTProxyType> types, GWTProxyList<HardwareData, GWTProxyHardware> hardwares, String name) {
        super(name, null, true);
        this.types = types;
        this.hardwares = hardwares;
        setList(hardwares);
    }

    @Override
    protected void getWidget(ChildOverview childOverview, LazyLoadedWidgetCallback callback) {
        callback.widgetReady(new Hardware(types, hardwares, childOverview));
    }
}
