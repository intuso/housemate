package com.intuso.housemate.web.client.bootstrap.widget.device;

import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.list.MainList;
import com.intuso.housemate.web.client.object.GWTProxyDevice;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/09/13
 * Time: 00:39
 * To change this template use File | Settings | File Templates.
 */
public class DeviceList extends MainList<DeviceData, GWTProxyDevice> {

    public DeviceList(String title, List<String> filteredIds, boolean includeFiltered) {
        super(Housemate.INJECTOR.getProxyRoot().getDevices(), title, filteredIds, includeFiltered);
    }

    @Override
    protected Widget getWidget(ChildOverview childOverview, GWTProxyDevice device) {
        if(device != null)
            return new Device(device);
        else
            return new Device(childOverview);
    }
}
