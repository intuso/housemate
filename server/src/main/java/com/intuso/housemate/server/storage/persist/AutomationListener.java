package com.intuso.housemate.server.storage.persist;

import com.google.inject.Inject;
import com.intuso.housemate.api.object.automation.Automation;
import com.intuso.housemate.persistence.api.Persistence;
import com.intuso.housemate.realclient.storage.persist.PrimaryObjectListener;
import com.intuso.utilities.log.Log;

/**
 * Created by tomc on 03/02/15.
 */
public class AutomationListener
        extends PrimaryObjectListener<Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>
        implements com.intuso.housemate.api.object.automation.AutomationListener<Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> {

    @Inject
    protected AutomationListener(Log log, Persistence persistence) {
        super(log, persistence);
    }

    @Override
    public void satisfied(Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> automation, boolean satisfied) {

    }
}
