package com.intuso.housemate.client.real.impl.internal;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.SubType;
import com.intuso.housemate.client.api.internal.object.Tree;
import com.intuso.housemate.client.api.internal.object.view.NoView;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.real.api.internal.RealSubType;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;

/**
 * @param <O> the type of the sub type's value
 */
public final class RealSubTypeImpl<O>
        extends RealObject<SubType.Data, SubType.Listener<? super RealSubTypeImpl<O>>, NoView>
        implements RealSubType<O, RealTypeImpl<O>, RealSubTypeImpl<O>> {

    private final RealTypeImpl<O> type;

    /**
     * @param logger {@inheritDoc}
     * @param managedCollectionFactory
     * @param type
     */
    @Inject
    public RealSubTypeImpl(@Assisted Logger logger,
                           @Assisted("id") String id,
                           @Assisted("name") String name,
                           @Assisted("description") String description,
                           @Assisted RealTypeImpl type,
                           @Assisted("min") int minValues,
                           @Assisted("max") int maxValues,
                           ManagedCollectionFactory managedCollectionFactory,
                           Sender.Factory senderFactory) {
        super(logger, new SubType.Data(id, name, description, type.getId(), minValues, maxValues), managedCollectionFactory, senderFactory);
        this.type = type;
    }

    @Override
    public NoView createView(View.Mode mode) {
        return new NoView(mode);
    }

    @Override
    public Tree getTree(NoView view, Tree.Listener listener, List<ManagedCollection.Registration> listenerRegistrations) {

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
        RealSubTypeImpl<?> create(Logger logger,
                                  @Assisted("id") String id,
                                  @Assisted("name") String name,
                                  @Assisted("description") String description,
                                  RealTypeImpl type,
                                  @Assisted("min") int minValues,
                                  @Assisted("max") int maxValues);
    }
}
