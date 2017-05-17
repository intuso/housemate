package com.intuso.housemate.pkg.node.v1_0.jar;

import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.intuso.housemate.client.v1_0.real.impl.HardwareDetectorPluginListener;
import com.intuso.housemate.pkg.node.v1_0.jar.ioc.NodePackageJarModule;
import com.intuso.housemate.platform.pc.Properties;
import com.intuso.housemate.plugin.host.internal.PluginHost;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import jssc.SerialPortList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by tomc on 30/12/16.
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
            public <LISTENER> ManagedCollection<LISTENER> create() {
                return new ManagedCollection<>(new CopyOnWriteArrayList<LISTENER>());
            }
        };

        WriteableMapPropertyRepository defaultProperties = WriteableMapPropertyRepository.newEmptyRepository(managedCollectionFactory);
        PropertyRepository properties = Properties.create(managedCollectionFactory, defaultProperties, args);

        NodePackageJarModule.configureDefaults(defaultProperties);

        Injector injector = Guice.createInjector(new NodePackageJarModule(properties));

        logger.info("Starting node");
        final ServiceManager serviceManager = injector.getInstance(ServiceManager.class);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.info("Stopping node");
                serviceManager.stopAsync();
                serviceManager.awaitStopped();
                logger.info("Stopped node");
            }
        });
        serviceManager.startAsync();
        serviceManager.awaitHealthy();

        // detect any hardware now that everything is started
        injector.getInstance(PluginHost.class).addV1_0Listener(injector.getInstance(HardwareDetectorPluginListener.class), true);

        logger.info("Started node");
    }

    private void loadSharedJNILibs() {
        SerialPortList.getPortNames();
        //CommPortIdentifier.getPortIdentifiers();
    }

    public static void main(String args[]) {
        new Main(args);
    }
}
