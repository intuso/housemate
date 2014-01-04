package com.intuso.housemate.server.client;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.real.RealCommand;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealRootObject;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.plugin.api.PluginDescriptor;
import com.intuso.housemate.server.comms.InternalAuthentication;
import com.intuso.housemate.server.factory.ConditionFactory;
import com.intuso.housemate.server.factory.DeviceFactory;
import com.intuso.housemate.server.factory.TaskFactory;
import com.intuso.housemate.server.plugin.PluginListener;
import com.intuso.housemate.server.plugin.PluginManager;
import com.intuso.utilities.log.Log;

/**
 * Util class for running objects local to the server
 */
public class LocalClient implements PluginListener {

    private final Log log;
    private final RealResources realResources;

    private final LocalClientRoot root;

    @Inject
    public LocalClient(final Log log, final RealResources realResources, final LocalClientRoot root,
                       final DeviceFactory deviceFactory, final ConditionFactory conditionFactory,
                       final TaskFactory taskFactory, final PluginManager pluginManager) throws HousemateException {
        this.log = log;
        this.realResources = realResources;
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
    public void pluginAdded(PluginDescriptor plugin) {
        for(RealType<?, ?, ?> type : plugin.getTypes(realResources)) {
            log.d("Adding type " + type.getId());
            root.addType(type);
        }
    }

    @Override
    public void pluginRemoved(PluginDescriptor plugin) {
        // todo remove types added by this plugin
    }

}
