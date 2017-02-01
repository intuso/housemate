package com.intuso.housemate.extension.plugin.homeeasyuk.feature;

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.annotation.Property;
import com.intuso.housemate.client.v1_0.api.driver.FeatureDriver;
import com.intuso.housemate.client.v1_0.api.feature.PowerControl;
import com.intuso.housemate.client.v1_0.api.type.ObjectReference;
import com.intuso.housemate.client.v1_0.proxy.api.annotation.ProxyWrapper;
import com.intuso.housemate.client.v1_0.proxy.simple.SimpleProxyHardware;
import com.intuso.housemate.extension.homeeasyuk.api.HomeEasyUKAPI;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 12/12/16.
 */
@Id(value = "homeeasyuk-appliance", name = "HomeEasy UK Appliance", description = "Power something on and off as a HomeEasy UK appliance")
public class HomeEasyUKFeature implements FeatureDriver, PowerControl, HomeEasyUKAPI.Appliance.Listener, ObjectReference.Listener<SimpleProxyHardware> {

    private final ManagedCollection<Listener> listeners;
    private final ProxyWrapper proxyWrapper;

    private Logger logger;
    private FeatureDriver.Callback callback;

    private ObjectReference<SimpleProxyHardware> hardware;
    private ManagedCollection.Registration hardwareListenerRegistration;
    private int houseId;
    private byte unitCode;

    private HomeEasyUKAPI hardwareProxy;
    private HomeEasyUKAPI.Appliance applianceProxy;
    private ManagedCollection.Registration applianceListener;

    @Inject
    public HomeEasyUKFeature(ManagedCollectionFactory managedCollectionFactory,
                             ProxyWrapper proxyWrapper) {
        this.listeners = managedCollectionFactory.create();
        this.proxyWrapper = proxyWrapper;
    }

    @Property
    @Id(value = "hardware", name = "Hardware", description = "Hardware providing communication to the HomeEasy UK appliance")
    public void setHardware(ObjectReference<SimpleProxyHardware> hardware) {
        uninit();
        if(hardwareListenerRegistration != null) {
            hardwareListenerRegistration.remove();
            hardwareListenerRegistration = null;
        }
        this.hardware = hardware;
        if(this.hardware != null)
            hardwareListenerRegistration = this.hardware.addListener(this);
        init(false);
    }

    @Property
    @Id(value = "house-id", name = "House ID", description = "The house ID of the HomeEasy UK appliance")
    public void setHouseId(int houseId) {
        uninit();
        this.houseId = houseId;
        init(false);
    }

    @Property
    @Id(value = "unit-id", name = "Unit ID", description = "The unit ID of the HomeEasy UK appliance")
    public void setUnitCode(byte unitCode) {
        uninit();
        this.unitCode = unitCode;
        init(false);
    }

    @Override
    public ManagedCollection.Registration addListener(Listener listener) {
        return listeners.add(listener);
    }

    @Override
    public void init(Logger logger, FeatureDriver.Callback callback) {
        this.logger = logger;
        this.callback = callback;
        init(true);
    }

    private void init(boolean fail) {
        // check whether it has been initialised or not
        if(callback == null)
            return;

        if(hardware == null) {
            callback.setError("Hardware property is not set");
            if (fail)
                throw new FeatureException("Hardware property is not set");
        } else if(hardware.getObject() == null) {
            callback.setError("Could not find hardware object at " + Joiner.on("/").join(hardware.getPath()));
            if (fail)
                throw new FeatureException("Could not find hardware object at " + Joiner.on("/").join(hardware.getPath()));
        } else if(houseId < 0 || houseId > 0x03FFFFFF) {
            // check the port value is a positive number
            callback.setError("House ID must be between 0 and " + 0x03FFFFFF);
            if (fail)
                throw new FeatureException("House ID must be between 0 and " + 0x03FFFFFF);
        } else if(unitCode < 1 || unitCode > 16) {
            // check the unit ID is a number between 1 and 16
            callback.setError("Unitcode must be between 1 and 16 (inclusive)");
            if (fail)
                throw new FeatureException("Unit ID must be between 1 and 16 (inclusive)");
        } else {
            callback.setError(null);
            hardwareProxy = proxyWrapper.build(logger, hardware.getObject(), HomeEasyUKAPI.class, "", 3000L);
            hardwareProxy.initAppliance(houseId, unitCode);
            applianceProxy = proxyWrapper.build(logger, hardware.getObject(), HomeEasyUKAPI.Appliance.class, Integer.toString(houseId) + "-" + Byte.toString(unitCode) + "-", 3000L);
            for(Listener listener : listeners)
                listener.on(applianceProxy.isOn());
            applianceListener = applianceProxy.addCallback(this);
        }
    }

    @Override
    public void uninit() {
        if(applianceListener != null) {
            applianceListener.remove();
            applianceListener = null;
        }
        if(applianceProxy != null) {
            hardwareProxy.uninitAppliance(houseId, unitCode);
            applianceProxy = null;
        }
        if(hardwareProxy != null)
            hardwareProxy = null;
    }

    @Override
    public void turnOn() {
        if(applianceProxy != null)
            applianceProxy.turnOn();
        else
            throw new FeatureException("Feature is not correctly configured");
    }

    @Override
    public void turnOff() {
        if(applianceProxy != null)
            applianceProxy.turnOff();
        else
            throw new FeatureException("Feature is not correctly configured");
    }

    @Override
    public void on(boolean on) {
        for(Listener listener : listeners)
            listener.on(applianceProxy.isOn());
    }

    @Override
    public void available(SimpleProxyHardware object) {
        init(false);
    }

    @Override
    public void unavailable() {
        uninit();
    }
}
