package com.intuso.housemate.web.client.bootstrap.widget.value;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.type.SimpleTypeData;
import com.intuso.housemate.api.object.type.Type;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 */
public abstract class Value extends Composite implements ValueListener<com.intuso.housemate.api.object.value.Value<?, ?>> {

    public static Widget getWidget(final com.intuso.housemate.api.object.value.Value<?, ?> value) {
        if(value == null)
            return null;

        Type type = value.getType();

        // type is loaded, return widget now
        if(type != null)
            return getWidget(value, (GWTProxyType) type);
        else {
            final SimplePanel panel = new SimplePanel();
            Housemate.INJECTOR.getProxyRoot().getTypes().load(new LoadManager(new LoadManager.Callback() {
                @Override
                public void failed(HousemateObject.TreeLoadInfo path) {
                    // todo show error
                }

                @Override
                public void allLoaded() {
                    Type loadedType = value.getType();
                    if(loadedType != null)
                        panel.setWidget(getWidget(value, (GWTProxyType) loadedType));
                }
            }, "loadValueType" + value.getId(), new HousemateObject.TreeLoadInfo(value.getTypeId(), new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE))));
            return panel;
        }
    }

    private static Value getWidget(com.intuso.housemate.api.object.value.Value<?, ?> value, GWTProxyType proxyType) {

        Value result = null;
        TypeData typeData = proxyType.getData();
        if(typeData instanceof SimpleTypeData) {
            switch(((SimpleTypeData) typeData).getType()) {
                case String:
                case Integer:
                case Double:
                    result = new LabelSimpleValue();
                    break;
                case Boolean:
                    result = new ThumbsSimpleValue();
                    break;
            }
        }
        if(result == null)
            result = new LabelSimpleValue();
        result.setValue(value);
        return result;
    }

    public Value() {
    }

    public final void setValue(com.intuso.housemate.api.object.value.Value<?, ?> value) {
        if(value != null) {
            value.addObjectListener(this);
            valueChanged(value);
        }
    }

    @Override
    public abstract void valueChanged(com.intuso.housemate.api.object.value.Value<?, ?> value);
}
