package com.intuso.housemate.plugin.rfxcom.lighting2;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.intuso.housemate.client.v1_0.real.api.RealCommand;
import com.intuso.housemate.client.v1_0.real.api.RealDevice;
import com.intuso.housemate.comms.v1_0.api.payload.DeviceData;
import com.intuso.housemate.object.v1_0.api.Command;
import com.intuso.housemate.object.v1_0.api.TypeInstance;
import com.intuso.housemate.object.v1_0.api.TypeInstances;
import com.intuso.housemate.plugin.rfxcom.Handler;
import com.intuso.utilities.listener.ListenerRegistration;
import com.rfxcom.rfxtrx.util.lighting2.Lighting2;
import com.rfxcom.rfxtrx.util.lighting2.Lighting2House;
import org.slf4j.Logger;

import java.util.UUID;

/**
 * Created by tomc on 02/12/15.
 */
public abstract class Lighting2Handler implements Handler {

    static Lighting2Handler INSTANCE;

    private final Logger logger;
    private final RealDevice.Container deviceContainer;
    private final Lighting2 lighting2;
    private ListenerRegistration messageListenerRegistration;
    private final SetMultimap<Integer, Byte> knownAppliances = HashMultimap.create();
    private final CallbackImpl callbackImpl = new CallbackImpl();

    public Lighting2Handler(Logger logger, Lighting2 lighting2, RealDevice.Container deviceContainer) {
        INSTANCE = this;
        this.logger = logger;
        this.deviceContainer = deviceContainer;
        this.lighting2 = lighting2;
    }

    @Override
    public void listen(boolean listen) {
        if(messageListenerRegistration != null) {
            messageListenerRegistration.removeListener();
            messageListenerRegistration = null;
        }
        if(listen)
            messageListenerRegistration = lighting2.addCallback(callbackImpl);
    }

    public com.rfxcom.rfxtrx.util.lighting2.Lighting2Appliance makeAppliance(int houseId, byte unitCode) {
        knownAppliances.put(houseId, unitCode);
        return new com.rfxcom.rfxtrx.util.lighting2.Lighting2Appliance(new Lighting2House(lighting2, houseId), unitCode);
    }

    public void propertiesChanged(int oldHouseId, byte oldUnitCode, int newHouseId, byte newUnitCode) {
        knownAppliances.remove(oldHouseId, oldUnitCode);
        knownAppliances.put(newHouseId, newUnitCode);
    }

    public void messageReceived(final int houseId, final byte unitCode, final boolean on) {
        if(!knownAppliances.containsEntry(houseId, unitCode)) {
            try {
                String name = getDeviceName(houseId, unitCode);
                final RealDevice<Lighting2Appliance> device = deviceContainer.createAndAddDevice(new DeviceData(UUID.randomUUID().toString(), name, name));
                device.getDriverProperty().set(new TypeInstances(new TypeInstance(getDriverId())), new Command.PerformListener<RealCommand>() {
                    @Override
                    public void commandStarted(RealCommand command) {

                    }

                    @Override
                    public void commandFinished(RealCommand command) {
                        if (device.isDriverLoaded()) {
                            Lighting2Appliance appliance = device.getDriver();
                            appliance.setHouseId(houseId);
                            appliance.setUnitCode(unitCode);
                            appliance.getPowerValues().isOn(on);
                        }
                    }

                    @Override
                    public void commandFailed(RealCommand command, String error) {

                    }
                });
            } catch (Throwable t) {
                logger.error("Failed to auto-create Lighting2 device " + houseId + "/" + (int) unitCode, t);
            }
        }
    }

    public abstract String getDeviceName(int houseId, byte unitCode);
    public abstract String getDriverId();

    private class CallbackImpl implements Lighting2.Callback {

        @Override
        public void turnedOn(int houseId, byte unitCode) {
            messageReceived(houseId, unitCode, true);
        }

        @Override
        public void turnedOnAll(int houseId) {

        }

        @Override
        public void turnedOff(int houseId, byte unitCode) {
            messageReceived(houseId, unitCode, false);
        }

        @Override
        public void turnedOffAll(int houseId) {

        }

        @Override
        public void setLevel(int houseId, byte unitCode, byte level) {
            messageReceived(houseId, unitCode, level != 0 && level != 0x0F);
        }

        @Override
        public void setLevelAll(int houseId, byte level) {

        }
    }
}
