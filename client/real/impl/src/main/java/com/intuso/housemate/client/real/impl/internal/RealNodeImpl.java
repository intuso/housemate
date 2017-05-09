package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.intuso.housemate.client.api.internal.object.Hardware;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.real.api.internal.RealNode;
import com.intuso.housemate.client.real.impl.internal.utils.AddHardwareCommand;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

public class RealNodeImpl
        extends RealObject<com.intuso.housemate.client.api.internal.object.Node.Data, com.intuso.housemate.client.api.internal.object.Node.Listener<? super RealNodeImpl>>
        implements RealNode<RealCommandImpl, RealListGeneratedImpl<RealTypeImpl<?>>, RealHardwareImpl, RealListPersistedImpl<Hardware.Data, RealHardwareImpl>, RealNodeImpl>,
        ServerBaseNode<RealCommandImpl, RealListGeneratedImpl<RealTypeImpl<?>>, RealListPersistedImpl<Hardware.Data, RealHardwareImpl>, RealNodeImpl>,
        AddHardwareCommand.Callback {

    private final RealListGeneratedImpl<RealTypeImpl<?>> types;
    private final RealListPersistedImpl<Hardware.Data, RealHardwareImpl> hardwares;
    private final RealCommandImpl addHardwareCommand;

    @AssistedInject
    public RealNodeImpl(@Assisted final Logger logger,
                        @Assisted("id") String id,
                        @Assisted("name") String name,
                        @Assisted("description") String description,
                        ManagedCollectionFactory managedCollectionFactory,
                        Sender.Factory senderFactory,
                        RealListGeneratedImpl.Factory<RealTypeImpl<?>> typesFactory,
                        RealListPersistedImpl.Factory<Hardware.Data, RealHardwareImpl> hardwaresFactory,
                        AddHardwareCommand.Factory addHardwareCommandFactory) {
        super(logger, new com.intuso.housemate.client.api.internal.object.Node.Data(id, name, description), managedCollectionFactory, senderFactory);
        this.types = typesFactory.create(ChildUtil.logger(logger, TYPES_ID),
                TYPES_ID,
                "Types",
                "Types",
                Lists.<RealTypeImpl<?>>newArrayList());
        this.hardwares = hardwaresFactory.create(ChildUtil.logger(logger, HARDWARES_ID),
                HARDWARES_ID,
                "Hardware",
                "Hardware");
        this.addHardwareCommand = addHardwareCommandFactory.create(ChildUtil.logger(logger, HARDWARES_ID),
                ChildUtil.logger(logger, ADD_HARDWARE_ID),
                ADD_HARDWARE_ID,
                ADD_HARDWARE_ID,
                "Add hardware",
                this,
                hardwares.getRemoveCallback());
    }

    public Logger getLogger() {
        return logger;
    }

    @Override
    protected void initChildren(String name) {
        super.initChildren(name);
        types.init(ChildUtil.name(name, TYPES_ID));
        hardwares.init(ChildUtil.name(name, HARDWARES_ID));
        addHardwareCommand.init(ChildUtil.name(name, ADD_HARDWARE_ID));
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        types.uninit();
        hardwares.uninit();
        addHardwareCommand.uninit();
    }

    @Override
    public RealListGeneratedImpl<RealTypeImpl<?>> getTypes() {
        return types;
    }

    @Override
    public RealListPersistedImpl<Hardware.Data, RealHardwareImpl> getHardwares() {
        return hardwares;
    }

    @Override
    public final void addHardware(RealHardwareImpl hardware) {
        hardwares.add(hardware);
    }

    @Override
    public RealCommandImpl getAddHardwareCommand() {
        return addHardwareCommand;
    }

    @Override
    public RealObject<?, ?> getChild(String id) {
        if(ADD_HARDWARE_ID.equals(id))
            return addHardwareCommand;
        else if(HARDWARES_ID.equals(id))
            return hardwares;
        else if(TYPES_ID.equals(id))
            return types;
        return null;
    }

    public interface Factory {
        RealNodeImpl create(Logger logger,
                            @Assisted("id") String id,
                            @Assisted("name") String name,
                            @Assisted("description") String description);
    }
}