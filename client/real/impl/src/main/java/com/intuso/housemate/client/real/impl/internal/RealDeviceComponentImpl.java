package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.DeviceComponent;
import com.intuso.housemate.client.api.internal.object.Tree;
import com.intuso.housemate.client.api.internal.object.view.DeviceComponentView;
import com.intuso.housemate.client.api.internal.object.view.ListView;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.real.api.internal.RealDeviceComponent;
import com.intuso.housemate.client.real.impl.internal.annotation.AnnotationParser;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;
import java.util.Set;

/**
 * Base class for all hardwares
 */
public final class RealDeviceComponentImpl
        extends RealObject<DeviceComponent.Data, DeviceComponent.Listener<? super RealDeviceComponentImpl>, DeviceComponentView>
        implements RealDeviceComponent<
                RealListGeneratedImpl<RealCommandImpl>,
                RealListGeneratedImpl<RealValueImpl<?>>,
                RealDeviceComponentImpl> {

    private final static String COMMANDS_DESCRIPTION = "The hardware's commands";
    private final static String VALUES_DESCRIPTION = "The hardware's values";

    private final AnnotationParser annotationParser;

    private final RealListGeneratedImpl<RealCommandImpl> commands;
    private final RealListGeneratedImpl<RealValueImpl<?>> values;

    /**
     * @param logger {@inheritDoc}
     * @param managedCollectionFactory
     */
    @Inject
    public RealDeviceComponentImpl(@Assisted final Logger logger,
                                   @Assisted("id") String id,
                                   @Assisted("name") String name,
                                   @Assisted("description") String description,
                                   ManagedCollectionFactory managedCollectionFactory,
                                   AnnotationParser annotationParser,
                                   RealListGeneratedImpl.Factory<RealCommandImpl> commandsFactory,
                                   RealListGeneratedImpl.Factory<RealValueImpl<?>> valuesFactory) {
        super(logger, new DeviceComponent.Data(id, name, description), managedCollectionFactory);
        this.annotationParser = annotationParser;
        this.commands = commandsFactory.create(ChildUtil.logger(logger, DeviceComponent.COMMANDS_ID),
                DeviceComponent.COMMANDS_ID,
                DeviceComponent.COMMANDS_ID,
                COMMANDS_DESCRIPTION,
                Lists.<RealCommandImpl>newArrayList());
        this.values = valuesFactory.create(ChildUtil.logger(logger, DeviceComponent.VALUES_ID),
                DeviceComponent.VALUES_ID,
                DeviceComponent.VALUES_ID,
                VALUES_DESCRIPTION,
                Lists.<RealValueImpl<?>>newArrayList());
    }

    @Override
    public Set<String> getClasses() {
        return getData().getClasses();
    }

    @Override
    public Set<String> getAbilities() {
        return getData().getAbilities();
    }

    @Override
    public DeviceComponentView createView(View.Mode mode) {
        return new DeviceComponentView(mode);
    }

    @Override
    public Tree getTree(DeviceComponentView view, Tree.ReferenceHandler referenceHandler, Tree.Listener listener, List<ManagedCollection.Registration> listenerRegistrations) {

        // register the listener
        addTreeListener(view, listener, listenerRegistrations);

        // create a result even for a null view
        Tree result = new Tree(getData());

        // get anything else the view wants
        if(view != null && view.getMode() != null) {
            switch (view.getMode()) {

                // get recursively
                case ANCESTORS:
                    result.getChildren().put(COMMANDS_ID, commands.getTree(new ListView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(VALUES_ID, values.getTree(new ListView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    break;

                    // get all children using inner view. NB all children non-null because of load(). Can give children null views
                case CHILDREN:
                    result.getChildren().put(COMMANDS_ID, commands.getTree(view.getCommands(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(VALUES_ID, values.getTree(view.getValues(), referenceHandler, listener, listenerRegistrations));
                    break;

                case SELECTION:
                    if(view.getCommands() != null)
                        result.getChildren().put(COMMANDS_ID, commands.getTree(view.getCommands(), referenceHandler, listener, listenerRegistrations));
                    if(view.getValues() != null)
                        result.getChildren().put(VALUES_ID, values.getTree(view.getValues(), referenceHandler, listener, listenerRegistrations));
                    break;
            }

        }

        return result;
    }

    @Override
    protected void initChildren(String name, Sender.Factory senderFactory, Receiver.Factory receiverFactory) {
        super.initChildren(name, senderFactory, receiverFactory);
        commands.init(ChildUtil.name(name, DeviceComponent.COMMANDS_ID), senderFactory, receiverFactory);
        values.init(ChildUtil.name(name, DeviceComponent.VALUES_ID), senderFactory, receiverFactory);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        commands.uninit();
        values.uninit();
    }

    @Override
    public RealListGeneratedImpl<RealCommandImpl> getCommands() {
        return commands;
    }

    @Override
    public RealListGeneratedImpl<RealValueImpl<?>> getValues() {
        return values;
    }

    @Override
    public RealObject<?, ?, ?> getChild(String id) {
        if(COMMANDS_ID.equals(id))
            return commands;
        else if(VALUES_ID.equals(id))
            return values;
        return null;
    }

    public interface Factory {
        RealDeviceComponentImpl create(Logger logger,
                                       @Assisted("id") String id,
                                       @Assisted("name") String name,
                                       @Assisted("description") String description);
    }
}
