package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.type.*;
import com.intuso.housemate.web.client.handler.HasUserInputHandlers;
import com.intuso.housemate.web.client.handler.UserInputHandler;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxySubType;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 */
public interface TypeInput extends IsWidget, HasUserInputHandlers {

    public TypeInstances getTypeInstances();

    public final static Factory FACTORY = new Factory();

    public final class Factory {

        private Factory() {}

        public TypeInput create(GWTProxyType type, TypeInstances typeInstances) {
            if(type == null)
                return null;
            TypeData typeData = type.getData();
            if(typeData instanceof SimpleTypeData) {
                if(((SimpleTypeData)typeData).getType() == SimpleTypeData.Type.Boolean)
                    return new CheckBoxInput(typeInstances);
                else
                    return new TextInput(typeData, typeInstances);
            } else if(typeData instanceof ChoiceTypeData) {
                if(typeData.getMinValues() == 1 && typeData.getMaxValues() == 1)
                    return new SingleSelectInput(type, typeInstances);
                else
                    return new MultiSelectInput(type, typeInstances);
            } else if(typeData instanceof RegexTypeData)
                return new TextInput(typeData, typeInstances);
            else if(typeData instanceof ObjectTypeData)
                return new ObjectBrowserInput((ObjectTypeData) typeData, typeInstances);
            else if(typeData instanceof CompoundTypeData)
                return new CompoundTypeInput((GWTProxyList<SubTypeData, GWTProxySubType>) type.getChild("sub-types"), typeInstances);
            return null;
        }

        public TypeInput create(GWTProxyType type, TypeInstances typeInstances, UserInputHandler handler) {
            TypeInput result = create(type, typeInstances);
            if(result != null)
                result.addUserInputHandler(handler);
            return result;
        }
    }
}
