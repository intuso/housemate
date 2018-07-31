package com.intuso.housemate.client.real.impl.internal;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.Parameter;
import com.intuso.housemate.client.api.internal.object.Tree;
import com.intuso.housemate.client.api.internal.object.view.ParameterView;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.housemate.client.real.api.internal.RealParameter;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;

/**
 * @param <O> the type of the parameter's value
 */
public final class RealParameterImpl<O>
        extends RealObject<Parameter.Data, Parameter.Listener<? super RealParameterImpl<O>>, ParameterView>
        implements RealParameter<O, RealTypeImpl<O>, RealParameterImpl<O>> {

    private RealTypeImpl<O> type;

    /**
     * @param logger {@inheritDoc}
     * @param managedCollectionFactory
     * @param type the type of the parameter's value
     */
    @Inject
    public RealParameterImpl(@Assisted final Logger logger,
                             @Assisted("id") String id,
                             @Assisted("name") String name,
                             @Assisted("description") String description,
                             @Assisted RealTypeImpl type,
                             @Assisted("min") int minValues,
                             @Assisted("max") int maxValues,
                             ManagedCollectionFactory managedCollectionFactory) {
        super(logger, new Parameter.Data(id, name, description, type.getId(), minValues, maxValues), managedCollectionFactory);
        this.type = type;
    }

    @Override
    public ParameterView createView(View.Mode mode) {
        return new ParameterView(mode);
    }

    @Override
    public Tree getTree(ParameterView view, Tree.ReferenceHandler referenceHandler, Tree.Listener listener, List<ManagedCollection.Registration> listenerRegistrations) {

        // register the listener
        addTreeListener(view, listener, listenerRegistrations);

        return new Tree(getData());
    }

    @Override
    public final RealTypeImpl<O> getType() {
        return type;
    }

    @Override
    public RealObject<?, ?, ?> getChild(String id) {
        return null;
    }

    public interface Factory {
        RealParameterImpl<?> create(Logger logger,
                                    @Assisted("id") String id,
                                    @Assisted("name") String name,
                                    @Assisted("description") String description,
                                    RealTypeImpl type,
                                    @Assisted("min") int minValues,
                                    @Assisted("max") int maxValues);
    }
}
