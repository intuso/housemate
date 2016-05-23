package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.Feature;
import com.intuso.housemate.client.real.api.internal.RealFeature;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.JMSException;
import javax.jms.Session;

public final class RealFeatureImpl
        extends RealObject<Feature.Data, Feature.Listener<? super RealFeatureImpl>>
        implements RealFeature<RealListImpl<RealCommandImpl>, RealListImpl<RealValueImpl<?>>, RealFeatureImpl> {

    private final RealListImpl<RealCommandImpl> commands;
    private final RealListImpl<RealValueImpl<?>> values;

    @Inject
    public RealFeatureImpl(@Assisted final Logger logger,
                           @Assisted("id") String id,
                           @Assisted("name") String name,
                           @Assisted("description") String description,
                           ListenersFactory listenersFactory,
                           RealListImpl.Factory<RealCommandImpl> commandsFactory,
                           RealListImpl.Factory<RealValueImpl<?>> valuesFactory) {
        super(logger, new Feature.Data(id, name, description), listenersFactory);
        this.commands = commandsFactory.create(ChildUtil.logger(logger, Feature.COMMANDS_ID),
                Feature.COMMANDS_ID,
                "Commands",
                "The commands of this feature",
                Lists.<RealCommandImpl>newArrayList());
        this.values = valuesFactory.create(ChildUtil.logger(logger, Feature.VALUES_ID),
                Feature.VALUES_ID,
                "Values",
                "The values of this feature",
                Lists.<RealValueImpl<?>>newArrayList());
    }

    @Override
    protected void initChildren(String name, Session session) throws JMSException {
        super.initChildren(name, session);
        commands.init(ChildUtil.name(name, Feature.COMMANDS_ID), session);
        values.init(ChildUtil.name(name, Feature.VALUES_ID), session);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        commands.uninit();
        values.uninit();
    }

    @Override
    public final RealListImpl<RealCommandImpl> getCommands() {
        return commands;
    }

    @Override
    public RealListImpl<RealValueImpl<?>> getValues() {
        return values;
    }

    public interface Factory {
        RealFeatureImpl create(Logger logger,
                               @Assisted("id") String id,
                               @Assisted("name") String name,
                               @Assisted("description") String description);
    }
}
