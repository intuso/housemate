package com.intuso.housemate.server.object.real.persist;

import com.google.inject.Inject;
import com.intuso.housemate.object.real.RealCommand;
import com.intuso.utilities.log.Log;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/02/14
* Time: 19:27
* To change this template use File | Settings | File Templates.
*/
public class CommandPerformListener implements com.intuso.housemate.api.object.command.CommandPerformListener<RealCommand> {

    private final Log log;
    private final String description;

    @Inject
    public CommandPerformListener(Log log, String description) {
        this.log = log;
        this.description = description;
    }

    @Override
    public void commandStarted(RealCommand command) {
        log.d("Doing " + description);
    }

    @Override
    public void commandFinished(RealCommand command) {
        log.d("Done " + description);
    }

    @Override
    public void commandFailed(RealCommand command, String error) {
        log.d(description + " failed: " + error);
    }
}
