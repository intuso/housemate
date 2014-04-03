package com.intuso.housemate.object.proxy.simple;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.intuso.housemate.api.comms.*;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.object.proxy.ProxyRoot;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 30/01/14
 * Time: 19:08
 * To change this template use File | Settings | File Templates.
 */
public class ProxyClientHelper<ROOT extends ProxyRoot<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> {

    private final Log log;
    private final ROOT proxyRoot;
    private final Router router;

    private ApplicationDetails applicationDetails;
    private List<HousemateObject.TreeLoadInfo> toLoad = Lists.newArrayList();
    private LoadManager.Callback callback;

    private boolean rootCleared = false;
    private ListenerRegistration proxyListenerRegistration;
    private ListenerRegistration routerListenerRegistration;

    private ProxyClientHelper(Log log, ROOT proxyRoot, Router router) {
        this.log = log;
        this.proxyRoot = proxyRoot;
        this.router = router;
    }

    public static <ROOT extends ProxyRoot<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> ProxyClientHelper<ROOT>
                newClientHelper(Log log, ROOT proxyRoot, Router router) {
        return new ProxyClientHelper<ROOT>(log, proxyRoot, router);
    }

    public static ProxyClientHelper newClientHelper(Injector injector) {
        return new ProxyClientHelper(
                injector.getInstance(Log.class),
                injector.getInstance(SimpleProxyRoot.class),
                injector.getInstance(Router.class));
    }

    public static ProxyClientHelper newClientHelper(Module... modules) {
        return ProxyClientHelper.newClientHelper(Guice.createInjector(modules));
    }

    public ROOT getRoot() {
        return proxyRoot;
    }

    public ProxyClientHelper applicationDetails(ApplicationDetails applicationDetails) {
        this.applicationDetails = applicationDetails;
        return this;
    }

    public ProxyClientHelper load(HousemateObject.TreeLoadInfo treeLoadInfo) {
        toLoad.add(treeLoadInfo);
        return this;
    }

    private ProxyClientHelper load(String[] path, String ending) {
        return load(HousemateObject.TreeLoadInfo.create(path, ending));
    }

    public ProxyClientHelper load(String ... path) {
        return load(path, null);
    }

    public ProxyClientHelper loadAllChildren(String ... path) {
        return load(path, HousemateObject.EVERYTHING);
    }

    public ProxyClientHelper loadAllDescendants(String... path) {
        return load(path, HousemateObject.EVERYTHING_RECURSIVE);
    }

    public ProxyClientHelper callback(LoadManager.Callback callback) {
        this.callback = callback;
        return this;
    }

    public void start() {
        proxyListenerRegistration = proxyRoot.addObjectListener(new ProxyRootListener());
        RouterListener routerListener = new RouterListener();
        routerListenerRegistration = router.addObjectListener(routerListener);
        routerListener.statusChanged(null, ServerConnectionStatus.Disconnected, ApplicationStatus.Unregistered, ApplicationInstanceStatus.Unregistered);
    }

    public void unregister() {
        if(proxyRoot.getApplicationInstanceStatus() != ApplicationInstanceStatus.Unregistered)
            proxyRoot.unregister();
        if(router.getApplicationInstanceStatus() != ApplicationInstanceStatus.Unregistered)
            router.unregister();
    }

    public void stop() {
        router.disconnect();
        if(proxyListenerRegistration != null)
            proxyListenerRegistration.removeListener();
        if(routerListenerRegistration != null)
            routerListenerRegistration.removeListener();
    }

    private class RouterListener implements RootListener<RouterRoot> {

        @Override
        public void statusChanged(RouterRoot root, ServerConnectionStatus serverConnectionStatus, ApplicationStatus applicationStatus, ApplicationInstanceStatus applicationInstanceStatus) {
            log.d("Router connection status: serverConnectionStatus=" + serverConnectionStatus
                    + ", applicationStatus=" + applicationStatus
                    + ", applicationInstanceStatus=" + applicationInstanceStatus);
            if(serverConnectionStatus == ServerConnectionStatus.Disconnected)
                router.connect();
            else if(serverConnectionStatus == ServerConnectionStatus.ConnectedToServer
                    && applicationInstanceStatus == ApplicationInstanceStatus.Unregistered)
                router.register(applicationDetails);
        }

        @Override
        public void newApplicationInstance(RouterRoot root, String instanceId) {
            // connection manager saves this in the properties for us
        }

        @Override
        public void newServerInstance(RouterRoot root, String serverId) {
            // connection manager will re-register us
        }
    }

    private class ProxyRootListener implements RootListener<ProxyRoot<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> {

        @Override
        public void statusChanged(ProxyRoot<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> root,
                                  ServerConnectionStatus serverConnectionStatus, ApplicationStatus applicationStatus,
                                  ApplicationInstanceStatus applicationInstanceStatus) {
            if(serverConnectionStatus == ServerConnectionStatus.ConnectedToServer
                    && applicationInstanceStatus == ApplicationInstanceStatus.Unregistered)
                proxyRoot.register(applicationDetails);
            else if(applicationInstanceStatus == ApplicationInstanceStatus.Allowed) {
                if(!rootCleared)
                    proxyRoot.clearLoadedObjects();
                if(toLoad.size() > 0)
                    proxyRoot.load(new LoadManager(callback, "clientHelper", toLoad));
                else if(callback != null)
                    callback.allLoaded();
            }
        }

        @Override
        public void newApplicationInstance(ProxyRoot<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> root, String instanceId) {
            // do nothing, saved in router listener
        }

        @Override
        public void newServerInstance(ProxyRoot<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> root, String serverId) {
            // nothing to do here
        }
    }
}
