package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.argument.Argument;
import com.intuso.housemate.api.object.argument.ArgumentListener;
import com.intuso.housemate.api.object.argument.ArgumentWrappable;
import com.intuso.housemate.object.real.RealType;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 10/07/12
 * Time: 00:58
 * To change this template use File | Settings | File Templates.
 */
public class BrokerRealArgument<O>
        extends BrokerRealObject<ArgumentWrappable, NoChildrenWrappable, NoChildrenBrokerRealObject, ArgumentListener>
        implements Argument<RealType<?, ?, O>> {

    private RealType<?, ?, O> type;

    public BrokerRealArgument(BrokerRealResources resources, String id, String name, String description, RealType<?, ?, O> type) {
        super(resources, new ArgumentWrappable(id, name, description, type.getId()));
        this.type = type;
    }

    @Override
    public final RealType<?, ?, O> getType() {
        return type;
    }
}
