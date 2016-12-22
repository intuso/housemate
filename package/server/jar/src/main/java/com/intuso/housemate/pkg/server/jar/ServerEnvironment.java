package com.intuso.housemate.pkg.server.jar;

import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.intuso.housemate.pkg.server.jar.ioc.JarServerModule;
import com.intuso.housemate.platform.pc.Properties;
import com.intuso.housemate.server.object.real.FactoryPluginListener;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import jssc.SerialPortList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Platform implementation for a server. Works the same was as the PC implementation in terms of getting properties
 * and overriding based on command line arguments, Main difference is that the Comms implementation is different
 * and some methods are unsupported as they should not be used by the server.
 *
 */
public class ServerEnvironment {

    private final static Logger logger = LoggerFactory.getLogger(ServerEnvironment.class);

    /**
	 * Create a environment instance
	 * @param args the command line args that the server was run with
	 */
	public ServerEnvironment(String args[]) {

        ListenersFactory listenersFactory = new ListenersFactory() {
            @Override
            public <LISTENER> Listeners<LISTENER> create() {
                return new Listeners<>(new CopyOnWriteArrayList<LISTENER>());
            }
        };

        WriteableMapPropertyRepository defaultProperties = WriteableMapPropertyRepository.newEmptyRepository(listenersFactory);
        PropertyRepository properties = Properties.create(listenersFactory, defaultProperties, args);

        Injector injector = Guice.createInjector(new JarServerModule(defaultProperties, properties));

        // force factory listener to be created and register itself for new plugins
        injector.getInstance(FactoryPluginListener.class);

        logger.debug("Starting server");
        ServiceManager serviceManager = injector.getInstance(ServiceManager.class);
        serviceManager.startAsync();
        serviceManager.awaitHealthy();
        logger.debug("Started server");
    }

    private void loadSharedJNILibs() {
        SerialPortList.getPortNames();
        //CommPortIdentifier.getPortIdentifiers();
    }
}
