package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.type.*;
import com.intuso.housemate.object.proxy.LoadManager;
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

    public TypeInstances getTypeInstances();

    public final static Factory FACTORY = new Factory();

    public final class Factory {

        private Factory() {}

        public TypeInput create(final GWTProxyList<TypeData<?>, GWTProxyType> types, final String typeId, final TypeInstances typeInstances) {
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
                }, new HousemateObject.TreeLoadInfo(typeId, new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE))));
                return lazyLoaded;
            }
        }

        private TypeInput _create(final GWTProxyList<TypeData<?>, GWTProxyType> types, GWTProxyType type, final TypeInstances typeInstances) {
            if(type == null)
                return null;
            TypeData typeData = type.getData();
            if (typeData instanceof SimpleTypeData) {
                if (((SimpleTypeData) typeData).getType() == SimpleTypeData.Type.Boolean)
                    return new CheckBoxInput(typeInstances);
                else
                    return new TextInput(typeData, typeInstances);
            } else if (typeData instanceof ChoiceTypeData) {
                if (typeData.getMinValues() == 1 && typeData.getMaxValues() == 1)
                    return new SingleSelectInput(types, type, typeInstances);
                else
                    return new MultiSelectInput(type, typeInstances);
            } else if (typeData instanceof RegexTypeData)
                return new TextInput(typeData, typeInstances);
            else if (typeData instanceof ObjectTypeData)
                return new ObjectBrowserInput((ObjectTypeData) typeData, typeInstances);
            else if (typeData instanceof CompoundTypeData)
                return new CompoundTypeInput(types, (GWTProxyList<SubTypeData, GWTProxySubType>) type.getChild("sub-types"), typeInstances);
            return null;
        }

        public TypeInput create(GWTProxyList<TypeData<?>, GWTProxyType> types, String typeId, TypeInstances typeInstances, UserInputHandler handler) {
            TypeInput result = create(types, typeId, typeInstances);
            if(result != null)
                result.addUserInputHandler(handler);
            return result;
        }
    }

    public static class LazyLoaded extends SimplePanel implements TypeInput, UserInputHandler {

        private final TypeInstances typeInstances;

        public LazyLoaded(TypeInstances typeInstances) {
            this.typeInstances = typeInstances;
        }

        @Override
        public TypeInstances getTypeInstances() {
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
