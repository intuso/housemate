package com.intuso.housemate.plugin.rfxcom.old.lighting1;

import com.rfxcom.rfxtrx.RFXtrx;
import com.rfxcom.rfxtrx.util.lighting1.Lighting1;
import org.slf4j.Logger;

/**
 * Created by tomc on 02/12/15.
 */
public class Lighting1ARCHandler extends Lighting1Handler {

    static Lighting1ARCHandler INSTANCE;

    public Lighting1ARCHandler(Logger logger, RFXtrx rfxtrx) {
        super(logger, Lighting1.forARC(rfxtrx));
        INSTANCE = this;
    }

    @Override
    public String getDeviceName(byte houseId, byte unitCode) {
        return "Lighting1 ARC " + houseId + "/" + (int)unitCode;
    }

    @Override
    public String getDriverId() {
        return "rfxcom-lighting1-arc";
    }
}
