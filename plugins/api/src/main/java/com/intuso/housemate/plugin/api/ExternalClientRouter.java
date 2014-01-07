package com.intuso.housemate.plugin.api;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Router;
import com.intuso.utilities.log.Log;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 03/01/14
 * Time: 16:40
 * To change this template use File | Settings | File Templates.
 */
public abstract class ExternalClientRouter extends Router {

    /**
     * @param log {@inheritDoc}
     */
    public ExternalClientRouter(Log log) {
        super(log);
    }

    public abstract void start() throws HousemateException;
    public abstract void stop();
}
