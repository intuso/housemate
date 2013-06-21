package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.type.CompoundTypeWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.web.client.event.TypeInputEditedEvent;
import com.intuso.housemate.web.client.object.GWTProxySubType;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 18/06/13
 * Time: 23:15
 * To change this template use File | Settings | File Templates.
 */
public class CompoundArgumentInput
        extends SubTypeInputList
        implements TypeInput {

    private final GWTProxyType type;
    private TypeInstance typeInstance;

    public CompoundArgumentInput(CompoundTypeWrappable typeWrappable, GWTProxyType type) {
        this.type = type;
    }

    @Override
    public void setTypeInstance(TypeInstance typeInstance) {
        this.typeInstance = typeInstance != null ? typeInstance : new TypeInstance();
        setTypeInstances(this.typeInstance.getChildValues());
        List<GWTProxySubType> list = (List<GWTProxySubType>) type.getWrapper("sub-types");
        if(list != null)
            setList(list);
    }

    @Override
    public void onTypeInputEdited(TypeInputEditedEvent event) {
        fireEvent(new TypeInputEditedEvent(typeInstance));
    }
}
