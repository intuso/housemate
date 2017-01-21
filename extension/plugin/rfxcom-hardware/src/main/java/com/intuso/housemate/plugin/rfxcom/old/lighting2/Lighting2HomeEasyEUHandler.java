package com.intuso.housemate.plugin.rfxcom.old.lighting2;

import com.rfxcom.rfxtrx.RFXtrx;
import com.rfxcom.rfxtrx.util.lighting2.Lighting2;
import org.slf4j.Logger;

/**
 * Created by tomc on 02/12/15.
 */
public class Lighting2HomeEasyEUHandler extends Lighting2Handler {

    static Lighting2HomeEasyEUHandler INSTANCE;

    public Lighting2HomeEasyEUHandler(Logger logger, RFXtrx rfxtrx) {
        super(logger, Lighting2.forHomeEasyEU(rfxtrx));
        INSTANCE = this;
    }

    @Override
    public String getDeviceName(int houseId, byte unitCode) {
        return "Lighting2 HomeEasy EU " + houseId + "/" + (int)unitCode;
    }

    @Override
    public String getDriverId() {
        return "rfxcom-lighting2-homeeasyeu";
    }
}
