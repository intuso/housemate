package com.intuso.housemate.client.proxy.api.internal;

import com.intuso.housemate.client.api.internal.Removeable;
import com.intuso.housemate.client.api.internal.object.Command;

/**
 * Classes implementing this can be removed
 * @param <REMOVE_COMMAND> the command type
 */
public interface ProxyRemoveable<REMOVE_COMMAND extends Command<?, ?, ?, ?>> extends Removeable<REMOVE_COMMAND> {}
