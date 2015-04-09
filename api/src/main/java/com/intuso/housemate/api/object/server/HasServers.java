package com.intuso.housemate.api.object.server;

import com.intuso.housemate.api.object.list.List;

/**
 *
 * Interface to show that the implementing object has a list of commands
 */
public interface HasServers<L extends List<? extends Server<?, ?, ?, ?, ?, ?, ?, ?>>> {

    /**
     * Gets the commands list
     * @return the commands list
     */
    public L getServers();
}
