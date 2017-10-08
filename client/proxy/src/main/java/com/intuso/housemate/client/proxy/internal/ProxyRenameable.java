package com.intuso.housemate.client.proxy.internal;

import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.object.Command;
import com.intuso.housemate.client.proxy.internal.object.view.CommandView;

/**
 * Created by tomc on 30/01/15.
 */
public interface ProxyRenameable<RENAME_COMMAND extends Command<?, ?, ?, ?>> extends Renameable<RENAME_COMMAND> {
    void viewRenameCommand(CommandView commandView);
}
