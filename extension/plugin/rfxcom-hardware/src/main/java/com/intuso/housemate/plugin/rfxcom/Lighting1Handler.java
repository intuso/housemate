package com.intuso.housemate.plugin.rfxcom;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.HousemateException;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import com.rfxcom.rfxtrx.RFXtrx;
import com.rfxcom.rfxtrx.util.lighting1.Lighting1;

import java.io.IOException;
import java.util.Map;

/**
 * Created by tomc on 02/02/17.
 */
public class Lighting1Handler extends Handler implements Lighting1.Callback {

    private final ManagedCollectionFactory managedCollectionFactory;
    private final Lighting1 lighting1;

    private final String idFormat, nameFormat, descriptionFormat;

    private final Map<Byte, Map<Byte, PowerControlImpl>> devices = Maps.newHashMap();

    protected Lighting1Handler(ManagedCollectionFactory managedCollectionFactory,
                               RFXtrx rfXtrx,
                               com.rfxcom.rfxtrx.message.Lighting1.SubType subType,
                               String idPrefix,
                               String idFormat,
                               String nameFormat,
                               String descriptionFormat) {
        super(idPrefix + "-");
        this.managedCollectionFactory = managedCollectionFactory;
        this.lighting1 = new Lighting1(rfXtrx, subType);
        this.idFormat = idPrefix + "-" + idFormat;
        this.nameFormat = nameFormat;
        this.descriptionFormat = descriptionFormat;
    }

    @Override
    public ManagedCollection.Registration initListener() {
        return lighting1.addCallback(this);
    }

    @Override
    public void turnedOn(byte houseCode, byte unitCode) {
        PowerControlImpl device = getOrCreate(houseCode, unitCode);
        if(device != null)
            device.setOn(true);
    }

    @Override
    public void turnedOnAll(byte houseCode) {
        if(devices.containsKey(houseCode))
            for(PowerControlImpl device : devices.get(houseCode).values())
                device.setOn(true);
    }

    @Override
    public void turnedOff(byte houseCode, byte unitCode) {
        PowerControlImpl device = getOrCreate(houseCode, unitCode);
        if(device != null)
            device.setOn(false);
    }

    @Override
    public void turnedOffAll(byte houseCode) {
        if(devices.containsKey(houseCode))
            for(PowerControlImpl device : devices.get(houseCode).values())
                device.setOn(false);
    }

    @Override
    public void dim(byte houseCode, byte unitCode) {
        // todo
    }

    @Override
    public void bright(byte houseCode, byte unitCode) {
        // todo
    }

    @Override
    public void chime(byte houseCode) {
        // todo
    }

    @Override
    void parseIdDetails(String details) {
        addDevice(Byte.parseByte(details.split("-")[0]), Byte.parseByte(details.split("-")[1]));
    }

    private PowerControlImpl getOrCreate(byte houseCode, byte unitCode) {
        if(devices.containsKey(houseCode) && devices.get(houseCode).containsKey(unitCode))
            return devices.get(houseCode).get(unitCode);
        else if(autoCreate)
            return addDevice(houseCode, unitCode);
        return null;
    }

    public PowerControlImpl addDevice(byte houseCode, byte unitCode) {
        PowerControlImpl device = new PowerControlImpl(managedCollectionFactory, houseCode, unitCode);
        if(!devices.containsKey(houseCode))
            devices.put(houseCode, Maps.<Byte, PowerControlImpl>newHashMap());
        devices.get(houseCode).put(unitCode, device);
        hardwareCallback.addConnectedDevice(
                idFormat.replaceAll("\\$\\{houseCode\\}", Byte.toString(houseCode)).replaceAll("\\$\\{unitCode\\}", Byte.toString(unitCode)),
                nameFormat.replaceAll("\\$\\{houseCode\\}", Byte.toString(houseCode)).replaceAll("\\$\\{unitCode\\}", Byte.toString(unitCode)),
                descriptionFormat.replaceAll("\\$\\{houseCode\\}", Byte.toString(houseCode)).replaceAll("\\$\\{unitCode\\}", Byte.toString(unitCode)),
                device);
        return device;
    }

    public void removeDevice(byte houseCode, byte unitCode) {
        if(devices.containsKey(houseCode) && devices.get(houseCode).containsKey(unitCode)) {
            PowerControlImpl device = devices.get(houseCode).remove(unitCode);
            if(devices.get(houseCode).size() == 0)
                devices.remove(houseCode);
            hardwareCallback.removeConnectedDevice(device);
        }
    }

    public class PowerControlImpl extends PowerControlBase {

        private final byte houseCode;
        private final byte unitCode;

        public PowerControlImpl(ManagedCollectionFactory managedCollectionFactory, byte houseCode, byte unitCode) {
            super(managedCollectionFactory);
            this.houseCode = houseCode;
            this.unitCode = unitCode;
        }

        @Override
        public void turnOn() {
            try {
                lighting1.turnOn(houseCode, unitCode);
            } catch (IOException e) {
                throw new HousemateException("Failed to turn device on");
            }
        }

        @Override
        public void turnOff() {
            try {
                lighting1.turnOff(houseCode, unitCode);
            } catch (IOException e) {
                throw new HousemateException("Failed to turn device off");
            }
        }
    }

    public static class X10 extends Lighting1Handler {

        @Inject
        protected X10(ManagedCollectionFactory managedCollectionFactory, RFXtrx rfXtrx) {
            super(managedCollectionFactory, rfXtrx, com.rfxcom.rfxtrx.message.Lighting1.SubType.X10,
                    "x10",
                    "${houseCode}-${unitCode}",
                    "X10 Appliance ${unitCode}",
                    "X10 Appliance ${unitCode}, remote id ${houseCode}");
        }
    }

    public static class ARC extends Lighting1Handler {

        @Inject
        protected ARC(ManagedCollectionFactory managedCollectionFactory, RFXtrx rfXtrx) {
            super(managedCollectionFactory, rfXtrx, com.rfxcom.rfxtrx.message.Lighting1.SubType.X10,
                    "arc",
                    "${houseCode}-${unitCode}",
                    "ARC Appliance ${unitCode}",
                    "ARC Appliance ${unitCode}, remote id ${houseCode}");
        }
    }

    public static class ELROAB400D extends Lighting1Handler {

        @Inject
        protected ELROAB400D(ManagedCollectionFactory managedCollectionFactory, RFXtrx rfXtrx) {
            super(managedCollectionFactory, rfXtrx, com.rfxcom.rfxtrx.message.Lighting1.SubType.X10,
                    "elroab400d",
                    "${houseCode}-${unitCode}",
                    "ELROAB400D Appliance ${unitCode}",
                    "ELROAB400D Appliance ${unitCode}, remote id ${houseCode}");
        }
    }

    public static class Waveman extends Lighting1Handler {

        @Inject
        protected Waveman(ManagedCollectionFactory managedCollectionFactory, RFXtrx rfXtrx) {
            super(managedCollectionFactory, rfXtrx, com.rfxcom.rfxtrx.message.Lighting1.SubType.X10,
                    "waveman",
                    "${houseCode}-${unitCode}",
                    "Waveman Appliance ${unitCode}",
                    "Waveman Appliance ${unitCode}, remote id ${houseCode}");
        }
    }

    public static class ChaconEMW200 extends Lighting1Handler {

        @Inject
        protected ChaconEMW200(ManagedCollectionFactory managedCollectionFactory, RFXtrx rfXtrx) {
            super(managedCollectionFactory, rfXtrx, com.rfxcom.rfxtrx.message.Lighting1.SubType.X10,
                    "chaconemw200",
                    "${houseCode}-${unitCode}",
                    "ChaconEMW200 Appliance ${unitCode}",
                    "ChaconEMW200 Appliance ${unitCode}, remote id ${houseCode}");
        }
    }

    public static class IMPULS extends Lighting1Handler {

        @Inject
        protected IMPULS(ManagedCollectionFactory managedCollectionFactory, RFXtrx rfXtrx) {
            super(managedCollectionFactory, rfXtrx, com.rfxcom.rfxtrx.message.Lighting1.SubType.X10,
                    "impuls",
                    "${houseCode}-${unitCode}",
                    "IMPULS Appliance ${unitCode}",
                    "IMPULS Appliance ${unitCode}, remote id ${houseCode}");
        }
    }
}
