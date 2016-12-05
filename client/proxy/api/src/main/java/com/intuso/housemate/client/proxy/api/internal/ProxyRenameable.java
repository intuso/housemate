package com.intuso.housemate.client.proxy.api.internal;

import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.object.Command;

/**
 * Created by tomc on 30/01/15.
 */
public interface ProxyRenameable<RENAME_COMMAND extends Command<?, ?, ?, ?>> extends Renameable<RENAME_COMMAND> {}
