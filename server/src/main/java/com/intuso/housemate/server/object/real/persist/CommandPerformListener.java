package com.intuso.housemate.server.object.real.persist;

import com.google.inject.Inject;
import com.intuso.housemate.object.api.internal.Command;
import org.slf4j.Logger;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/02/14
* Time: 19:27
* To change this template use File | Settings | File Templates.
*/
public class CommandPerformListener implements Command.PerformListener<Command<?, ?, ?, ?>> {

    private final Logger logger;
    private final String description;

    @Inject
    public CommandPerformListener(Logger logger, String description) {
        this.logger = logger;
        this.description = description;
    }

    @Override
    public void commandStarted(Command<?, ?, ?, ?> command) {
        logger.debug("Doing " + description);
    }

    @Override
    public void commandFinished(Command<?, ?, ?, ?> command) {
        logger.debug("Done " + description);
    }

    @Override
    public void commandFailed(Command<?, ?, ?, ?> command, String error) {
        logger.debug(description + " failed: " + error);
    }
}
