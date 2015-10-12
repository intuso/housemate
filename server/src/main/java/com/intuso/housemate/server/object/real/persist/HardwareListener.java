package com.intuso.housemate.server.object.real.persist;

import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.RealHardware;
import com.intuso.housemate.comms.api.internal.payload.DeviceData;
import com.intuso.housemate.object.api.internal.Hardware;
import com.intuso.housemate.object.api.internal.TypeInstance;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.housemate.persistence.api.internal.Persistence;
import com.intuso.utilities.log.Log;

/**
 * Created by tomc on 03/02/15.
 */
public class HardwareListener implements Hardware.Listener<RealHardware<?>> {

    private final Log log;
    private final Persistence persistence;

    @Inject
    protected HardwareListener(Log log, Persistence persistence) {
        this.log = log;
        this.persistence = persistence;
    }

    @Override
    public void renamed(RealHardware primaryObject, String oldName, String newName) {
        try {
            TypeInstanceMap values = persistence.getValues(primaryObject.getPath());
            values.getChildren().put(DeviceData.NAME_ID, new TypeInstances(new TypeInstance(newName)));
            persistence.saveValues(primaryObject.getPath(), values);
        } catch(Throwable t) {
            log.e("Failed to update persisted name", t);
        }
    }

    @Override
    public void error(RealHardware primaryObject, String error) {

    }

    @Override
    public void running(RealHardware primaryObject, boolean running) {

    }

    @Override
    public void driverLoaded(RealHardware usesDriver, boolean loaded) {

    }
}
