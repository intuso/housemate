package com.intuso.housemate.comms.api.internal;

import com.intuso.utilities.object.BaseObject;
import com.intuso.utilities.object.Data;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 03/07/12
 * Time: 22:17
 * To change this template use File | Settings | File Templates.
 */
public interface ObjectFactory<DATA extends Data<?>, O extends BaseObject<? extends DATA, ?, ?>> {
    O create(Logger logger, DATA data);
}
