package com.intuso.housemate.web.client.bootstrap.widget.hardware;

import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.LazyLoadedWidgetCallback;
import com.intuso.housemate.web.client.bootstrap.widget.list.MainList;
import com.intuso.housemate.web.client.object.GWTProxyHardware;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/09/13
 * Time: 00:39
 * To change this template use File | Settings | File Templates.
 */
public class HardwareList extends MainList<HardwareData, GWTProxyHardware> {

    public HardwareList(String title, List<String> filteredIds, boolean includeFiltered) {
        super(Housemate.INJECTOR.getProxyRoot().getHardwares(), title, filteredIds, includeFiltered);
    }

    @Override
    protected void getWidget(ChildOverview childOverview, LazyLoadedWidgetCallback callback) {
        callback.widgetReady(new Hardware(childOverview));
    }
}
