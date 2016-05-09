package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;
import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.housemate.client.v1_0.api.object.Object;

/**
 * Created by tomc on 03/11/15.
 */
public class ListMapper {

    public <FROM extends Object<?>, TO extends com.intuso.housemate.client.api.internal.object.Object<?>> RealList<TO, ?>
            map(com.intuso.housemate.client.v1_0.real.api.RealList<FROM, ?> list,
                Function<? super FROM, ? extends TO> convertFrom,
                Function<? super TO, ? extends FROM> convertTo) {
        if(list == null)
            return null;
        else if(list instanceof RealListBridgeReverse)
            return ((RealListBridgeReverse<TO, FROM>) list).getList();
        return new RealListBridge<>(list, convertFrom, convertTo);
    }

    public <FROM extends com.intuso.housemate.client.api.internal.object.Object<?>, TO extends Object<?>> com.intuso.housemate.client.v1_0.real.api.RealList<TO, ?>
            map(RealList<FROM, ?> list,
                Function<? super FROM, ? extends TO> convertFrom,
                Function<? super TO, ? extends FROM> convertTo) {
        if(list == null)
            return null;
        else if(list instanceof RealListBridge)
            return ((RealListBridge<TO, FROM>) list).getList();
        return new RealListBridgeReverse<>(list, convertFrom, convertTo);
    }
}
