package com.intuso.housemate.broker.plugin.type.constant;

import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeWrappable;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealOption;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealSubType;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.RealChoiceType;

/**
 */
public class ConstantType extends RealChoiceType<ConstantInstance<Object>> implements ListListener<RealType<?, ?, ?>> {

    public final static String ID = "constant";
    public final static String NAME = "Constant";
    public final static String DESCRIPTION = "A constant value";

    public final static String SUB_TYPE_ID = "value";
    public final static String SUB_TYPE_NAME = "Value";
    public final static String SUB_TYPE_DESCRIPTION = "The value of the constant";

    private final RealList<TypeWrappable<?>, RealType<?, ?, ?>> types;

    public ConstantType(RealResources resources, RealList<TypeWrappable<?>, RealType<?, ?, ?>> types) {
        super(resources, ID, NAME, DESCRIPTION, 1, 1);
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
        return new ConstantInstance<Object>((RealType<?,?,Object>) type, value.getChildValues().get(SUB_TYPE_ID));
    }

    @Override
    public void elementAdded(RealType<?, ?, ?> type) {
        // don't add self
        if(type.getId().equals(getId()))
            return;
        RealSubType<Object> subType = new RealSubType<Object>(getResources(), SUB_TYPE_ID, SUB_TYPE_NAME, SUB_TYPE_DESCRIPTION, (RealType<?,?,Object>) type);
        RealOption option = new RealOption(getResources(), type.getId(), type.getName(), type.getDescription(), subType);
        getOptions().add(option);
    }

    @Override
    public void elementRemoved(RealType<?, ?, ?> element) {
        getOptions().remove(element.getId());
    }
}
