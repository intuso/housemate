package com.intuso.housemate.object.real;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.subtype.SubType;
import com.intuso.housemate.api.object.subtype.SubTypeListener;
import com.intuso.housemate.api.object.subtype.SubTypeWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 10/07/12
 * Time: 00:58
 * To change this template use File | Settings | File Templates.
 */
public class RealSubType<O>
        extends RealObject<SubTypeWrappable, NoChildrenWrappable, RealObject<NoChildrenWrappable, ? ,?, ?>, SubTypeListener>
        implements SubType<RealType<?, ?, O>> {

    private RealType<?, ?, O> type;

    public RealSubType(RealResources resources, String id, String name, String description, RealType<?, ?, O> type) {
        this(resources, id, name, description, type.getId());
    }

    public RealSubType(RealResources resources, String id, String name, String description, String typeId) {
        super(resources, new SubTypeWrappable(id, name, description, typeId));
        this.type = type;
    }

    @Override
    public final RealType<?, ?, O> getType() {
        return type;
    }
}
