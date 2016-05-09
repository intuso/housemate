package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.intuso.housemate.client.v1_0.api.object.SubType;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.proxy.api.LoadManager;
import com.intuso.housemate.client.v1_0.data.api.RemoteObject;
import com.intuso.housemate.client.v1_0.data.api.TreeLoadInfo;
import com.intuso.housemate.web.client.event.UserInputEvent;
import com.intuso.housemate.web.client.handler.HasUserInputHandlers;
import com.intuso.housemate.web.client.handler.UserInputHandler;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxySubType;
import com.intuso.housemate.web.client.object.GWTProxyType;

import java.util.List;

/**
 */
public interface TypeInput extends IsWidget, HasUserInputHandlers {

    Type.Instances getTypeInstances();

    Factory FACTORY = new Factory();

    final class Factory {

        private Factory() {}

        public TypeInput create(final GWTProxyList<Type.Data<?>, GWTProxyType> types, final String typeId, final Type.Instances typeInstances) {
            GWTProxyType type = types.get(typeId);
            if(type != null)
                return _create(types, type, typeInstances);
            else {
                final LazyLoaded lazyLoaded = new LazyLoaded(typeInstances);
                types.load(new LoadManager(new LoadManager.Callback() {
                    @Override
                    public void failed(List<String> errors) {
                        // todo show error
                    }

                    @Override
                    public void succeeded() {
                        GWTProxyType loadedType = types.get(typeId);
                        if (loadedType != null)
                            lazyLoaded.setWidget(_create(types, types.get(typeId), typeInstances));
                    }
                }, new TreeLoadInfo(typeId, new TreeLoadInfo(RemoteObject.EVERYTHING_RECURSIVE))));
                return lazyLoaded;
            }
        }

        private TypeInput _create(final GWTProxyList<Type.Data<?>, GWTProxyType> types, GWTProxyType type, final Type.Instances typeInstances) {
            if(type == null)
                return null;
            Type.Data data = type.getData();
            if (data instanceof Type.SimpleData) {
                if (((Type.SimpleData) data).getType() == Type.SimpleData.SimpleType.Boolean)
                    return new CheckBoxInput(typeInstances);
                else
                    return new TextInput(data, typeInstances);
            } else if (data instanceof Type.ChoiceData) {
                if (data.getMinValues() == 1 && data.getMaxValues() == 1)
                    return new SingleSelectInput(types, type, typeInstances);
                else
                    return new MultiSelectInput(type, typeInstances);
            } else if (data instanceof Type.RegexData)
                return new TextInput(data, typeInstances);
            else if (data instanceof Type.ObjectData)
                return new ObjectBrowserInput((Type.ObjectData) data, typeInstances);
            else if (data instanceof Type.CompoundData)
                return new CompoundTypeInput(types, (GWTProxyList<SubType.Data, GWTProxySubType>) type.getChild("sub-types"), typeInstances);
            return null;
        }

        public TypeInput create(GWTProxyList<Type.Data<?>, GWTProxyType> types, String typeId, Type.Instances typeInstances, UserInputHandler handler) {
            TypeInput result = create(types, typeId, typeInstances);
            if(result != null)
                result.addUserInputHandler(handler);
            return result;
        }
    }

    class LazyLoaded extends SimplePanel implements TypeInput, UserInputHandler {

        private final Type.Instances typeInstances;

        public LazyLoaded(Type.Instances typeInstances) {
            this.typeInstances = typeInstances;
        }

        @Override
        public Type.Instances getTypeInstances() {
            return typeInstances;
        }

        @Override
        public HandlerRegistration addUserInputHandler(UserInputHandler handler) {
            return addHandler(handler, UserInputEvent.TYPE);
        }

        public void setWidget(TypeInput typeInput) {
            typeInput.addUserInputHandler(this);
            super.setWidget(typeInput);
        }

        @Override
        public void onUserInput(UserInputEvent event) {
            fireEvent(event);
        }
    }
}
