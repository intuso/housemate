package com.intuso.housemate.server.object.real.persist;

import com.google.inject.Inject;
import com.intuso.housemate.object.real.RealAutomation;
import com.intuso.housemate.persistence.api.Persistence;
import com.intuso.utilities.log.Log;

/**
 * Created by tomc on 03/02/15.
 */
public class AutomationListener
        extends PrimaryObjectListener<RealAutomation>
        implements com.intuso.housemate.api.object.automation.AutomationListener<RealAutomation> {

    @Inject
    protected AutomationListener(Log log, Persistence persistence) {
        super(log, persistence);
    }

    @Override
    public void satisfied(RealAutomation automation, boolean satisfied) {

    }
}
