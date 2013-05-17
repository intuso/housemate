package com.intuso.housemate.broker.object.general;

import com.intuso.housemate.broker.comms.ServerComms;
import com.intuso.housemate.core.resources.Resources;
import com.intuso.utils.log.Log;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 16/04/13
 * Time: 07:41
 * To change this template use File | Settings | File Templates.
 */
public class BrokerResources<R> implements Resources {

    private final BrokerGeneralResources generalResources;
    private R root;

    public BrokerResources(BrokerGeneralResources generalResources) {
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

    public ServerComms getComms() {
        return generalResources.getComms();
    }

    public R getRoot() {
        return root;
    }

    public void setRoot(R root) {
        this.root = root;
    }
}
