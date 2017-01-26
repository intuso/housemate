package com.intuso.housemate.plugin.rfxcom;

import com.intuso.housemate.client.v1_0.api.driver.FeatureDriver;
import com.intuso.housemate.extension.homeeasyuk.api.HomeEasyUKAPI;
import com.intuso.utilities.listener.ManagedCollection;
import com.intuso.utilities.listener.ManagedCollectionFactory;
import com.rfxcom.rfxtrx.RFXtrx;
import com.rfxcom.rfxtrx.util.lighting2.Lighting2;
import com.rfxcom.rfxtrx.util.lighting2.Lighting2Appliance;
import com.rfxcom.rfxtrx.util.lighting2.Lighting2House;

import java.io.IOException;

/**
 * Housemate device that controls a HomeEasy Appliance
 *
 */
public class HomeEasyUKApplianceImpl implements HomeEasyUKAPI.Appliance {

    private final ManagedCollection<Listener> callbacks;

    private Lighting2Appliance lighting2Appliance;
    private ManagedCollection.Registration listenerRegistration;
    private boolean isOn = false;

    public HomeEasyUKApplianceImpl(ManagedCollectionFactory managedCollectionFactory, RFXtrx rfXtrx, int houseId, byte unitCode) {
        this.callbacks = managedCollectionFactory.create();
        if(listenerRegistration != null) {
            listenerRegistration.remove();
            listenerRegistration = null;
        }
        lighting2Appliance = new Lighting2Appliance(new Lighting2House(new Lighting2(rfXtrx, com.rfxcom.rfxtrx.message.Lighting2.SubType.AC), houseId), unitCode);
        listenerRegistration = lighting2Appliance.addCallback(new com.rfxcom.rfxtrx.util.lighting2.Lighting2Appliance.Callback() {

            @Override
            public void turnedOn(com.rfxcom.rfxtrx.util.lighting2.Lighting2Appliance a) {
                setIsOn(true);
            }

            @Override
            public void turnedOff(com.rfxcom.rfxtrx.util.lighting2.Lighting2Appliance a) {
                setIsOn(false);
            }
        });
	}

    @Override
    public boolean isOn() {
        return isOn;
    }

    @Override
    public void turnOn() {
        if(lighting2Appliance == null)
            throw new FeatureDriver.FeatureException("Not connected to RFXCom device. Ensure properties are set correctly");
		try {
			lighting2Appliance.turnOn();
            setIsOn(true);
		} catch (IOException e) {
			throw new FeatureDriver.FeatureException("Could not turn appliance on", e);
		}
	}
	
	@Override
    public void turnOff() {
        if(lighting2Appliance == null)
            throw new FeatureDriver.FeatureException("Not connected to RFXCom device. Ensure properties are set correctly");
		try {
			lighting2Appliance.turnOff();
            setIsOn(false);
		} catch (IOException e) {
			throw new FeatureDriver.FeatureException("Could not turn appliance off", e);
		}
	}

    @Override
    public ManagedCollection.Registration addCallback(Listener listener) {
        return callbacks.add(listener);
    }

	private void setIsOn(boolean isOn) {
        this.isOn = isOn;
        for(Listener listener : callbacks)
            listener.on(isOn);
    }
}
