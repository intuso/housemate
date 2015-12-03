package com.intuso.housemate.plugin.rfxcom.lighting1;

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
import com.intuso.utilities.log.Log;
import com.rfxcom.rfxtrx.util.lighting1.Lighting1;
import com.rfxcom.rfxtrx.util.lighting1.Lighting1House;

import java.util.UUID;

/**
 * Created by tomc on 02/12/15.
 */
public abstract class Lighting1Handler implements Handler {

    static Lighting1Handler INSTANCE;

    private final Log log;
    private final RealDevice.Container deviceContainer;
    private final Lighting1 lighting1;
    private ListenerRegistration messageListenerRegistration;
    private final SetMultimap<Byte, Byte> knownAppliances = HashMultimap.create();
    private final CallbackImpl callbackImpl = new CallbackImpl();

    public Lighting1Handler(Log log, Lighting1 lighting1, RealDevice.Container deviceContainer) {
        INSTANCE = this;
        this.log = log;
        this.deviceContainer = deviceContainer;
        this.lighting1 = lighting1;
    }

    @Override
    public void listen(boolean listen) {
        if(messageListenerRegistration != null) {
            messageListenerRegistration.removeListener();
            messageListenerRegistration = null;
        }
        if(listen)
            messageListenerRegistration = lighting1.addCallback(callbackImpl);
    }

    public com.rfxcom.rfxtrx.util.lighting1.Lighting1Appliance makeAppliance(byte houseId, byte unitCode) {
        knownAppliances.put(houseId, unitCode);
        return new com.rfxcom.rfxtrx.util.lighting1.Lighting1Appliance(new Lighting1House(lighting1, houseId), unitCode);
    }

    public void propertiesChanged(byte oldHouseId, byte oldUnitCode, byte newHouseId, byte newUnitCode) {
        knownAppliances.remove(oldHouseId, oldUnitCode);
        knownAppliances.put(newHouseId, newUnitCode);
    }

    public void messageReceived(final byte houseId, final byte unitCode, final boolean on) {
        if(!knownAppliances.containsEntry(houseId, unitCode)) {
            try {
                String name = getDeviceName(houseId, unitCode);
                final RealDevice<Lighting1Appliance> device = deviceContainer.createAndAddDevice(new DeviceData(UUID.randomUUID().toString(), name, name));
                device.getDriverProperty().set(new TypeInstances(new TypeInstance(getDriverId())), new Command.PerformListener<RealCommand>() {
                    @Override
                    public void commandStarted(RealCommand command) {

                    }

                    @Override
                    public void commandFinished(RealCommand command) {
                        if(device.isDriverLoaded()) {
                            Lighting1Appliance appliance = device.getDriver();
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
                log.e("Failed to auto-create Lighting1 device " + houseId + "/" + (int) unitCode, t);
            }
        }
    }

    public abstract String getDeviceName(byte houseId, byte unitCode);
    public abstract String getDriverId();

    private class CallbackImpl implements Lighting1.Callback {

        @Override
        public void turnedOn(byte houseId, byte unitCode) {
            messageReceived(houseId, unitCode, true);
        }

        @Override
        public void turnedOnAll(byte houseId) {
            // no unit to create device for
        }

        @Override
        public void turnedOff(byte houseId, byte unitCode) {
            messageReceived(houseId, unitCode, false);
        }

        @Override
        public void turnedOffAll(byte houseId) {
            // no unit to create device for
        }

        @Override
        public void dim(byte houseId, byte unitCode) {
            // dimmable devices not supported yet
        }

        @Override
        public void bright(byte houseId, byte level) {
            // dimmable devices not supported yet
        }

        @Override
        public void chime(byte houseId) {
            // chime devices not supported yet
        }
    }
}
