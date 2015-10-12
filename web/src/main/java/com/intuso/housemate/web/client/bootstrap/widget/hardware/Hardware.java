package com.intuso.housemate.web.client.bootstrap.widget.hardware;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.client.v1_0.proxy.api.LoadManager;
import com.intuso.housemate.comms.v1_0.api.ChildOverview;
import com.intuso.housemate.comms.v1_0.api.RemoteObject;
import com.intuso.housemate.comms.v1_0.api.TreeLoadInfo;
import com.intuso.housemate.comms.v1_0.api.payload.HardwareData;
import com.intuso.housemate.comms.v1_0.api.payload.TypeData;
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

    private final GWTProxyList<TypeData<?>, GWTProxyType> types;

    public Hardware(GWTProxyList<TypeData<?>, GWTProxyType> types, final GWTProxyList<HardwareData, GWTProxyHardware> hardwares, final ChildOverview childOverview) {
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
