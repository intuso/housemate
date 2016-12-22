package com.intuso.housemate.extension.plugin.homeeasyuk.feature;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.annotation.Property;
import com.intuso.housemate.client.v1_0.api.driver.FeatureDriver;
import com.intuso.housemate.client.v1_0.api.feature.PowerControl;
import com.intuso.housemate.client.v1_0.proxy.api.annotation.ProxyWrapper;
import com.intuso.housemate.client.v1_0.proxy.simple.SimpleProxyHardware;
import com.intuso.housemate.extension.homeeasyuk.api.HomeEasyUKAPI;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 12/12/16.
 */
@Id(value = "homeeasyuk-appliance", name = "HomeEasy UK Appliance", description = "Power something on and off as a HomeEasy UK appliance")
public class HomeEasyUKFeature implements FeatureDriver, PowerControl.Stateful, HomeEasyUKAPI.Appliance.Listener {

    private final Logger logger;
    private final Listeners<Listener> listeners;
    private final FeatureDriver.Callback callback;
    private final ProxyWrapper proxyWrapper;

    private SimpleProxyHardware hardware;
    private Integer houseId;
    private Byte unitCode;

    private HomeEasyUKAPI hardwareProxy;
    private HomeEasyUKAPI.Appliance applianceProxy;
    private ListenerRegistration applianceListener;

    @Inject
    public HomeEasyUKFeature(@Assisted Logger logger,
                             @Assisted FeatureDriver.Callback callback,
                             ListenersFactory listenersFactory,
                             ProxyWrapper proxyWrapper) {
        this.logger = logger;
        this.listeners = listenersFactory.create();
        this.callback = callback;
        this.proxyWrapper = proxyWrapper;
    }

    @Property
    @Id(value = "hardware", name = "Hardware", description = "Hardware providing communication to the HomeEasy UK appliance")
    public void setHardware(SimpleProxyHardware hardware) {
        this.hardware = hardware;
        reconfigure(false);
    }

    @Property
    @Id(value = "houseId", name = "House ID", description = "The house ID of the HomeEasy UK appliance")
    public void setHouseId(Integer houseId) {
        this.houseId = houseId;
        reconfigure(false);
    }

    @Property
    @Id(value = "untiId", name = "Unit ID", description = "The unit ID of the HomeEasy UK appliance")
    public void setUnitCode(Byte unitCode) {
        this.unitCode = unitCode;
        reconfigure(false);
    }

    @Override
    public boolean isOn() {
        return applianceProxy.isOn();
    }

    @Override
    public ListenerRegistration addListener(Listener listener) {
        return listeners.addListener(listener);
    }

    @Override
    public void startFeature() {
        reconfigure(true);
    }

    private void reconfigure(boolean fail) {
        if(hardware == null) {
            callback.setError("Hardware property is not set");
            if(fail)
                throw new FeatureException("Hardware property is not set");
        } else if(houseId == null) {
            callback.setError("House ID proprety is not set");
            if (fail)
                throw new FeatureException("House ID proprety is not set");
        } else if(houseId < 0 || houseId > 0x03FFFFFF) {
            // check the port value is a positive number
            callback.setError("House ID must be between 0 and " + 0x03FFFFFF);
            if (fail)
                throw new FeatureException("House ID must be between 0 and " + 0x03FFFFFF);
        } else if(unitCode == null) {
            callback.setError("Unit ID proprety is not set");
            if (fail)
                throw new FeatureException("Unit ID proprety is not set");
        } else if(unitCode < 1 || unitCode > 16) {
            // check the unit ID is a number between 1 and 16
            callback.setError("Unitcode must be between 1 and 16 (inclusive)");
            if (fail)
                throw new FeatureException("Unit ID must be between 1 and 16 (inclusive)");
        } else {
            callback.setError(null);
            hardwareProxy = proxyWrapper.build(logger, hardware, HomeEasyUKAPI.class, "");
            hardwareProxy.initAppliance(houseId, unitCode);
            applianceProxy = proxyWrapper.build(logger, hardware, HomeEasyUKAPI.Appliance.class, houseId.toString() + "-" + unitCode.toString());
            for(Listener listener : listeners)
                listener.on(applianceProxy.isOn());
            applianceListener = applianceProxy.addCallback(this);
        }
    }

    @Override
    public void stopFeature() {
        if(applianceListener != null) {
            applianceListener.removeListener();
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
}
