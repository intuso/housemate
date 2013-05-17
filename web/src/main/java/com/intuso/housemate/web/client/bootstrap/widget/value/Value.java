package com.intuso.housemate.web.client.bootstrap.widget.value;

import com.google.gwt.user.client.ui.Composite;
import com.intuso.housemate.core.object.type.SimpleTypeWrappable;
import com.intuso.housemate.core.object.type.Type;
import com.intuso.housemate.core.object.type.TypeWrappable;
import com.intuso.housemate.core.object.value.ValueListener;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 28/11/12
 * Time: 22:28
 * To change this template use File | Settings | File Templates.
 */
public abstract class Value extends Composite implements ValueListener<com.intuso.housemate.core.object.value.Value<?, ?>> {

    public static Value getWidget(com.intuso.housemate.core.object.value.Value<?, ?> value) {
        if(value != null) {
            Value result = null;
            Type type = value.getType();
            if(type instanceof GWTProxyType) {
                GWTProxyType proxyType = (GWTProxyType)type;
                TypeWrappable typeWrappable = proxyType.getWrappable();
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

    public final void setValue(com.intuso.housemate.core.object.value.Value<?, ?> value) {
        if(value != null) {
            value.addObjectListener(this);
            valueChanged(value);
        }
    }

    @Override
    public abstract void valueChanged(com.intuso.housemate.core.object.value.Value<?, ?> value);
}
