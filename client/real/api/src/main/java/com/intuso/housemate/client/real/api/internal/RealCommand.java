package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.comms.api.internal.payload.CommandData;
import com.intuso.housemate.object.api.internal.Command;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import org.slf4j.Logger;

/**
 */
public interface RealCommand
        extends Command<TypeInstanceMap, RealValue<Boolean>, RealList<? extends RealParameter<?>>, RealCommand> {

    /**
     * Performs the command
     * @param values the values of the parameters to use
     */
    void perform(TypeInstanceMap values);

    interface Factory {
        RealCommand create(Logger logger, CommandData data);
    }
}
