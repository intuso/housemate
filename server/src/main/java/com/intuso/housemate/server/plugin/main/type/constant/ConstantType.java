package com.intuso.housemate.server.plugin.main.type.constant;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealOption;
import com.intuso.housemate.object.real.RealSubType;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.RealChoiceType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class ConstantType extends RealChoiceType<ConstantInstance<Object>> implements ListListener<RealType<?, ?, ?>> {

    public final static String ID = "constant";
    public final static String NAME = "Constant";
    public final static String DESCRIPTION = "A constant value";

    public final static String SUB_TYPE_ID = "value";
    public final static String SUB_TYPE_NAME = "Value";
    public final static String SUB_TYPE_DESCRIPTION = "The value of the constant";

    private final ListenersFactory listenersFactory;

    private final RealList<TypeData<?>, RealType<?, ?, ?>> types;

    @Inject
    public ConstantType(Log log, ListenersFactory listenersFactory, RealList<TypeData<?>, RealType<?, ?, ?>> types) {
        super(log, listenersFactory, ID, NAME, DESCRIPTION, 1, 1);
        this.listenersFactory = listenersFactory;
        this.types = types;
        types.addObjectListener(this);
    }

    @Override
    public TypeInstance serialise(ConstantInstance<Object> o) {
        throw new HousemateRuntimeException("Cannot serialise a constant");
    }

    @Override
    public ConstantInstance<Object> deserialise(TypeInstance value) {
        String typeId = value.getValue();
        if(typeId == null) {
            getLog().w("Cannot deserialise constant, type id is null");
            return null;
        }
        RealType<?, ?, ?> type = types.get(typeId);
        if(type == null) {
            getLog().w("Cannot deserialise constant, no type for id " + typeId);
            return null;
        }
        return new ConstantInstance<>(listenersFactory, (RealType<?,?,Object>) type, value.getChildValues().getChildren().get(SUB_TYPE_ID));
    }

    @Override
    public void elementAdded(RealType<?, ?, ?> type) {
        // don't add self
        if(type.getId().equals(getId()))
            return;
        RealSubType<Object> subType = new RealSubType<>(getLog(), listenersFactory, SUB_TYPE_ID, SUB_TYPE_NAME, SUB_TYPE_DESCRIPTION, type.getId(), types);
        RealOption option = new RealOption(getLog(), listenersFactory, type.getId(), type.getName(), type.getDescription(), subType);
        getOptions().add(option);
    }

    @Override
    public void elementRemoved(RealType<?, ?, ?> element) {
        getOptions().remove(element.getId());
    }
}
