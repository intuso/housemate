package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;
import com.intuso.housemate.client.v1_0.real.api.RealParameter;

/**
 * Created by tomc on 03/11/15.
 */
public class ParameterMapper {

    private final Function<com.intuso.housemate.client.real.api.internal.RealParameter<?, ?, ?>, RealParameter<?, ?, ?>> toV1_0Function = new Function<com.intuso.housemate.client.real.api.internal.RealParameter<?, ?, ?>, RealParameter<?, ?, ?>>() {
        @Override
        public RealParameter<?, ?, ?> apply(com.intuso.housemate.client.real.api.internal.RealParameter<?, ?, ?> parameter) {
            return map(parameter);
        }
    };

    private final Function<RealParameter<?, ?, ?>, com.intuso.housemate.client.real.api.internal.RealParameter<?, ?, ?>> fromV1_0Function = new Function<RealParameter<?, ?, ?>, com.intuso.housemate.client.real.api.internal.RealParameter<?, ?, ?>>() {
        @Override
        public com.intuso.housemate.client.real.api.internal.RealParameter<?, ?, ?> apply(RealParameter<?, ?, ?> parameter) {
            return map(parameter);
        }
    };

    public Function<com.intuso.housemate.client.real.api.internal.RealParameter<?, ?, ?>, RealParameter<?, ?, ?>>
            getToV1_0Function() {
        return toV1_0Function;
    }

    public Function<RealParameter<?, ?, ?>, com.intuso.housemate.client.real.api.internal.RealParameter<?, ?, ?>>
            getFromV1_0Function() {
        return fromV1_0Function;
    }

    public <FROM, TO> RealParameter<TO, ?, ?> map(com.intuso.housemate.client.real.api.internal.RealParameter<FROM, ?, ?> parameter) {
        if(parameter == null)
            return null;
        if(parameter instanceof RealParameterBridge)
            return ((RealParameterBridge<TO, FROM>)parameter).getParameter();
        return new RealParameterBridgeReverse<>(parameter);
    }

    public <FROM, TO> com.intuso.housemate.client.real.api.internal.RealParameter<TO, ?, ?> map(RealParameter<FROM, ?, ?> parameter) {
        if(parameter == null)
            return null;
        if(parameter instanceof RealParameterBridgeReverse)
            return ((RealParameterBridgeReverse<TO, FROM>)parameter).getParameter();
        return new RealParameterBridge<>(parameter);
    }
}
