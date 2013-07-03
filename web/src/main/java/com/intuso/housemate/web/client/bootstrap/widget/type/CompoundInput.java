package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.type.CompoundTypeWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.web.client.event.TypeInputEditedEvent;
import com.intuso.housemate.web.client.object.GWTProxySubType;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 */
public class CompoundInput
        extends SubTypeInputList
        implements TypeInput {

    private final GWTProxyType type;
    private TypeInstances typeInstances;

    public CompoundInput(CompoundTypeWrappable typeWrappable, GWTProxyType type) {
        this.type = type;
    }

    @Override
    public void setTypeInstances(TypeInstances typeInstance) {
        this.typeInstances = typeInstance != null ? typeInstance : new TypeInstances();
        if(typeInstances.size() == 0)
            typeInstances.add(new TypeInstance());
        super.setTypeInstances(this.typeInstances.get(0).getChildValues());
        List<GWTProxySubType> list = (List<GWTProxySubType>) type.getWrapper("sub-types");
        if(list != null)
            setList(list);
    }

    @Override
    public void onTypeInputEdited(TypeInputEditedEvent event) {
        fireEvent(new TypeInputEditedEvent(typeInstances));
    }
}
