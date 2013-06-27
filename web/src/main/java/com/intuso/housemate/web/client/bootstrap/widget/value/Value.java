package com.intuso.housemate.web.client.bootstrap.widget.value;

import com.google.gwt.user.client.ui.Composite;
import com.intuso.housemate.api.object.type.SimpleTypeWrappable;
import com.intuso.housemate.api.object.type.Type;
import com.intuso.housemate.api.object.type.TypeWrappable;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 */
public abstract class Value extends Composite implements ValueListener<com.intuso.housemate.api.object.value.Value<?, ?>> {

    public static Value getWidget(com.intuso.housemate.api.object.value.Value<?, ?> value) {
        if(value != null) {
            Value result = null;
            Type type = value.getType();
            if(type instanceof GWTProxyType) {
                GWTProxyType proxyType = (GWTProxyType)type;
                TypeWrappable typeWrappable = proxyType.getData();
                if(typeWrappable instanceof SimpleTypeWrappable) {
                    switch(((SimpleTypeWrappable) typeWrappable).getType()) {
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
            }
            if(result == null)
                result = new LabelSimpleValue();
            result.setValue(value);
            return result;
        }
        return null;
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
