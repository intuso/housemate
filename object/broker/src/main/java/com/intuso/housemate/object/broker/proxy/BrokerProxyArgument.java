package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.command.argument.Argument;
import com.intuso.housemate.api.object.command.argument.ArgumentListener;
import com.intuso.housemate.api.object.command.argument.ArgumentWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 10/07/12
 * Time: 00:57
 * To change this template use File | Settings | File Templates.
 */
public class BrokerProxyArgument
        extends BrokerProxyObject<ArgumentWrappable, NoChildrenWrappable, NoChildrenBrokerProxyObject, BrokerProxyArgument, ArgumentListener>
        implements Argument<BrokerProxyType> {

    private BrokerProxyType type;

    public BrokerProxyArgument(BrokerProxyResources<NoChildrenBrokerProxyObjectFactory> resources, ArgumentWrappable wrappable) {
        super(resources, wrappable);
    }

    @Override
    public void getChildObjects() {
        super.getChildObjects();
        type = getResources().getRoot().getTypes().get(getWrappable().getType());
        if(type == null)
            getLog().e("Could not unwrap value, value type \"" + getWrappable().getType() + "\" is not known");
    }

    public final BrokerProxyType getType() {
        return type;
    }
}
