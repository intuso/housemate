package com.intuso.housemate.server.plugin.main.type.constant;

import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.housemate.client.real.api.internal.RealRoot;
import com.intuso.housemate.client.real.api.internal.RealType;
import com.intuso.housemate.client.real.impl.internal.RealOptionImpl;
import com.intuso.housemate.client.real.impl.internal.RealSubTypeImpl;
import com.intuso.housemate.client.real.impl.internal.type.RealChoiceType;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.object.api.internal.List;
import com.intuso.housemate.object.api.internal.TypeInstance;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class ConstantType extends RealChoiceType<ConstantInstance<Object>> implements List.Listener<RealType<?>> {

    private final static Logger logger = LoggerFactory.getLogger(ConstantType.class);

    public final static String ID = "constant";
    public final static String NAME = "Constant";
    public final static String DESCRIPTION = "A constant value";

    public final static String SUB_TYPE_ID = "value";
    public final static String SUB_TYPE_NAME = "Value";
    public final static String SUB_TYPE_DESCRIPTION = "The value of the constant";

    private final ListenersFactory listenersFactory;

    private final RealRoot root;

    @Inject
    public ConstantType(ListenersFactory listenersFactory, RealRoot root) {
        super(logger, listenersFactory, ID, NAME, DESCRIPTION, 1, 1);
        this.listenersFactory = listenersFactory;
        this.root = root;
        root.getTypes().addObjectListener(this);
    }

    @Override
    public TypeInstance serialise(ConstantInstance<Object> o) {
        throw new HousemateCommsException("Cannot serialise a constant");
    }

    @Override
    public ConstantInstance<Object> deserialise(TypeInstance value) {
        String typeId = value.getValue();
        if(typeId == null) {
            getLogger().warn("Cannot deserialise constant, type id is null");
            return null;
        }
        RealType<?> type = root.getTypes().get(typeId);
        if(type == null) {
            getLogger().warn("Cannot deserialise constant, no type for id " + typeId);
            return null;
        }
        return new ConstantInstance<>(listenersFactory, (RealType<Object>) type, value.getChildValues().getChildren().get(SUB_TYPE_ID));
    }

    @Override
    public void elementAdded(RealType<?> type) {
        // don't add self
        if(type.getId().equals(getId()))
            return;
        RealSubTypeImpl<Object> subType = new RealSubTypeImpl<>(getLogger(), listenersFactory, SUB_TYPE_ID, SUB_TYPE_NAME, SUB_TYPE_DESCRIPTION, type.getId(), root.getTypes());
        RealOptionImpl option = new RealOptionImpl(getLogger(), listenersFactory, type.getId(), type.getName(), type.getDescription(), subType);
        ((RealList<RealOptionImpl>)getOptions()).add(option);
    }

    @Override
    public void elementRemoved(RealType<?> element) {
        getOptions().remove(element.getId());
    }
}
