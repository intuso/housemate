package com.intuso.housemate.web.client.object;

import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.object.proxy.ProxyCommand;
import com.intuso.utilities.log.Log;

/**
 */
public class GWTProxyCommand extends ProxyCommand<
            GWTProxyParameter,
            GWTProxyList<ParameterData, GWTProxyParameter>,
            GWTProxyCommand> {
    public GWTProxyCommand(Log log,
                           Injector injector,
                           @Assisted CommandData data) {
        super(log, injector, data);
    }
}
