package com.intuso.housemate.plugin.rfxcom;

import com.intuso.housemate.client.v1_0.real.api.driver.FeatureDriver;
import com.intuso.housemate.extension.homeeasyuk.api.HomeEasyUKHardwareAPI;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import com.rfxcom.rfxtrx.RFXtrx;
import com.rfxcom.rfxtrx.util.lighting2.Lighting2;
import com.rfxcom.rfxtrx.util.lighting2.Lighting2Appliance;
import com.rfxcom.rfxtrx.util.lighting2.Lighting2House;

import java.io.IOException;

/**
 * Housemate device that controls a HomeEasy Appliance
 *
 */
public class HomeEasyUKAppliance implements HomeEasyUKHardwareAPI.Appliance {

    private final Listeners<Listener> listeners;

    private Lighting2Appliance lighting2Appliance;
    private ListenerRegistration listenerRegistration;
    private boolean isOn = false;

    public HomeEasyUKAppliance(ListenersFactory listenersFactory, RFXtrx rfXtrx, int houseId, byte unitId) {
        this.listeners = listenersFactory.create();
        if(listenerRegistration != null) {
            listenerRegistration.removeListener();
            listenerRegistration = null;
        }
        lighting2Appliance = new Lighting2Appliance(new Lighting2House(new Lighting2(rfXtrx, com.rfxcom.rfxtrx.message.Lighting2.SubType.AC), houseId), unitId);
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
    public ListenerRegistration listen(Listener listener) {
        return listeners.addListener(listener);
    }

	private void setIsOn(boolean isOn) {
        this.isOn = isOn;
        for(Listener listener : listeners)
            listener.on(isOn);
    }
}
