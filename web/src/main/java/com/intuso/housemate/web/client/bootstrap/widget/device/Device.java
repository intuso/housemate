package com.intuso.housemate.web.client.bootstrap.widget.device;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.proxy.api.LoadManager;
import com.intuso.housemate.client.v1_0.data.api.ChildOverview;
import com.intuso.housemate.client.v1_0.data.api.RemoteObject;
import com.intuso.housemate.client.v1_0.data.api.TreeLoadInfo;
import com.intuso.housemate.web.client.bootstrap.widget.object.ObjectWidget;
import com.intuso.housemate.web.client.object.GWTProxyDevice;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;
import org.gwtbootstrap3.client.ui.constants.AlertType;

import java.util.List;

/**
 * Created by tomc on 05/03/15.
 */
public class Device extends ObjectWidget<GWTProxyDevice> implements com.intuso.housemate.client.v1_0.api.object.Device.Listener<GWTProxyDevice> {

    private final GWTProxyList<Type.Data<?>, GWTProxyType> types;

    public Device(GWTProxyList<Type.Data<?>, GWTProxyType> types, final GWTProxyList<com.intuso.housemate.client.v1_0.api.object.Device.Data, GWTProxyDevice> devices, final ChildOverview childOverview) {
        this.types = types;
        GWTProxyDevice device = devices.get(childOverview.getId());
        if(device != null)
            setObject(device);
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
            }, new TreeLoadInfo(childOverview.getId(), new TreeLoadInfo(RemoteObject.EVERYTHING_RECURSIVE))));
        }
    }

    @Override
    protected void setObject(GWTProxyDevice device) {
        super.setObject(device);
        device.addObjectListener(this);
    }

    @Override
    protected IsWidget getBodyWidget(GWTProxyDevice object) {
        return new DeviceBody(types, object);
    }

    @Override
    protected IsWidget getSettingsWidget(GWTProxyDevice object) {
        return new DeviceSettings(types, object);
    }

    @Override
    public void renamed(GWTProxyDevice device, String oldName, String newName) {
        updateName(newName);
    }

    @Override
    public void error(GWTProxyDevice device, String error) {

    }

    @Override
    public void driverLoaded(GWTProxyDevice usesDriver, boolean loaded) {

    }

    @Override
    public void running(GWTProxyDevice runnable, boolean running) {

    }
}
