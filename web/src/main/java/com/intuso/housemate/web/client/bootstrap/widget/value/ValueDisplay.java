package com.intuso.housemate.web.client.bootstrap.widget.value;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.type.SimpleTypeData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;
import com.intuso.housemate.web.client.object.GWTProxyValue;

import java.util.List;

/**
 */
public interface ValueDisplay extends IsWidget, ValueListener<com.intuso.housemate.api.object.value.Value<?, ?>> {

    public final static Factory FACTORY = new Factory();

    public class Factory {

        private Factory() {}

        public IsWidget create(final GWTProxyList<TypeData<?>, GWTProxyType> types, final GWTProxyValue value) {
            if (value == null)
                return null;

            GWTProxyType type = types.get(value.getTypeId());

            // type is loaded, return widget now
            if (type != null)
                return create(value, type);
            else {
                final SimplePanel panel = new SimplePanel();
                types.load(new LoadManager(new LoadManager.Callback() {
                    @Override
                    public void failed(List<String> errors) {
                        // todo show error
                    }

                    @Override
                    public void succeeded() {
                        GWTProxyType loadedType = types.get(value.getTypeId());
                        if (loadedType != null)
                            panel.setWidget(create(value, loadedType));
                    }
                }, new HousemateObject.TreeLoadInfo(value.getTypeId(), new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE))));
                return panel;
            }
        }

        private ValueDisplay create(com.intuso.housemate.api.object.value.Value<?, ?> value, GWTProxyType proxyType) {

            ValueDisplay result = null;
            TypeData typeData = proxyType.getData();
            if (typeData instanceof SimpleTypeData) {
                switch (((SimpleTypeData) typeData).getType()) {
                    case String:
                    case Integer:
                    case Double:
                        result = new LabelSimpleValue();
                        break;
                    case Boolean:
                        result = new BooleanValueDisplay();
                        break;
                }
            }
            if (result == null)
                result = new LabelSimpleValue();
            result.setValue(value);
            return result;
        }
    }

    public void setValue(com.intuso.housemate.api.object.value.Value<?, ?> value);
}
