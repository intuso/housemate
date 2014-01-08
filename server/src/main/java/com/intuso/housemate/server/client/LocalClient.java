package com.intuso.housemate.server.client;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.real.RealCommand;
import com.intuso.housemate.object.real.RealRootObject;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.server.comms.InternalAuthentication;
import com.intuso.housemate.server.factory.ConditionFactory;
import com.intuso.housemate.server.factory.DeviceFactory;
import com.intuso.housemate.server.factory.TaskFactory;
import com.intuso.housemate.server.plugin.PluginListener;
import com.intuso.housemate.server.plugin.PluginManager;
import com.intuso.utilities.log.Log;

import java.util.Set;

/**
 * Util class for running objects local to the server
 */
public class LocalClient implements PluginListener {

    private final Log log;

    private final LocalClientRoot root;

    @Inject
    public LocalClient(final Log log, final LocalClientRoot root,
                       final DeviceFactory deviceFactory, final ConditionFactory conditionFactory,
                       final TaskFactory taskFactory, final PluginManager pluginManager) throws HousemateException {
        this.log = log;
        this.root = root;
        root.login(new InternalAuthentication());
        root.addType(deviceFactory.getType());
        root.addType(conditionFactory.getType());
        root.addType(taskFactory.getType());
        pluginManager.addPluginListener(LocalClient.this, true);
    }

    /**
     * Get the command for adding devices
     * @return the command for adding devices
     */
    public RealCommand getAddDeviceCommand() {
        return root.getAddDeviceCommand();
    }

    /**
     * Get the root object
     * @return the root object
     */
    public RealRootObject getRoot() {
        return root;
    }

    @Override
    public void pluginAdded(Injector pluginInjector) {
        for(RealType<?, ?, ?> type : pluginInjector.getInstance(new Key<Set<RealType<?, ?, ?>>>() {})) {
            log.d("Adding type " + type.getId());
            root.addType(type);
        }
    }

    @Override
    public void pluginRemoved(Injector pluginInjector) {
        // todo remove types added by this plugin
    }

}
