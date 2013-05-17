package com.intuso.housemate.core.object.command;

import com.intuso.housemate.core.object.list.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 04/07/12
 * Time: 23:36
 * To change this template use File | Settings | File Templates.
 */
public interface HasCommands<L extends List<? extends Command<?, ?>>> {
    public L getCommands();
}
