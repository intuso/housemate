package com.intuso.housemate.web.client.bootstrap.widget.value;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.proxy.api.LoadManager;
import com.intuso.housemate.client.v1_0.data.api.RemoteObject;
import com.intuso.housemate.client.v1_0.data.api.TreeLoadInfo;
import com.intuso.housemate.client.v1_0.api.object.Value;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;
import com.intuso.housemate.web.client.object.GWTProxyValue;

import java.util.List;

/**
 */
public interface ValueDisplay extends IsWidget, Value.Listener<GWTProxyValue> {

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
                }, new TreeLoadInfo(value.getTypeId(), new TreeLoadInfo(RemoteObject.EVERYTHING_RECURSIVE))));
                return panel;
            }
        }

        private ValueDisplay create(GWTProxyValue value, GWTProxyType proxyType) {

            ValueDisplay result = null;
            TypeData typeData = proxyType.getData();
            if (typeData instanceof Type.SimpleData) {
                switch (((Type.SimpleData) typeData).getType()) {
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

    public void setValue(GWTProxyValue value);
}
