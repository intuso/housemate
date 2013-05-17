package com.intuso.housemate.core.object.command.argument;

import com.intuso.housemate.core.object.list.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 04/07/12
 * Time: 22:38
 * To change this template use File | Settings | File Templates.
 */
public interface HasArguments<L extends List<? extends Argument<?>>> {
    public L getArguments();
}
