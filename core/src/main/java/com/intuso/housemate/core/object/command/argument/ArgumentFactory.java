package com.intuso.housemate.core.object.command.argument;

import com.intuso.housemate.core.object.HousemateObjectFactory;
import com.intuso.housemate.core.resources.Resources;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 11/02/13
 * Time: 09:13
 * To change this template use File | Settings | File Templates.
 */
public interface ArgumentFactory<
            R extends Resources,
            A extends Argument<?>>
        extends HousemateObjectFactory<R, ArgumentWrappable, A> {
}
