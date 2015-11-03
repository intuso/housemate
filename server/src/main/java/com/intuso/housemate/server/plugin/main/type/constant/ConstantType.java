package com.intuso.housemate.server.plugin.main.type.constant;

import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.housemate.client.real.api.internal.RealType;
import com.intuso.housemate.client.real.impl.internal.RealListImpl;
import com.intuso.housemate.client.real.impl.internal.RealOptionImpl;
import com.intuso.housemate.client.real.impl.internal.RealSubTypeImpl;
import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import com.intuso.housemate.client.real.impl.internal.type.RealChoiceType;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.comms.api.internal.payload.TypeData;
import com.intuso.housemate.object.api.internal.List;
import com.intuso.housemate.object.api.internal.TypeInstance;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class ConstantType extends RealChoiceType<ConstantInstance<Object>> implements List.Listener<RealType<?>> {

    public final static String ID = "constant";
    public final static String NAME = "Constant";
    public final static String DESCRIPTION = "A constant value";

    public final static String SUB_TYPE_ID = "value";
    public final static String SUB_TYPE_NAME = "Value";
    public final static String SUB_TYPE_DESCRIPTION = "The value of the constant";

    private final ListenersFactory listenersFactory;

    private final RealListImpl<TypeData<?>, RealTypeImpl<?, ?, ?>> types;

    @Inject
    public ConstantType(Log log, ListenersFactory listenersFactory, RealListImpl<TypeData<?>, RealTypeImpl<?, ?, ?>> types) {
        super(log, listenersFactory, ID, NAME, DESCRIPTION, 1, 1);
        this.listenersFactory = listenersFactory;
        this.types = types;
        types.addObjectListener(this);
    }

    @Override
    public TypeInstance serialise(ConstantInstance<Object> o) {
        throw new HousemateCommsException("Cannot serialise a constant");
    }

    @Override
    public ConstantInstance<Object> deserialise(TypeInstance value) {
        String typeId = value.getValue();
        if(typeId == null) {
            getLog().w("Cannot deserialise constant, type id is null");
            return null;
        }
        RealType<?> type = types.get(typeId);
        if(type == null) {
            getLog().w("Cannot deserialise constant, no type for id " + typeId);
            return null;
        }
        return new ConstantInstance<>(listenersFactory, (RealType<Object>) type, value.getChildValues().getChildren().get(SUB_TYPE_ID));
    }

    @Override
    public void elementAdded(RealType<?> type) {
        // don't add self
        if(type.getId().equals(getId()))
            return;
        RealSubTypeImpl<Object> subType = new RealSubTypeImpl<>(getLog(), listenersFactory, SUB_TYPE_ID, SUB_TYPE_NAME, SUB_TYPE_DESCRIPTION, type.getId(), types);
        RealOptionImpl option = new RealOptionImpl(getLog(), listenersFactory, type.getId(), type.getName(), type.getDescription(), subType);
        ((RealList<RealOptionImpl>)getOptions()).add(option);
    }

    @Override
    public void elementRemoved(RealType<?> element) {
        getOptions().remove(element.getId());
    }
}
