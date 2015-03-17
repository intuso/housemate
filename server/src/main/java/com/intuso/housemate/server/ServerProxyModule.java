package com.intuso.housemate.server;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.server.proxy.ServerProxyList;
import com.intuso.housemate.object.server.proxy.ServerProxyType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class ServerProxyModule extends AbstractModule {

    @Override
    protected void configure() {
        // install all required modules
        install(new com.intuso.housemate.object.server.proxy.ServerProxyModule());
    }

    @Provides
    @Singleton
    public ServerProxyList<TypeData<?>, ServerProxyType> getProxyTypes(Log log, ListenersFactory listenersFactory, Injector injector) {
        return new ServerProxyList<TypeData<?>, ServerProxyType>(log, listenersFactory, injector, new ListData<TypeData<?>>(Root.TYPES_ID, Root.TYPES_ID, "Proxied types"));
    }
}
