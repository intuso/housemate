package com.intuso.housemate.broker.client;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.broker.PluginListener;
import com.intuso.housemate.broker.comms.InternalAuthentication;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.object.real.RealCommand;
import com.intuso.housemate.object.real.RealRootObject;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.plugin.api.PluginDescriptor;

/**
 */
public class LocalClient implements PluginListener {

    private final BrokerGeneralResources resources;
    private final LocalClientRoot root;

    public LocalClient(final BrokerGeneralResources resources) throws HousemateException {
        this.resources = resources;
        root = new LocalClientRoot(resources);
        root.login(new InternalAuthentication());
        root.addType(resources.getDeviceFactory().getType());
        root.addType(resources.getConditionFactory().getType());
        root.addType(resources.getTaskFactory().getType());
        resources.addPluginListener(LocalClient.this, true);
    }

    public RealCommand getAddDeviceCommand() {
        return root.getAddDeviceCommand();
    }

    public RealRootObject getRoot() {
        return root;
    }

    @Override
    public void pluginAdded(PluginDescriptor plugin) {
        for(RealType<?, ?, ?> type : plugin.getTypes(resources.getClientResources())) {
            resources.getLog().d("Adding type " + type.getId());
            root.addType(type);
        }
    }

    @Override
    public void pluginRemoved(PluginDescriptor plugin) {
        // todo remove types added by this plugin
    }

}
