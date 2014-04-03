package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.type.*;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.bootstrap.widget.list.WidgetList;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 */
public abstract class TypeInputList<DATA extends HousemateData<?>, OBJECT extends ProxyObject<DATA, ?, ?, ?, ?>>
        extends WidgetList<DATA, OBJECT> {

    private final TypeInstanceMap typeInstanceMap;

    public TypeInputList(GWTProxyList<DATA, OBJECT> list, TypeInstances typeInstances) {
        super(list, null, null, true);
        while(typeInstances.size() > 0 && typeInstances.get(0) == null)
            typeInstances.remove(0);
        if(typeInstances.size() == 0)
            typeInstances.add(new TypeInstance());
        typeInstanceMap = typeInstances.get(0).getChildValues();
        loadRows();
    }

    protected IsWidget getWidget(GWTProxyType type, String key) {
        TypeInstances typeInstances = typeInstanceMap.get(key);
        if(typeInstances == null) {
            typeInstances = new TypeInstances();
            typeInstanceMap.put(key, typeInstances);
        }
        return getWidget(type, typeInstances);
    }

    protected IsWidget getWidget(GWTProxyType type, TypeInstances typeInstances) {
        return getInput(type, typeInstances);
    }

    public static TypeInput getInput(GWTProxyType type, TypeInstances typeInstances) {
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
            return new CompoundInput((CompoundTypeData) typeData, type, typeInstances);
        return null;
    }
}
