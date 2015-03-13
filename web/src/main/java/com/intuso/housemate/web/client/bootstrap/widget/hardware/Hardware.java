package com.intuso.housemate.web.client.bootstrap.widget.hardware;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.object.ObjectWidget;
import com.intuso.housemate.web.client.object.GWTProxyHardware;
import org.gwtbootstrap3.client.ui.constants.AlertType;

/**
 * Created by tomc on 05/03/15.
 */
public class Hardware extends ObjectWidget<GWTProxyHardware> {

    public Hardware(final ChildOverview childOverview) {
        GWTProxyHardware hardware = Housemate.INJECTOR.getProxyRoot().getHardwares().get(childOverview.getId());
        if(hardware != null)
            setObject(hardware);
        else {
            setName(childOverview.getName());
            loading(true);
            Housemate.INJECTOR.getProxyRoot().getHardwares().load(new LoadManager(new LoadManager.Callback() {
                @Override
                public void failed(HousemateObject.TreeLoadInfo path) {
                    loading(false);
                    setMessage(AlertType.WARNING, "Failed to load hardware");
                }

                @Override
                public void allLoaded() {
                    loading(false);
                    setObject(Housemate.INJECTOR.getProxyRoot().getHardwares().get(childOverview.getId()));
                }
            }, "loadHardware-" + childOverview.getId(),
                    new HousemateObject.TreeLoadInfo(childOverview.getId(), new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE))));
        }
    }
    
    @Override
    protected IsWidget getBodyWidget(GWTProxyHardware object) {
        return new HardwareBody(object);
    }

    @Override
    protected IsWidget getSettingsWidget(GWTProxyHardware object) {
        return new HardwareSettings(object);
    }
}
