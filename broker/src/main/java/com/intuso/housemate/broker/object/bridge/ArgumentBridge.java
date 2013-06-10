package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.argument.Argument;
import com.intuso.housemate.api.object.argument.ArgumentListener;
import com.intuso.housemate.api.object.argument.ArgumentWrappable;

import javax.annotation.Nullable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 17/07/12
 * Time: 23:44
 * To change this template use File | Settings | File Templates.
 */
public class ArgumentBridge
        extends BridgeObject<ArgumentWrappable, NoChildrenWrappable, NoChildrenBridgeObject, ArgumentBridge, ArgumentListener>
        implements Argument<TypeBridge> {

    private final TypeBridge type;

    public ArgumentBridge(BrokerBridgeResources resources, Argument<?> argument) {
        super(resources, new ArgumentWrappable(argument.getId(), argument.getName(), argument.getDescription(), argument.getType().getId()));
        type = resources.getGeneralResources().getBridgeResources().getRoot().getTypes().get(getWrappable().getType());
    }

    @Override
    public TypeBridge getType() {
        return type;
    }

    public final static class Converter implements Function<Argument<?>, ArgumentBridge> {

        private BrokerBridgeResources resources;

        public Converter(BrokerBridgeResources resources) {
            this.resources = resources;
        }

        @Override
        public ArgumentBridge apply(@Nullable Argument<?> argument) {
            return new ArgumentBridge(resources, argument);
        }
    }
}
