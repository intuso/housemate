package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.util.Types;
import com.intuso.housemate.client.api.internal.object.Tree;
import com.intuso.housemate.client.api.internal.object.Value;
import com.intuso.housemate.client.api.internal.object.view.ValueView;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.housemate.client.api.internal.type.ObjectReference;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.proxy.internal.object.ProxyDevice;
import com.intuso.housemate.client.real.api.internal.RealValue;
import com.intuso.housemate.client.real.impl.internal.type.TypeRepository;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.annotation.Nullable;

/**
 * @param <O> the type of this value's value
 */
public final class RealValueImpl<O>
        extends RealValueBaseImpl<O, Value.Data, Value.Listener<? super RealValueImpl<O>>, ValueView, RealValueImpl<O>>
        implements RealValue<O, RealTypeImpl<O>, RealValueImpl<O>> {

    /**
     * @param logger {@inheritDoc}
     * @param managedCollectionFactory
     * @param type the type of the value's value
     */
    @Inject
    public RealValueImpl(@Assisted Logger logger,
                         @Assisted("id") String id,
                         @Assisted("name") String name,
                         @Assisted("description") String description,
                         @Assisted RealTypeImpl type,
                         @Assisted("min") int minValues,
                         @Assisted("max") int maxValues,
                         @Assisted @Nullable Iterable values,
                         ManagedCollectionFactory managedCollectionFactory,
                         Receiver.Factory receiverFactory,
                         Sender.Factory senderFactory) {
        super(logger, new Value.Data(id, name, description, type.getId(), minValues, maxValues), managedCollectionFactory, receiverFactory, senderFactory, type, values);
    }

    @Override
    public ValueView createView(View.Mode mode) {
        return new ValueView(mode);
    }

    @Override
    public Tree getTree(ValueView view) {
        return new Tree(getData());
    }

    @Override
    public RealObject<?, ?, ?> getChild(String id) {
        return null;
    }

    public interface Factory {
        RealValueImpl<?> create(Logger logger,
                                @Assisted("id") String id,
                                @Assisted("name") String name,
                                @Assisted("description") String description,
                                RealTypeImpl type,
                                @Assisted("min") int minValues,
                                @Assisted("max") int maxValues,
                                @Nullable Iterable values);
    }

    public static class LoadPersistedDeviceObjectReference implements RealListPersistedImpl.ElementFactory<Value.Data, RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>> {

        private final RealValueImpl.Factory factory;
        private final TypeRepository typeRepository;

        @Inject
        public LoadPersistedDeviceObjectReference(RealValueImpl.Factory factory, TypeRepository typeRepository) {
            this.factory = factory;
            this.typeRepository = typeRepository;
        }

        @Override
        public RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>> create(Logger logger, Value.Data data, RealListPersistedImpl.RemoveCallback<RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>> removeCallback) {
            return (RealValueImpl<ObjectReference<ProxyDevice<?, ?, ?, ?, ?, ?, ?>>>)
                    factory.create(logger,
                            data.getId(),
                            data.getName(),
                            data.getDescription(),
                            typeRepository.getType(new TypeSpec(Types.newParameterizedType(ObjectReference.class, ProxyDevice.class))),
                            1,
                            1,
                            Lists.newArrayList());
        }
    }
}
