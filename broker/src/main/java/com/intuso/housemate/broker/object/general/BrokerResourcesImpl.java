package com.intuso.housemate.broker.object.general;

import com.intuso.housemate.api.resources.Resources;
import com.intuso.housemate.object.broker.LifecycleHandler;
import com.intuso.housemate.object.broker.BrokerResources;
import com.intuso.housemate.object.broker.ServerComms;
import com.intuso.utilities.log.Log;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 16/04/13
 * Time: 07:41
 * To change this template use File | Settings | File Templates.
 */
public class BrokerResourcesImpl<R> implements Resources, BrokerResources<R> {

    private final BrokerGeneralResources generalResources;
    private R root;

    public BrokerResourcesImpl(BrokerGeneralResources generalResources) {
        this.generalResources = generalResources;
    }

    public BrokerGeneralResources getGeneralResources() {
        return generalResources;
    }

    @Override
    public Log getLog() {
        return generalResources.getLog();
    }

    @Override
    public Map<String, String> getProperties() {
        return generalResources.getProperties();
    }

    @Override
    public ServerComms getComms() {
        return generalResources.getComms();
    }

    @Override
    public LifecycleHandler getLifecycleHandler() {
        return generalResources.getLifecycleHandler();
    }

    @Override
    public R getRoot() {
        return root;
    }

    public void setRoot(R root) {
        this.root = root;
    }
}
