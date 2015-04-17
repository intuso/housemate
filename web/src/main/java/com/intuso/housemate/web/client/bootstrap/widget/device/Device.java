package com.intuso.housemate.web.client.bootstrap.widget.device;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.web.client.bootstrap.widget.object.ObjectWidget;
import com.intuso.housemate.web.client.object.GWTProxyDevice;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;
import org.gwtbootstrap3.client.ui.constants.AlertType;

import java.util.List;

/**
 * Created by tomc on 05/03/15.
 */
public class Device extends ObjectWidget<GWTProxyDevice> {

    private final GWTProxyList<TypeData<?>, GWTProxyType> types;

    public Device(GWTProxyList<TypeData<?>, GWTProxyType> types, final GWTProxyList<DeviceData, GWTProxyDevice> devices, final ChildOverview childOverview) {
        this.types = types;
        GWTProxyDevice user = devices.get(childOverview.getId());
        if(user != null)
            setObject(user);
        else {
            setName(childOverview.getName());
            loading(true);
            devices.load(new LoadManager(new LoadManager.Callback() {
                @Override
                public void failed(List<String> errors) {
                    loading(false);
                    setMessage(AlertType.WARNING, "Failed to load device");
                }

                @Override
                public void succeeded() {
                    loading(false);
                    setObject(devices.get(childOverview.getId()));
                }
            }, new HousemateObject.TreeLoadInfo(childOverview.getId(), new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE))));
        }
    }

    @Override
    protected IsWidget getBodyWidget(GWTProxyDevice object) {
        return new DeviceBody(types, object);
    }

    @Override
    protected IsWidget getSettingsWidget(GWTProxyDevice object) {
        return new DeviceSettings(types, object);
    }
}
