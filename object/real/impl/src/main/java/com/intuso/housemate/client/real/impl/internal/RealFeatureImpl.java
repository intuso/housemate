package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.Feature;
import com.intuso.housemate.client.real.api.internal.RealFeature;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

public final class RealFeatureImpl
        extends RealObject<Feature.Data, Feature.Listener<? super RealFeatureImpl>>
        implements RealFeature<RealListGeneratedImpl<RealCommandImpl>, RealListGeneratedImpl<RealValueImpl<?>>, RealFeatureImpl> {

    private final RealListGeneratedImpl<RealCommandImpl> commands;
    private final RealListGeneratedImpl<RealValueImpl<?>> values;

    @Inject
    public RealFeatureImpl(@Assisted final Logger logger,
                           @Assisted("id") String id,
                           @Assisted("name") String name,
                           @Assisted("description") String description,
                           ListenersFactory listenersFactory,
                           RealListGeneratedImpl.Factory<RealCommandImpl> commandsFactory,
                           RealListGeneratedImpl.Factory<RealValueImpl<?>> valuesFactory) {
        super(logger, false, new Feature.Data(id, name, description), listenersFactory);
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
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        commands.init(ChildUtil.name(name, Feature.COMMANDS_ID), connection);
        values.init(ChildUtil.name(name, Feature.VALUES_ID), connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        commands.uninit();
        values.uninit();
    }

    @Override
    public final RealListGeneratedImpl<RealCommandImpl> getCommands() {
        return commands;
    }

    @Override
    public RealListGeneratedImpl<RealValueImpl<?>> getValues() {
        return values;
    }

    public interface Factory {
        RealFeatureImpl create(Logger logger,
                               @Assisted("id") String id,
                               @Assisted("name") String name,
                               @Assisted("description") String description);
    }
}
