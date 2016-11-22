package com.intuso.housemate.server;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.intuso.housemate.plugin.host.internal.PluginHost;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 03/01/14
 * Time: 10:22
 * To change this template use File | Settings | File Templates.
 */
public class ServerService extends AbstractIdleService {

    public final static String SERVER_NAME = "server.name";

    private final PluginHost pluginHost;

    @Inject
    public ServerService(PluginHost pluginHost) {
        this.pluginHost = pluginHost;
    }

    @Override
    protected void startUp() throws Exception {
        pluginHost.start();
    }

    @Override
    protected void shutDown() throws Exception {
        pluginHost.stop();
    }
}
