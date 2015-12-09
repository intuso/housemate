package com.intuso.housemate.server.object.real.persist;

import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.RealAutomation;
import com.intuso.housemate.comms.api.internal.payload.AutomationData;
import com.intuso.housemate.object.api.internal.Automation;
import com.intuso.housemate.object.api.internal.TypeInstance;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.housemate.persistence.api.internal.Persistence;
import org.slf4j.Logger;

/**
 * Created by tomc on 03/02/15.
 */
public class AutomationListener
        implements Automation.Listener<RealAutomation> {

    private final Logger logger;
    private final Persistence persistence;

    @Inject
    protected AutomationListener(Logger logger, Persistence persistence) {

        this.logger = logger;
        this.persistence = persistence;
    }

    @Override
    public void renamed(RealAutomation primaryObject, String oldName, String newName) {
        try {
            TypeInstanceMap values = persistence.getValues(primaryObject.getPath());
            values.getChildren().put(AutomationData.NAME_ID, new TypeInstances(new TypeInstance(newName)));
            persistence.saveValues(primaryObject.getPath(), values);
        } catch(Throwable t) {
            logger.error("Failed to update persisted name", t);
        }
    }

    @Override
    public void error(RealAutomation primaryObject, String error) {

    }

    @Override
    public void running(RealAutomation primaryObject, boolean running) {

    }

    @Override
    public void satisfied(RealAutomation automation, boolean satisfied) {

    }
}
