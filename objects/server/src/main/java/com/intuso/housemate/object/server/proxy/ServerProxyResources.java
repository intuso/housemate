package com.intuso.housemate.object.server.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.object.server.ServerResources;
import com.intuso.housemate.object.server.real.ServerRealResources;
import com.intuso.housemate.object.real.RealResources;

/**
 * @param <FACTORY> the type of the factory
 */
public interface ServerProxyResources<
        FACTORY extends HousemateObjectFactory<?, ?, ?>> extends ServerResources<ServerProxyRootObject> {
    public FACTORY getFactory();
    <NF extends HousemateObjectFactory<? extends ServerProxyResources<?>, ?, ? extends ServerProxyObject<?, ?, ?, ?, ?>>>
    ServerProxyResources<NF> cloneForNewFactory(NF newFactory);

    ServerRealResources getServerRealResources();

    RealResources getRealResources();
}
