package com.intuso.housemate.server.object.real.persist;

import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.RealDevice;
import com.intuso.housemate.comms.api.internal.payload.DeviceData;
import com.intuso.housemate.object.api.internal.Device;
import com.intuso.housemate.object.api.internal.TypeInstance;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.housemate.persistence.api.internal.Persistence;
import com.intuso.utilities.log.Log;

/**
 * Created by tomc on 03/02/15.
 */
public class DeviceListener implements Device.Listener<RealDevice<?>> {

    private final Log log;
    private final Persistence persistence;

    @Inject
    protected DeviceListener(Log log, Persistence persistence) {

        this.log = log;
        this.persistence = persistence;
    }

    @Override
    public void renamed(RealDevice primaryObject, String oldName, String newName) {
        try {
            TypeInstanceMap values = persistence.getValues(primaryObject.getPath());
            values.getChildren().put(DeviceData.NAME_ID, new TypeInstances(new TypeInstance(newName)));
            persistence.saveValues(primaryObject.getPath(), values);
        } catch(Throwable t) {
            log.e("Failed to update persisted name", t);
        }
    }

    @Override
    public void error(RealDevice primaryObject, String error) {

    }

    @Override
    public void running(RealDevice primaryObject, boolean running) {

    }

    @Override
    public void driverLoaded(RealDevice usesDriver, boolean loaded) {

    }
}
