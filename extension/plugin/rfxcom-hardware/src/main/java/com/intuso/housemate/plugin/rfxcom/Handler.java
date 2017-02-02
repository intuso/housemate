package com.intuso.housemate.plugin.rfxcom;

import com.intuso.housemate.client.v1_0.api.driver.HardwareDriver;
import com.intuso.utilities.collection.ManagedCollection;

/**
 * Created by tomc on 02/02/17.
 */
public abstract class Handler {

    private final String idPrefix;

    protected HardwareDriver.Callback hardwareCallback;
    protected boolean autoCreate;
    private ManagedCollection.Registration callbackRegsitration = null;

    protected Handler(String idPrefix) {
        this.idPrefix = idPrefix;
    }

    public void init(HardwareDriver.Callback hardwareCallback) {
        this.hardwareCallback = hardwareCallback;
        callbackRegsitration = initListener();
    }

    public void uninit() {
        if(callbackRegsitration != null)
            callbackRegsitration.remove();
        callbackRegsitration = null;
        this.hardwareCallback = null;
    }

    public void autoCreate(boolean autoCreate) {
        this.autoCreate = autoCreate;
    }

    boolean matches(String id) {
        return id.startsWith(idPrefix);
    }

    void parseId(String id) {
        parseIdDetails(id.substring(idPrefix.length()));
    }

    abstract ManagedCollection.Registration initListener();
    abstract void parseIdDetails(String details);
}
