package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;

/**
 * Created by tomc on 06/11/15.
 */
public class IdentityFunction<O> implements Function<O, O> {
    @Override
    public O apply(O o) {
        return o;
    }
}
