package com.intuso.housemate.web.client.bootstrap.widget.application.instance;

import com.intuso.housemate.web.client.bootstrap.extensions.CommandToggleSwitch;
import com.intuso.housemate.web.client.object.GWTProxyApplicationInstance;
import com.intuso.housemate.web.client.object.GWTProxyCommand;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 10/02/14
 * Time: 18:49
 * To change this template use File | Settings | File Templates.
 */
public class AccessWidget extends CommandToggleSwitch {

    private GWTProxyApplicationInstance applicationInstance;

    public AccessWidget() {
        setOnText("Allowed");
        setOffText("Rejected");
    }

    public void setApplicationInstance(GWTProxyApplicationInstance applicationInstance) {
        this.applicationInstance = applicationInstance;
        setValue(applicationInstance.getStatusValue());
    }

    @Override
    protected boolean isTrue() {
        return applicationInstance.getStatus() == com.intuso.housemate.object.v1_0.api.ApplicationInstance.Status.Allowed;
    }

    @Override
    protected GWTProxyCommand getTrueCommand() {
        return applicationInstance.getAllowCommand();
    }

    @Override
    protected GWTProxyCommand getFalseCommand() {
        return applicationInstance.getRejectCommand();
    }
}
