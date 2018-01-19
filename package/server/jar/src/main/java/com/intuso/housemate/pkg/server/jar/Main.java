package com.intuso.housemate.pkg.server.jar;

import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.intuso.housemate.client.real.impl.internal.HardwareDetectorInternalPluginListener;
import com.intuso.housemate.client.real.impl.internal.HardwareDetectorV1_0PluginListener;
import com.intuso.housemate.pkg.server.jar.ioc.ServerPackageJarModule;
import com.intuso.housemate.platform.pc.Properties;
import com.intuso.housemate.plugin.host.internal.PluginHost;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import jssc.SerialPortList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Main class for the server
 *
 */
public class Main {

	private final static Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * Create a environment instance
     * @param args the command line args that the server was run with
     */
    public Main(String args[]) {

        ManagedCollectionFactory managedCollectionFactory = new ManagedCollectionFactory() {

            @Override
            public <LISTENER> ManagedCollection<LISTENER> createSet() {
                return new ManagedCollection<>(Collections.synchronizedSet(new HashSet<>()));
            }

            @Override
            public <LISTENER> ManagedCollection<LISTENER> createList() {
                return new ManagedCollection<>(Collections.synchronizedList(new LinkedList<>()));
            }
        };

        WriteableMapPropertyRepository defaultProperties = WriteableMapPropertyRepository.newEmptyRepository(managedCollectionFactory);
        PropertyRepository properties = Properties.create(managedCollectionFactory, defaultProperties, args);

        ServerPackageJarModule.configureDefaults(defaultProperties);

        Injector injector = Guice.createInjector(new ServerPackageJarModule(properties));

        logger.debug("Starting server");
        final ServiceManager serviceManager = injector.getInstance(ServiceManager.class);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.debug("Stopping server");
                serviceManager.stopAsync();
                serviceManager.awaitStopped();
                logger.debug("Stopped server");
            }
        });
        serviceManager.startAsync();
        serviceManager.awaitHealthy();

        // detect any hardware now that everything is started
        injector.getInstance(PluginHost.class).addInternalListener(injector.getInstance(HardwareDetectorInternalPluginListener.class), true);
        injector.getInstance(PluginHost.class).addV1_0Listener(injector.getInstance(HardwareDetectorV1_0PluginListener.class), true);

        logger.debug("Started server");
    }

    private void loadSharedJNILibs() {
        SerialPortList.getPortNames();
        //CommPortIdentifier.getPortIdentifiers();
    }

    public static void main(String args[]) {
        new Main(args);
    }
}
