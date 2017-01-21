package com.intuso.housemate.plugin.rfxcom.old.lighting1;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.intuso.housemate.plugin.rfxcom.old.Handler;
import com.intuso.utilities.listener.ListenerRegistration;
import com.rfxcom.rfxtrx.util.lighting1.Lighting1;
import com.rfxcom.rfxtrx.util.lighting1.Lighting1House;
import org.slf4j.Logger;

/**
 * Created by tomc on 02/12/15.
 */
public abstract class Lighting1Handler implements Handler {

    static Lighting1Handler INSTANCE;

    private final Logger logger;
    private final Lighting1 lighting1;
    private ListenerRegistration messageListenerRegistration;
    private final SetMultimap<Byte, Byte> knownAppliances = HashMultimap.create();
    private final CallbackImpl callback = new CallbackImpl();

    public Lighting1Handler(Logger logger, Lighting1 lighting1) {
        INSTANCE = this;
        this.logger = logger;
        this.lighting1 = lighting1;
    }

    @Override
    public void listen(boolean listen) {
        if(!listen && messageListenerRegistration != null) {
            messageListenerRegistration.removeListener();
            messageListenerRegistration = null;
        } else if(listen && messageListenerRegistration == null)
            messageListenerRegistration = lighting1.addCallback(callback);
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
                // todo create and add device
                /*final RealDevice<Lighting1Appliance> device = deviceContainer.createAndAddDevice(new Device.Data(UUID.randomUUID().toString(), name, name));
                device.getDriverProperty().set(new Type.Instances(new Type.Instance(getDriverId())), new Command.PerformListener<RealCommand>() {
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
                });*/
            } catch (Throwable t) {
                logger.error("Failed to auto-create Lighting1 device " + houseId + "/" + (int) unitCode, t);
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
