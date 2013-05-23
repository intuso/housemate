package com.intuso.housemate.api.object.connection;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.NoChildrenWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 21:45
 * To change this template use File | Settings | File Templates.
 */
public final class ClientWrappable extends HousemateObjectWrappable<NoChildrenWrappable> {

    public enum Type {
        ROUTER,
        REAL,
        PROXY
    }

    private Type type;

    private ClientWrappable() {}

    public ClientWrappable(String id, String name, String description, Type type) {
        super(id, name,  description);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new ClientWrappable(getId(), getName(), getDescription(), type);
    }
}
