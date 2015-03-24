package com.intuso.housemate.realclient.persist;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.RealValueBase;
import com.intuso.housemate.persistence.api.DetailsNotFoundException;
import com.intuso.housemate.persistence.api.Persistence;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/02/14
* Time: 19:25
* To change this template use File | Settings | File Templates.
*/
public class ValueWatcher  {

    private final Log log;
    private final Persistence persistence;
    private final Listener listener = new Listener();

    @Inject
    public ValueWatcher(Log log, Persistence persistence) {
        this.log = log;
        this.persistence = persistence;
    }

    public ListenerRegistration watch(RealValueBase<?, ?, ?, ?, ?> value) {
        try {
            value.setTypedValues((List)RealType.deserialiseAll(value.getType(), persistence.getTypeInstances(value.getPath())));
        } catch(DetailsNotFoundException e) {
            // nothing to load, so we should persist the current value to make sure that what is persisted is always in
            // sync with what the current value is
            listener.valueChanged(value);
        } catch(HousemateException e) {
            log.e("Failed to load initial property value");
        }
        return value.addObjectListener(listener);
    }

    private class Listener implements ValueListener<RealValueBase<?, ?, ?, ?, ?>> {

        @Override
        public void valueChanging(RealValueBase<?, ?, ?, ?, ?> value) {
            // do nothing
        }

        @Override
        public void valueChanged(RealValueBase<?, ?, ?, ?, ?> value) {
            try {
                persistence.saveTypeInstances(value.getPath(), value.getTypeInstances());
            } catch(HousemateException e) {
                log.e("Failed to save property value", e);
            }
        }
    }
}
