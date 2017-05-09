package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.real.api.internal.RealCommand;
import com.intuso.housemate.client.real.api.internal.RealDevice;
import com.intuso.housemate.client.real.impl.internal.annotation.AnnotationParser;
import com.intuso.housemate.client.real.impl.internal.type.TypeRepository;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

public abstract class RealDeviceImpl<DATA extends Device.Data,
        LISTENER extends Device.Listener<? super DEVICE>,
        DEVICE extends RealDeviceImpl<DATA, LISTENER, DEVICE>>
        extends RealObject<DATA, LISTENER>
        implements RealDevice<DATA, LISTENER, RealCommandImpl, RealListGeneratedImpl<RealCommandImpl>, RealListGeneratedImpl<RealValueImpl<?>>, DEVICE> {

    private final static String PROPERTIES_DESCRIPTION = "The device's properties";

    private final AnnotationParser annotationParser;

    private final RealCommandImpl renameCommand;
    private final RealListGeneratedImpl<RealCommandImpl> commands;
    private final RealListGeneratedImpl<RealValueImpl<?>> values;

    @Inject
    public RealDeviceImpl(@Assisted final Logger logger,
                          DATA data,
                          ManagedCollectionFactory managedCollectionFactory,
                          Sender.Factory senderFactory,
                          AnnotationParser annotationParser,
                          RealCommandImpl.Factory commandFactory,
                          RealParameterImpl.Factory parameterFactory,
                          RealListGeneratedImpl.Factory<RealCommandImpl> commandsFactory,
                          RealListGeneratedImpl.Factory<RealValueImpl<?>> valuesFactory,
                          TypeRepository typeRepository) {
        super(logger, data, managedCollectionFactory, senderFactory);
        this.annotationParser = annotationParser;
        this.renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID),
                Renameable.RENAME_ID,
                Renameable.RENAME_ID,
                "Rename the device",
                new RealCommand.Performer() {
                    @Override
                    public void perform(Type.InstanceMap values) {
                        if(values != null && values.getChildren().containsKey(Renameable.NAME_ID)) {
                            String newName = values.getChildren().get(Renameable.NAME_ID).getFirstValue();
                            if (newName != null && !RealDeviceImpl.this.getName().equals(newName))
                                setName(newName);
                        }
                    }
                },
                Lists.newArrayList(parameterFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID, Renameable.NAME_ID),
                        Renameable.NAME_ID,
                        Renameable.NAME_ID,
                        "The new name",
                        typeRepository.getType(new TypeSpec(String.class)),
                        1,
                        1)));
        this.commands = commandsFactory.create(ChildUtil.logger(logger, COMMANDS_ID),
                COMMANDS_ID,
                "Commands",
                "The commands of this feature",
                Lists.<RealCommandImpl>newArrayList());
        this.values = valuesFactory.create(ChildUtil.logger(logger, VALUES_ID),
                VALUES_ID,
                "Values",
                "The values of this feature",
                Lists.<RealValueImpl<?>>newArrayList());
    }

    private void setName(String newName) {
        getData().setName(newName);
        for(Device.Listener<? super DEVICE> listener : listeners)
            listener.renamed(getThis(), RealDeviceImpl.this.getName(), newName);
        data.setName(newName);
        sendData();
    }

    @Override
    protected void initChildren(String name) {
        super.initChildren(name);
        renameCommand.init(ChildUtil.name(name, Renameable.RENAME_ID));
        commands.init(ChildUtil.name(name, COMMANDS_ID));
        values.init(ChildUtil.name(name, VALUES_ID));
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        commands.uninit();
        values.uninit();
    }

    @Override
    public RealCommandImpl getRenameCommand() {
        return renameCommand;
    }

    @Override
    public final RealListGeneratedImpl<RealCommandImpl> getCommands() {
        return commands;
    }

    @Override
    public RealListGeneratedImpl<RealValueImpl<?>> getValues() {
        return values;
    }

    @Override
    public Object<?, ?> getChild(String id) {
        if(RENAME_ID.equals(id))
            return renameCommand;
        else if(COMMANDS_ID.equals(id))
            return commands;
        else if(VALUES_ID.equals(id))
            return values;
        return null;
    }

    void clear() {
        for(RealCommandImpl command : Lists.newArrayList(commands))
            commands.remove(command.getId());
        for(RealValueImpl<?> value : Lists.newArrayList(values))
            values.remove(value.getId());
    }

    void wrap(java.lang.Object object) {
        clear();
        // add the commands, values and properties specified by the object
        for(RealCommandImpl command : annotationParser.findCommands(ChildUtil.logger(logger, COMMANDS_ID), "", object))
            commands.add(command);
        for(RealValueImpl<?> value : annotationParser.findValues(ChildUtil.logger(logger, VALUES_ID), "", object))
            values.add(value);
    }

    public DEVICE getThis() {
        return (DEVICE) this;
    }
}
