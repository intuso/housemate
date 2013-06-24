package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.parameter.ParameterWrappable;
import com.intuso.housemate.object.proxy.ProxyCommand;
import com.intuso.housemate.web.client.GWTResources;

/**
 */
public class GWTProxyCommand extends ProxyCommand<
            GWTResources<GWTProxyFactory.List<ParameterWrappable, GWTProxyParameter>>,
            GWTResources<? extends HousemateObjectFactory<GWTResources<?>, ParameterWrappable, GWTProxyParameter>>,
        GWTProxyParameter, GWTProxyList<ParameterWrappable, GWTProxyParameter>, GWTProxyCommand> {
    public GWTProxyCommand(GWTResources<GWTProxyFactory.List<ParameterWrappable, GWTProxyParameter>> resources,
                           GWTResources<GWTProxyFactory.Parameter> subResources,
                           CommandWrappable wrappable) {
        super(resources, subResources, wrappable);
    }
}
