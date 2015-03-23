package com.intuso.housemate.server;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.comms.ServerConnectionStatus;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.object.real.RealRoot;
import com.intuso.housemate.object.real.factory.condition.ConditionFactoryType;
import com.intuso.housemate.object.real.factory.device.DeviceFactoryType;
import com.intuso.housemate.object.real.factory.hardware.HardwareFactoryType;
import com.intuso.housemate.object.real.factory.task.TaskFactoryType;
import com.intuso.housemate.plugin.host.PluginManager;
import com.intuso.housemate.server.comms.MainRouter;
import com.intuso.housemate.server.object.bridge.RootBridge;
import com.intuso.housemate.server.plugin.main.ioc.MainPluginModule;

import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 03/01/14
 * Time: 10:22
 * To change this template use File | Settings | File Templates.
 */
public class Server {

    public final static String INSTANCE_ID = UUID.randomUUID().toString();
    public final static ApplicationDetails INTERNAL_APPLICATION = new ApplicationDetails(UUID.randomUUID().toString(), "Server", "Server");

    public final static String SERVER_NAME = "server.name";

    private final Injector injector;

    @Inject
    public Server(Injector injector) {
        this.injector = injector;
    }

    public final void start() {

        // start the main router
        injector.getInstance(MainRouter.class).start();

        // make sure all the framework is created before we add plugins
        injector.getInstance(RootBridge.class);

        // add the default plugin
        injector.getInstance(PluginManager.class).addPlugin(MainPluginModule.class);

        final RealRoot serverRealRoot = injector.getInstance(RealRoot.class);
        serverRealRoot.addObjectListener(new RootListener<RealRoot>() {

            boolean typesAdded = false;

            @Override
            public void serverConnectionStatusChanged(RealRoot root, ServerConnectionStatus serverConnectionStatus) {

            }

            @Override
            public void applicationStatusChanged(RealRoot root, ApplicationStatus applicationStatus) {

            }

            @Override
            public void applicationInstanceStatusChanged(RealRoot root, ApplicationInstanceStatus applicationInstanceStatus) {
                if (!typesAdded && applicationInstanceStatus == ApplicationInstanceStatus.Allowed) {
                    typesAdded = true;
                    root.addType(injector.getInstance(ConditionFactoryType.class));
                    root.addType(injector.getInstance(DeviceFactoryType.class));
                    root.addType(injector.getInstance(HardwareFactoryType.class));
                    root.addType(injector.getInstance(TaskFactoryType.class));
                }
            }

            @Override
            public void newApplicationInstance(RealRoot root, String instanceId) {

            }

            @Override
            public void newServerInstance(RealRoot root, String serverId) {

            }
        });
    }

    public final void acceptClients() {
        injector.getInstance(MainRouter.class).startExternalRouters();
    }

    public final void stop() {
        // stop the main router
        injector.getInstance(MainRouter.class).stop();
    }

    public Injector getInjector() {
        return injector;
    }
}
