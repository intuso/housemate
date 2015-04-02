package com.intuso.housemate.api.object.realclient;

import com.intuso.housemate.api.object.list.List;

/**
 *
 * Interface to show that the implementing object has a list of commands
 */
public interface HasRealClients<L extends List<? extends RealClient<?, ?, ?, ?, ?, ?, ?, ?>>> {

    /**
     * Gets the commands list
     * @return the commands list
     */
    public L getRealClients();
}
