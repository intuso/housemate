package com.intuso.housemate.api.object.automation;

import com.intuso.housemate.api.object.list.List;

/**
 *
 * Interface to show that the implementing object has a list of automations
 */
public interface HasAutomations<L extends List<? extends Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>> {

    /**
     * Gets the automation list
     * @return the automation list
     */
    public L getAutomations();
}
