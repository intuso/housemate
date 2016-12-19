package com.intuso.housemate.extension.plugin.homeeasyuk.feature;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.annotation.Property;
import com.intuso.housemate.client.v1_0.api.driver.FeatureDriver;
import com.intuso.housemate.client.v1_0.api.feature.StatefulPowerControl;
import com.intuso.housemate.client.v1_0.proxy.simple.SimpleProxyHardware;
import com.intuso.housemate.extension.homeeasyuk.api.HomeEasyUKHardwareAPI;
import com.intuso.utilities.listener.ListenerRegistration;
import org.slf4j.Logger;

/**
 * Created by tomc on 12/12/16.
 */
@Id(value = "homeeasyuk-appliance", name = "HomeEasy UK Appliance", description = "Power something on and off as a HomeEasy UK appliance")
public class HomeEasyUKFeature implements FeatureDriver, StatefulPowerControl, HomeEasyUKHardwareAPI.Appliance.Listener {

    private final Logger logger;
    private final FeatureDriver.Callback callback;
    private final HomeEasyUKHardwareProxy.Factory homeEasyUKHardwareProxyFactory;

    private SimpleProxyHardware hardware;
    private Integer houseId;
    private Integer unitId;

    private StatefulPowerControl.PowerValues values;

    private HomeEasyUKHardwareAPI.Appliance applianceProxy;
    private ListenerRegistration applianceListener;

    @Inject
    public HomeEasyUKFeature(@Assisted Logger logger,
                             @Assisted Callback callback,
                             HomeEasyUKHardwareProxy.Factory homeEasyUKHardwareProxyFactory) {
        this.logger = logger;
        this.callback = callback;
        this.homeEasyUKHardwareProxyFactory = homeEasyUKHardwareProxyFactory;
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
    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
        reconfigure(false);
    }

    @Override
    public void start() {
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
        } else if(unitId == null) {
            callback.setError("Unit ID proprety is not set");
            if (fail)
                throw new FeatureException("Unit ID proprety is not set");
        } else if(unitId < 1 || unitId > 16) {
            // check the unit ID is a number between 1 and 16
            callback.setError("Unitcode must be between 1 and 16 (inclusive)");
            if (fail)
                throw new FeatureException("Unit ID must be between 1 and 16 (inclusive)");
        } else {
            callback.setError(null);
            applianceProxy = homeEasyUKHardwareProxyFactory.create(logger, hardware).appliance(houseId, unitId.byteValue());
            values.isOn(applianceProxy.isOn());
            applianceListener = applianceProxy.addListener(this);
        }
    }

    @Override
    public void stop() {
        applianceProxy = null;
        if(applianceListener != null) {
            applianceListener.removeListener();
            applianceListener = null;
        }
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
    public void on(boolean isOn) {
        values.isOn(isOn);
    }
}
