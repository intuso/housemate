package com.intuso.housemate.web.client.bootstrap.widget.value;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.api.object.Value;
import com.intuso.housemate.client.v1_0.data.api.ChildOverview;
import com.intuso.housemate.web.client.bootstrap.widget.list.NestedList;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;
import com.intuso.housemate.web.client.object.GWTProxyValue;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 23/09/13
 * Time: 21:16
 * To change this template use File | Settings | File Templates.
 */
public class ValueList extends NestedList<Value.Data, GWTProxyValue> {

    private GWTProxyList<Type.Data<?>, GWTProxyType> types;

    public void setTypes(GWTProxyList<Type.Data<?>, GWTProxyType> types) {
        this.types = types;
    }

    @Override
    protected IsWidget getWidget(ChildOverview childOverview, GWTProxyValue value) {
        return ValueDisplay.FACTORY.create(types, value);
    }
}
