package com.intuso.housemate.web.client.bootstrap.widget.hardware;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.proxy.api.LoadManager;
import com.intuso.housemate.client.v1_0.data.api.ChildOverview;
import com.intuso.housemate.client.v1_0.data.api.RemoteObject;
import com.intuso.housemate.client.v1_0.data.api.TreeLoadInfo;
import com.intuso.housemate.web.client.bootstrap.widget.object.ObjectWidget;
import com.intuso.housemate.web.client.object.GWTProxyHardware;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;
import org.gwtbootstrap3.client.ui.constants.AlertType;

import java.util.List;

/**
 * Created by tomc on 05/03/15.
 */
public class Hardware extends ObjectWidget<GWTProxyHardware> {

    private final GWTProxyList<Type.Data<?>, GWTProxyType> types;

    public Hardware(GWTProxyList<Type.Data<?>, GWTProxyType> types, final GWTProxyList<com.intuso.housemate.client.v1_0.api.object.Hardware.Data, GWTProxyHardware> hardwares, final ChildOverview childOverview) {
        this.types = types;
        final GWTProxyHardware hardware = hardwares.get(childOverview.getId());
        if(hardware != null)
            setObject(hardware);
        else {
            setName(childOverview.getName());
            loading(true);
            hardwares.load(new LoadManager(new LoadManager.Callback() {
                @Override
                public void failed(List<String> errors) {
                    loading(false);
                    setMessage(AlertType.WARNING, "Failed to load hardware");
                }

                @Override
                public void succeeded() {
                    loading(false);
                    setObject(hardwares.get(childOverview.getId()));
                }
            }, new TreeLoadInfo(childOverview.getId(), new TreeLoadInfo(RemoteObject.EVERYTHING_RECURSIVE))));
        }
    }
    
    @Override
    protected IsWidget getBodyWidget(GWTProxyHardware object) {
        return new HardwareBody(object);
    }

    @Override
    protected IsWidget getSettingsWidget(GWTProxyHardware object) {
        return new HardwareSettings(types, object);
    }
}
