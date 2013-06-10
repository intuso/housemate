package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.argument.ArgumentWrappable;
import com.intuso.housemate.object.proxy.ProxyCommand;
import com.intuso.housemate.web.client.GWTResources;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 27/03/12
 * Time: 23:39
 * To change this template use File | Settings | File Templates.
 */
public class GWTProxyCommand extends ProxyCommand<
            GWTResources<GWTProxyFactory.List<ArgumentWrappable, GWTProxyArgument>>,
            GWTResources<? extends HousemateObjectFactory<GWTResources<?>, ArgumentWrappable, GWTProxyArgument>>,
            GWTProxyArgument, GWTProxyList<ArgumentWrappable, GWTProxyArgument>, GWTProxyCommand> {
    public GWTProxyCommand(GWTResources<GWTProxyFactory.List<ArgumentWrappable, GWTProxyArgument>> resources,
                           GWTResources<GWTProxyFactory.Argument> subResources,
                           CommandWrappable wrappable) {
        super(resources, subResources, wrappable);
    }
}
