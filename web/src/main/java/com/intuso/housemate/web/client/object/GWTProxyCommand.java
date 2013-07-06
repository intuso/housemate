package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.object.proxy.ProxyCommand;
import com.intuso.housemate.web.client.GWTResources;

/**
 */
public class GWTProxyCommand extends ProxyCommand<
            GWTResources<GWTProxyFactory.List<ParameterData, GWTProxyParameter>>,
            GWTResources<? extends HousemateObjectFactory<GWTResources<?>, ParameterData, GWTProxyParameter>>,
        GWTProxyParameter, GWTProxyList<ParameterData, GWTProxyParameter>, GWTProxyCommand> {
    public GWTProxyCommand(GWTResources<GWTProxyFactory.List<ParameterData, GWTProxyParameter>> resources,
                           GWTResources<GWTProxyFactory.Parameter> childResources,
                           CommandData data) {
        super(resources, childResources, data);
    }
}
