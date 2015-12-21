package com.intuso.housemate.server.object.real.persist;

import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.RealValueBase;
import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.housemate.object.api.internal.ValueBase;
import com.intuso.housemate.persistence.api.internal.DetailsNotFoundException;
import com.intuso.housemate.persistence.api.internal.Persistence;
import com.intuso.utilities.listener.ListenerRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/02/14
* Time: 19:25
* To change this template use File | Settings | File Templates.
*/
public class ValueBaseWatcher {

    private final static Logger logger = LoggerFactory.getLogger(ValueBaseWatcher.class);

    private final Persistence persistence;
    private final Listener listener = new Listener();

    @Inject
    public ValueBaseWatcher(Persistence persistence) {
        this.persistence = persistence;
    }

    public ListenerRegistration watch(RealValueBase<?, ?, ?> value) {
        try {
            value.setTypedValues((List) RealTypeImpl.deserialiseAll(value.getType(), persistence.getTypeInstances(value.getPath())));
        } catch(DetailsNotFoundException e) {
            // nothing to load, so we should persist the current value to make sure that what is persisted is always in
            // sync with what the current value is
            listener.valueChanged(value);
        } catch(Throwable t) {
            logger.error("Failed to load initial property value");
        }
        return ((RealValueBase<?, ValueBase.Listener<? super ValueBase<TypeInstances, ?, ?>>, ?>)value).addObjectListener(listener);
    }

    private class Listener implements ValueBase.Listener<ValueBase<TypeInstances, ?, ?>> {

        @Override
        public void valueChanging(ValueBase<TypeInstances, ?, ?> value) {
            // do nothing
        }

        @Override
        public void valueChanged(ValueBase<TypeInstances, ?, ?> value) {
            try {
                persistence.saveTypeInstances(value.getPath(), value.getValue());
            } catch(Throwable t) {
                logger.error("Failed to save property value", t);
            }
        }
    }
}
