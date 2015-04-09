package com.intuso.housemate.realclient.persist;

import com.intuso.housemate.api.object.Renameable;
import com.intuso.housemate.api.object.primary.PrimaryListener;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.real.RealPrimaryObject;
import com.intuso.housemate.persistence.api.Persistence;
import com.intuso.utilities.log.Log;

/**
 * Created by tomc on 03/02/15.
 */
public abstract class PrimaryObjectListener<PO extends RealPrimaryObject<?, ?, ?>> implements PrimaryListener<PO> {

    private final Log log;
    private final Persistence persistence;

    protected PrimaryObjectListener(Log log, Persistence persistence) {
        this.log = log;
        this.persistence = persistence;
    }

    @Override
    public void renamed(PO primaryObject, String oldName, String newName) {
        try {
            TypeInstanceMap values = persistence.getValues(primaryObject.getPath());
            values.getChildren().put(Renameable.NAME_ID, new TypeInstances(new TypeInstance(newName)));
            persistence.saveValues(primaryObject.getPath(), values);
        } catch(Throwable t) {
            log.e("Failed to update persisted name", t);
        }
    }

    @Override
    public void error(PO primaryObject, String error) {

    }

    @Override
    public void running(PO primaryObject, boolean running) {

    }
}
