package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.object.ConnectedDevice;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.real.api.internal.RealCommand;
import com.intuso.housemate.client.real.api.internal.RealConnectedDevice;
import com.intuso.housemate.client.real.impl.internal.type.TypeRepository;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

public final class RealConnectedDeviceImpl
        extends RealObject<ConnectedDevice.Data, ConnectedDevice.Listener<? super RealConnectedDeviceImpl>>
        implements RealConnectedDevice<RealCommandImpl,
                RealListGeneratedImpl<RealCommandImpl>,
                RealListGeneratedImpl<RealValueImpl<?>>,
                RealListGeneratedImpl<RealPropertyImpl<?>>,
                RealConnectedDeviceImpl> {

    private final static String PROPERTIES_DESCRIPTION = "The device's properties";

    private final RealCommandImpl renameCommand;
    private final RealListGeneratedImpl<RealCommandImpl> commands;
    private final RealListGeneratedImpl<RealValueImpl<?>> values;
    private final RealListGeneratedImpl<RealPropertyImpl<?>> properties;

    @Inject
    public RealConnectedDeviceImpl(@Assisted final Logger logger,
                                   @Assisted("id") String id,
                                   @Assisted("name") String name,
                                   @Assisted("description") String description,
                                   ManagedCollectionFactory managedCollectionFactory,
                                   RealCommandImpl.Factory commandFactory,
                                   RealParameterImpl.Factory parameterFactory,
                                   RealListGeneratedImpl.Factory<RealCommandImpl> commandsFactory,
                                   RealListGeneratedImpl.Factory<RealValueImpl<?>> valuesFactory,
                                   RealListGeneratedImpl.Factory<RealPropertyImpl<?>> propertiesFactory,
                                   TypeRepository typeRepository) {
        super(logger, new ConnectedDevice.Data(id, name, description), managedCollectionFactory);
        this.renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID),
                Renameable.RENAME_ID,
                Renameable.RENAME_ID,
                "Rename the device",
                new RealCommand.Performer() {
                    @Override
                    public void perform(Type.InstanceMap values) {
                        if(values != null && values.getChildren().containsKey(Renameable.NAME_ID)) {
                            String newName = values.getChildren().get(Renameable.NAME_ID).getFirstValue();
                            if (newName != null && !RealConnectedDeviceImpl.this.getName().equals(newName))
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
        this.properties = propertiesFactory.create(ChildUtil.logger(logger, PROPERTIES_ID),
                PROPERTIES_ID,
                PROPERTIES_ID,
                PROPERTIES_DESCRIPTION,
                Lists.<RealPropertyImpl<?>>newArrayList());
    }

    private void setName(String newName) {
        getData().setName(newName);
        for(ConnectedDevice.Listener<? super RealConnectedDeviceImpl> listener : listeners)
            listener.renamed(RealConnectedDeviceImpl.this, RealConnectedDeviceImpl.this.getName(), newName);
        data.setName(newName);
        sendData();
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        renameCommand.init(ChildUtil.name(name, Renameable.RENAME_ID), connection);
        commands.init(ChildUtil.name(name, COMMANDS_ID), connection);
        values.init(ChildUtil.name(name, VALUES_ID), connection);
        properties.init(ChildUtil.name(name, PROPERTIES_ID), connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        commands.uninit();
        values.uninit();
        properties.uninit();
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
    public final RealListGeneratedImpl<RealPropertyImpl<?>> getProperties() {
        return properties;
    }

    public interface Factory {
        RealConnectedDeviceImpl create(Logger logger,
                                       @Assisted("id") String id,
                                       @Assisted("name") String name,
                                       @Assisted("description") String description);
    }
}
