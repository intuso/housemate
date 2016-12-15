package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.RealParameter;
import com.intuso.housemate.client.v1_0.real.api.RealType;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created by tomc on 03/11/15.
 */
public class RealParameterBridgeReverse<FROM, TO> implements RealParameter<TO, RealType<TO, ?>, RealParameterBridgeReverse<FROM, TO>> {

    private final com.intuso.housemate.client.real.api.internal.RealParameter<FROM, ?, ?> parameter;

    @Inject
    public RealParameterBridgeReverse(@Assisted com.intuso.housemate.client.real.api.internal.RealParameter<FROM, ?, ?> parameter) {
        this.parameter = parameter;
    }

    public com.intuso.housemate.client.real.api.internal.RealParameter<FROM, ?, ?> getParameter() {
        return parameter;
    }

    @Override
    public RealType<TO, ?> getType() {
        return null; // todo
    }

    @Override
    public String getId() {
        return parameter.getId();
    }

    @Override
    public String getName() {
        return parameter.getName();
    }

    @Override
    public String getDescription() {
        return parameter.getDescription();
    }

    @Override
    public ListenerRegistration addObjectListener(Listener<? super RealParameterBridgeReverse<FROM, TO>> listener) {
        return null; // todo
    }
}
