package com.intuso.housemate.web.client.bootstrap.widget.value;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.intuso.housemate.api.object.type.SimpleTypeData;
import com.intuso.housemate.api.object.type.Type;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 */
public interface ValueDisplay extends IsWidget, ValueListener<com.intuso.housemate.api.object.value.Value<?, ?>> {

    public final static Factory FACTORY = new Factory();

    public class Factory {

        private Factory() {}

        public IsWidget create(final com.intuso.housemate.api.object.value.Value<?, ?> value) {
            if (value == null)
                return null;

            Type type = value.getType();

            // type is loaded, return widget now
            if (type != null)
                return create(value, (GWTProxyType) type);
            else {
                final SimplePanel panel = new SimplePanel();
                // todo
                /*Housemate.INJECTOR.getProxyRoot().getTypes().load(new LoadManager(new LoadManager.Callback() {
                    @Override
                    public void failed(HousemateObject.TreeLoadInfo path) {
                        // todo show error
                    }

                    @Override
                    public void allLoaded() {
                        Type loadedType = value.getType();
                        if (loadedType != null)
                            panel.setWidget(create(value, (GWTProxyType) loadedType));
                    }
                }, "loadValueType" + value.getId(), new HousemateObject.TreeLoadInfo(value.getTypeId(), new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE))));*/
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
