package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.Option;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.RealListGeneratedImpl;
import com.intuso.housemate.client.real.impl.internal.RealOptionImpl;
import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import com.intuso.utilities.listener.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Type for selecting options from a list
 */
public abstract class RealChoiceType<O>
        extends RealTypeImpl<O>
        implements Option.Container<RealListGeneratedImpl<? extends RealOptionImpl>> {

    public final static String OPTIONS = "options";

    protected final RealListGeneratedImpl<RealOptionImpl> options;

    /**
     * @param logger the log
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param managedCollectionFactory
     * @param options the type's options
     */
    protected RealChoiceType(@Assisted Logger logger,
                             @Assisted("id") String id,
                             @Assisted("name") String name,
                             @Assisted("description") String description,
                             @Assisted Iterable<RealOptionImpl> options,
                             ManagedCollectionFactory managedCollectionFactory,
                             RealListGeneratedImpl.Factory<RealOptionImpl> optionsFactory) {
        super(logger, new ChoiceData(id, name, description), managedCollectionFactory);
        this.options = optionsFactory.create(logger,
                OPTIONS,
                OPTIONS,
                "The options for the choice",
                options
        );
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        options.init(ChildUtil.name(name, OPTIONS), connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        options.uninit();
    }

    @Override
    public RealListGeneratedImpl<RealOptionImpl> getOptions() {
        return options;
    }

    public interface Factory {
        <O> RealChoiceType<O> create(Logger logger,
                                     @Assisted("id") String id,
                                     @Assisted("name") String name,
                                     @Assisted("description") String description,
                                     Iterable<RealOptionImpl> options);
    }
}
