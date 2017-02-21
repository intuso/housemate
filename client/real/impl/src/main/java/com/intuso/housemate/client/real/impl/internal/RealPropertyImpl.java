package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.util.Types;
import com.intuso.housemate.client.api.internal.object.Command;
import com.intuso.housemate.client.api.internal.object.Property;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.type.ObjectReference;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.proxy.internal.object.ProxyDevice;
import com.intuso.housemate.client.real.api.internal.RealProperty;
import com.intuso.housemate.client.real.impl.internal.type.TypeRepository;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
import java.util.List;

/**
 * @param <O> the type of the property's value
 */
public class RealPropertyImpl<O>
        extends RealValueBaseImpl<O, Property.Data, Property.Listener<? super RealPropertyImpl<O>>, RealPropertyImpl<O>>
        implements RealProperty<O, RealTypeImpl<O>, RealCommandImpl, RealPropertyImpl<O>> {

    private RealCommandImpl setCommand;

    /**
     * @param logger {@inheritDoc}
     * @param managedCollectionFactory
     * @param type the property's type
     * @param values the property's initial value
     */
    @Inject
    public RealPropertyImpl(@Assisted Logger logger,
                            @Assisted("id") String id,
                            @Assisted("name") String name,
                            @Assisted("description") String description,
                            @Assisted RealTypeImpl type,
                            @Assisted("min") int minValues,
                            @Assisted("max") int maxValues,
                            @Assisted Iterable values,
                            ManagedCollectionFactory managedCollectionFactory,
                            RealCommandImpl.Factory commandFactory,
                            RealParameterImpl.Factory parameterFactory) {
        super(logger, new Property.Data(id, name, description, type.getId(), minValues, maxValues), managedCollectionFactory, type, values);
        setCommand = commandFactory.create(ChildUtil.logger(logger, Property.SET_COMMAND_ID),
                Property.SET_COMMAND_ID,
                Property.SET_COMMAND_ID,
                "The function to change the property's value",
                new RealCommandImpl.Performer() {
                    @Override
                    public void perform(Type.InstanceMap serialisedValues) {
                        List<O> values = RealTypeImpl.deserialiseAll(getType(), serialisedValues.getChildren().get(Property.VALUE_PARAM));
                        RealPropertyImpl.this.setValues(values);
                    }
                },
                Lists.newArrayList(parameterFactory.create(
                        ChildUtil.logger(logger, Property.SET_COMMAND_ID, Property.VALUE_PARAM),
                        Property.VALUE_PARAM,
                        Property.VALUE_PARAM,
                        "The new value for the property",
                        type,
                        minValues,
                        maxValues)));
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        setCommand.init(ChildUtil.name(name, Property.SET_COMMAND_ID), connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        setCommand.uninit();
    }

    @Override
    public void set(final O value, Command.PerformListener<? super RealCommandImpl> listener) {
        getSetCommand().perform(new Type.InstanceMap() {
            {
                getChildren().put(Property.VALUE_ID, RealTypeImpl.serialiseAll(getType(), value));
            }
        }, listener);
    }

    @Override
    public RealCommandImpl getSetCommand() {
        return setCommand;
    }

    public interface Factory {
        RealPropertyImpl<?> create(Logger logger,
                                   @Assisted("id") String id,
                                   @Assisted("name") String name,
                                   @Assisted("description") String description,
                                   RealTypeImpl type,
                                   @Assisted("min") int minValues,
                                   @Assisted("max") int maxValues,
                                   Iterable values);
    }

    public static class LoadPersistedDeviceObjectReference implements RealListPersistedImpl.ElementFactory<Property.Data, RealPropertyImpl<ObjectReference<ProxyDevice.Simple>>> {

        private final RealPropertyImpl.Factory factory;
        private final TypeRepository typeRepository;

        @Inject
        public LoadPersistedDeviceObjectReference(Factory factory, TypeRepository typeRepository) {
            this.factory = factory;
            this.typeRepository = typeRepository;
        }

        @Override
        public RealPropertyImpl<ObjectReference<ProxyDevice.Simple>> create(Logger logger, Property.Data data, RealListPersistedImpl.RemoveCallback<RealPropertyImpl<ObjectReference<ProxyDevice.Simple>>> removeCallback) {
            return (RealPropertyImpl<ObjectReference<ProxyDevice.Simple>>)
                    factory.create(logger, data.getId(), data.getName(), data.getDescription(), typeRepository.getType(new TypeSpec(Types.newParameterizedType(ObjectReference.class, ProxyDevice.Simple.class))), 1, 1, Lists.newArrayList());
        }
    }
}
