package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.object.*;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.view.CommandView;
import com.intuso.housemate.client.api.internal.object.view.DeviceView;
import com.intuso.housemate.client.api.internal.object.view.ListView;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.real.api.internal.RealCommand;
import com.intuso.housemate.client.real.api.internal.RealDevice;
import com.intuso.housemate.client.real.impl.internal.type.TypeRepository;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;

public abstract class RealDeviceImpl<DATA extends Device.Data,
        LISTENER extends Device.Listener<? super DEVICE>,
        VIEW extends DeviceView<?>,
        DEVICE extends RealDeviceImpl<DATA, LISTENER, VIEW, DEVICE>>
        extends RealObject<DATA, LISTENER, VIEW>
        implements RealDevice<DATA, LISTENER, RealCommandImpl, RealListGeneratedImpl<RealDeviceComponentImpl>, VIEW, DEVICE> {

    private final RealCommandImpl renameCommand;
    private final RealListGeneratedImpl<RealDeviceComponentImpl> components;

    @Inject
    public RealDeviceImpl(@Assisted final Logger logger,
                          DATA data,
                          ManagedCollectionFactory managedCollectionFactory,
                          RealCommandImpl.Factory commandFactory,
                          RealParameterImpl.Factory parameterFactory,
                          RealListGeneratedImpl.Factory<RealDeviceComponentImpl> componentsFactory,
                          TypeRepository typeRepository) {
        super(logger, data, managedCollectionFactory);
        this.renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID),
                Renameable.RENAME_ID,
                Renameable.RENAME_ID,
                "Rename the device",
                new RealCommand.Performer() {
                    @Override
                    public void perform(Type.InstanceMap values) {
                        if(values != null) {
                            String newName = values.containsKey(Renameable.NAME_ID) ? values.get(Renameable.NAME_ID).getFirstValue() : data.getName();
                            String newDescription = values.containsKey(Renameable.DESCRIPTION_ID) ? values.get(Renameable.DESCRIPTION_ID).getFirstValue() : data.getDescription();
                            if (!(data.getName().equals(newName) && data.getDescription().equals(newDescription)))
                                rename(newName, newDescription);
                        }
                    }
                },
                Lists.newArrayList(
                        parameterFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID, Renameable.NAME_ID),
                                Renameable.NAME_ID,
                                Renameable.NAME_ID,
                                "The new name",
                                typeRepository.getType(new TypeSpec(String.class)),
                                1,
                                1),
                        parameterFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID, Renameable.DESCRIPTION_ID),
                                Renameable.DESCRIPTION_ID,
                                Renameable.DESCRIPTION_ID,
                                "The new description",
                                typeRepository.getType(new TypeSpec(String.class)),
                                1,
                                1)
                ));
        this.components = componentsFactory.create(ChildUtil.logger(logger, DEVICE_COMPONENTS_ID),
                DEVICE_COMPONENTS_ID,
                "Commands",
                "The commands of this feature",
                Lists.<RealDeviceComponentImpl>newArrayList());
    }

    @Override
    public Tree getTree(VIEW view, Tree.ReferenceHandler referenceHandler, Tree.Listener listener, List<ManagedCollection.Registration> listenerRegistrations) {

        // register the listener
        addTreeListener(view, listener, listenerRegistrations);

        // create a result even for a null view
        Tree result = new Tree(getData());

        // get anything else the view wants
        if(view != null && view.getMode() != null) {
            switch (view.getMode()) {

                // get recursively
                case ANCESTORS:
                    result.getChildren().put(RENAME_ID, renameCommand.getTree(new CommandView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(DEVICE_COMPONENTS_ID, components.getTree(new ListView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    break;

                // get all children using inner view. NB all children non-null because of load(). Can give children null views
                case CHILDREN:
                    result.getChildren().put(RENAME_ID, renameCommand.getTree(view.getRenameCommand(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(DEVICE_COMPONENTS_ID, components.getTree(view.getComponents(), referenceHandler, listener, listenerRegistrations));
                    break;

                case SELECTION:
                    if(view.getRenameCommand() != null)
                        result.getChildren().put(RENAME_ID, renameCommand.getTree(view.getRenameCommand(), referenceHandler, listener, listenerRegistrations));
                    if(view.getComponents() != null)
                        result.getChildren().put(DEVICE_COMPONENTS_ID, components.getTree(view.getComponents(), referenceHandler, listener, listenerRegistrations));
                    break;
            }
        }

        return result;
    }

    private void rename(String newName, String newDescription) {
        getData().setName(newName);
        for(Device.Listener<? super DEVICE> listener : listeners)
            listener.renamed(getThis(), data.getName(), data.getDescription(), newName, newDescription);
        data.setName(newName);
        data.setDescription(newDescription);
        dataUpdated();
    }

    @Override
    protected void initChildren(String name, Sender.Factory senderFactory, Receiver.Factory receiverFactory) {
        super.initChildren(name, senderFactory, receiverFactory);
        renameCommand.init(ChildUtil.name(name, Renameable.RENAME_ID), senderFactory, receiverFactory);
        components.init(ChildUtil.name(name, DEVICE_COMPONENTS_ID), senderFactory, receiverFactory);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        components.uninit();
    }

    @Override
    public RealCommandImpl getRenameCommand() {
        return renameCommand;
    }

    @Override
    public final RealListGeneratedImpl<RealDeviceComponentImpl> getDeviceComponents() {
        return components;
    }

    void clear() {
        for(RealDeviceComponentImpl deviceComponent : Lists.newArrayList(components))
            components.remove(deviceComponent.getId());
    }

    @Override
    public Object<?, ?, ?> getChild(String id) {
        if(RENAME_ID.equals(id))
            return renameCommand;
        else if(DEVICE_COMPONENTS_ID.equals(id))
            return components;
        return null;
    }

    public DEVICE getThis() {
        return (DEVICE) this;
    }
}
