package com.intuso.housemate.client.proxy.internal;

import com.intuso.housemate.client.api.internal.Removeable;
import com.intuso.housemate.client.api.internal.object.Command;
import com.intuso.housemate.client.api.internal.object.view.CommandView;

/**
 * Classes implementing this can be removed
 * @param <REMOVE_COMMAND> the command type
 */
public interface ProxyRemoveable<REMOVE_COMMAND extends Command<?, ?, ?, ?>> extends Removeable<REMOVE_COMMAND> {
    void loadRemoveCommand(CommandView commandView);
}
